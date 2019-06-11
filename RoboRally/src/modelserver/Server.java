package modelserver;

import javafx.application.Application;
import javafx.stage.Stage;
import modelserver.game.Game;
import modelserver.game.Player;
import utils.instructions.Instruction;
import utils.json.JSONDecoder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static utils.Parameter.*;
import static utils.instructions.Instruction.ServerToClientInstructionType.*;

/**
 * This class implements the server. <br>
 * It will communicate with the clients.
 *
 * @author Ivan Dovecar
 */
public class Server extends Application {

    private ArrayList<ClientWrapper> connectedClients;
    private ArrayList<Player> players = new ArrayList<>();
    private boolean gameIsInitialized = false;
    private boolean gameIsRunning = false;
    private static final Logger logger = Logger.getLogger( Server.class.getName() );


    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Starting server...");

        //  Open socket for incoming connections, if socket already exists start aborts
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

        connectedClients = new ArrayList<>();
        boolean isAcceptingNewClients = true;

        while(isAcceptingNewClients) { //Runs forever at the moment
            logger.info("Waiting for new client...");
            //New client connects: (accept() waits for new client)
            Socket clientSocket = serverSocket.accept();
            logger.info("Client connected from: " + clientSocket.getInetAddress().getHostAddress());

            //ServerReaderTask that reads incoming messages from clients -> Every client has its own Task/Thread
            ServerReaderTask task = new ServerReaderTask(clientSocket, this);
            Thread clientHandlerThread = new Thread(task);
            clientHandlerThread.start();
        }
        //Server shuts down:
        serverSocket.close();
        logger.info("Server shut down.");
    }

    private void startGame() {
        Game game = new Game(this);
        game.startGame(players);
        gameIsRunning = true;
    }


    private class ServerReaderTask extends Thread {

        private Socket clientSocket;
        private Server server;

        ServerReaderTask(Socket clientSocket, Server server) {
            this.clientSocket = clientSocket;
            this.server = server;
        }

        @Override
        public void run() {
            try {
                //WRITER:
                ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());

                //READER:
                ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
                Instruction instruction;

                while((instruction = (Instruction) reader.readObject()) != null) {
                    Instruction.ClientToServerInstructionType clientToServerInstructionType = instruction.getClientToServerInstructionType();
                    String content = instruction.getContent();

                    switch (clientToServerInstructionType) {
                        //Check if name is already used, if not, register client
                        case CHECK_NAME: {
                            boolean success = true;
                            for (ClientWrapper client : connectedClients) {
                                if (client.name.equals(content)) {
                                    logger.info("Client " + content + " refused (name already exists)");

                                    writer.writeObject(new Instruction(NAME_INVALID, ""));
                                    writer.flush();
                                    success = false;
                                    break;
                                }
                            }
                            if (success) {
                                logger.info("Client " + content + " successfully registered");

                                //Add new Client to list connected clients
                                connectedClients.add(new ClientWrapper(clientSocket, content, writer));

                                // TODO check if the following three lines are necessary
                                writer.writeObject(new Instruction(NAME_SUCCESS, ""));
                                writer.flush();

                                //Send message to all already active clients
                                for (ClientWrapper client : connectedClients) {
                                    if(!client.socket.equals(clientSocket)) {
                                        client.writer.writeObject(new Instruction(CLIENT_JOINED, content));
                                        client.writer.flush();

                                    } else { //New Client receives different message:
                                        client.writer.writeObject(new Instruction(CLIENT_WELCOME, content));
                                        client.writer.flush();
                                    }
                                }
                                //Register all current activeClients at new Client
                                for(ClientWrapper client : connectedClients) {
                                    if (!client.socket.equals(clientSocket)) {
                                        writer.writeObject(new Instruction(CLIENT_REGISTER, client.name));
                                        writer.flush();
                                    }
                                }
                            }
                            break;
                        }
                        case SEND_MESSAGE: {
                            //Stream to get client's name (because atm only the socket is known)
                            String clientName = connectedClients.stream().
                                    filter(clientWrapper -> clientWrapper.socket.equals(clientSocket)).
                                    findFirst().get().name;
                            //Send message to all clients:
                            for(ClientWrapper client : connectedClients) {
                                client.writer.writeObject(new Instruction(NEW_MESSAGE,
                                        clientName + ": " + content));
                                client.writer.flush();
                            }
                            break;
                        }
                        case SEND_PRIVATE_MESSAGE: {
                            //Stream to get client's name
                            String sendingClientName = connectedClients.stream().
                                    filter(clientWrapper -> clientWrapper.socket.equals(clientSocket)).
                                    findFirst().get().name;
                            for(ClientWrapper client : connectedClients){
                                if(instruction.getAddressedClient().equals(client.name)){
                                    client.writer.writeObject(new Instruction(NEW_MESSAGE,
                                            sendingClientName + ": (private) " + content));
                                    client.writer.flush();
                                }
                                if(sendingClientName.equals(client.name)){
                                    client.writer.writeObject(new Instruction(NEW_MESSAGE,
                                            sendingClientName + ": @" + instruction.getAddressedClient() + " (private) " + content));
                                    client.writer.flush();
                                }
                            }
                            break;
                        }
                        // TODO JOIN_GAME is no longer existing adapt to ... maybe SET_STATUS is suitable
                        case SET_STATUS: {
                            //Enables clients to join the players-list, if game is initialized but not running and
                            //maximum players-number of four is not reached
                            if (players.size() == MAX_PLAYERSIZE) {
                                logger.info("Client " + content + " can't join the Game, because maximum player number reached");
                                for (ClientWrapper client : connectedClients) {
                                    client.writer.writeObject(new Instruction(GAME_JOIN_FAIL2, content));
                                    client.writer.flush();
                                }
                            } else if (gameIsRunning) {
                                logger.info("Client " + content + " can't join an already running game");
                                for (ClientWrapper client : connectedClients) {
                                    client.writer.writeObject(new Instruction(GAME_JOIN_FAIL3, content));
                                    client.writer.flush();
                                }
                            } else {
                                logger.info(content + " joined the game");
                                //Get PlayerName
                                for (ClientWrapper client : connectedClients) {
                                    if(client.socket.equals(clientSocket)) {
                                        players.add(new Player(client.name, instruction.getAge()));
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                        case BYE: {
                            logger.info("Client " + content + " left the room");
                            //Send message to all clients:
                            for(ClientWrapper client : connectedClients) {
                                client.writer.writeObject(new Instruction(CLIENT_LEAVES, content)); //Typsicherheit durch Instruction
                                client.writer.flush();
                            }
                            writer.writeObject(new Instruction(KILL_CLIENT, "")); //Todo: Integrate in sayBye() -> saves instruction we do not actually need
                            writer.flush();

                            //Use stream to remove client from serverlist: (Maybe there is a more efficient way?)
                            connectedClients = connectedClients.stream().
                                    filter(clientWrapper -> !clientWrapper.socket.equals(clientSocket)).
                                    collect(Collectors.toCollection(ArrayList::new));
                            writer.close();
                            break;
                        }
                    }
                }
            } catch (SocketException exp) {
                if (exp.getMessage().contains("Socket closed"))
                    logger.info("Client at " + clientSocket.getInetAddress().getHostAddress() + " disconnected.");
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inner class to wrap the information from the client (socket + name)
     */
    private class ClientWrapper {
        Socket socket;
        String name;
        ObjectOutputStream writer;

        private ClientWrapper(Socket socket, String name, ObjectOutputStream writer) {
            this.socket = socket;
            this.name = name;
            this.writer = writer;
        }
    }
}
