package modelserver;

import javafx.application.Application;
import javafx.stage.Stage;
import modelserver.game.Game;
import modelserver.game.Player;
import utils.instructions.ClientInstruction;
import utils.instructions.Instruction;
import utils.instructions.ServerInstruction;
import utils.json.JSONDecoder;
import utils.json.JSONEncoder;
import utils.json.JSONMessage;
import utils.json.MessageBody;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static utils.Parameter.*;
import static utils.instructions.Instruction.ClientToServerInstructionType.*;
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
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                //READER:
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String jsonString;
                while ((jsonString = reader.readLine()) != null) {
                    // Deserialize the received JSON String into a JSON object
                    JSONMessage jsonMessage = JSONDecoder.deserializeJSON(jsonString);
                    String content = jsonMessage.getMessageBody().getMessage();

                    // Here we get the instruction from the received JSON Object
                    ClientInstruction clientInstruction = JSONDecoder.getClientInstructionByMessageType(jsonMessage);
                    // Here we get its instruction type (enum)
                    ClientInstruction.ClientInstructionType clientInstructionType = clientInstruction.getClientInstructionType();

                    switch (clientInstructionType) {
                        //Check if name is already used, if not, register client
                        /*
                        case CHECK_NAME: {
                    switch (clientToServerInstructionType) {


                        *//////////////////////////////////////////////
                        /*  This part handles C2S CHAT instructions  */
                        ///////////////////////////////////////////////

                        //Client sends group name, protocol-vs and KI-on/off to Server
                        case HELLO_SERVER: {
                            //TODO write code here
                        }

                        //Client sends public message to all, the value of "to" of the JSON-message must be -1
                        case SEND_CHAT: {
                            //Stream to get client's name (because atm only the socket is known)
                            String clientName = connectedClients.stream().
                                    filter(clientWrapper -> clientWrapper.socket.equals(clientSocket)).
                                    findFirst().get().name;
                            //Send message to all clients:
                            for (ClientWrapper client : connectedClients) {
                                MessageBody messageBody = new MessageBody();
                                // TODO: messageBody.setFrom() <- playerID of sending client!
                                messageBody.setMessage(content);
                                messageBody.setPrivate(false);
                                jsonMessage = new JSONMessage("ReceivedChat", messageBody);

                                client.writer.println(JSONEncoder.serializeJSON(jsonMessage));
                                client.writer.flush();
                            }
                            break;
                        }

                        //Clients sends private message to another player via the server
                        case SEND_PRIVATE_CHAT: {
                            //Stream to get client's name
                            String sendingClientName = connectedClients.stream().
                                    filter(clientWrapper -> clientWrapper.socket.equals(clientSocket)).
                                    findFirst().get().name;
                            for (ClientWrapper client : connectedClients){
                                if (jsonMessage.getMessageBody().getTo().equals(client.getPlayerID())) {

                                    MessageBody messageBody = new MessageBody();
                                    messageBody.setMessage(content);
                                    // TODO: messageBody.setFrom() <- playerID of sending client!
                                    messageBody.setPrivate(true);

                                    jsonMessage = new JSONMessage("ReceivedChat", messageBody);
                                    client.writer.println(JSONEncoder.serializeJSON(jsonMessage));
                                    client.writer.flush();
                                }
                                if (sendingClientName.equals(client.name)){
                                    client.writer.println(JSONEncoder.serializeJSON(jsonMessage) + ": @" + client.name + " (private) " + content);
                                    client.writer.flush();
                                }
                            }
                            break;
                        }


                            /* (!) NO PROTOCOL FOR THIS FLAG YET (!)
                             //Client leaves game and informs server thereof
                        case BYE: {
                            logger.info("Client " + content + " left the room");
                            //Send message to all clients:
                            for(ClientWrapper client : connectedClients) {
                                client.writer.println(new Instruction(CLIENT_LEAVES, content));
                                client.writer.flush();
                            }
                            writer.write(new Instruction(CLIENT_LEAVES, ""));
                            writer.flush();

                            //Use stream to remove client from serverlist: (Maybe there is a more efficient way?)
                            connectedClients = connectedClients.stream().
                                    filter(clientWrapper -> !clientWrapper.socket.equals(clientSocket)).
                                    collect(Collectors.toCollection(ArrayList::new));
                            writer.close();
                            break;
                        }
                             */

                        ///////////////////////////////////////////////
                        /*  This part handles C2S GAME instructions  */
                        ///////////////////////////////////////////////

                        //Client sends player-name and player figure to server, where availability is checked and if so player is registered
                        case PLAYER_VALUES: {
                            //TODO modify code according to RoboRally needs
                            boolean success = true;
                            for (ClientWrapper client : connectedClients) {
                                content = jsonMessage.getMessageBody().getName();
                                if (client.name.equals(content)) {
                                    logger.info("Client " + content + " refused (name already exists)");

                                    writer.println(String.valueOf(new ServerInstruction(ServerInstruction.ServerInstructionType.ERROR))); // Todo hier fehlen die ebcoder methoden
                                    writer.flush();
                                    success = false;
                                    break;
                                }
                            }

                             /* (!) NO PROTOCOL FOR CHECK NAME YET (!)
                            if (success) {
                                logger.info("Client " + content + " successfully registered");

                                //Add new Client to list connected clients
                                connectedClients.add(new ClientWrapper(clientSocket, content, writer));


                                // TODO check if the following three lines are necessary
                                writer.write(new Instruction(NAME_SUCCESS, ""));
                                writer.flush();

                                //Send message to all already active clients
                                for (ClientWrapper client : connectedClients) {
                                    if(!client.socket.equals(clientSocket)) {
                                        client.writer.write(new Instruction(CLIENT_JOINED, content));
                                        client.writer.flush();

                                    } else { //New Client receives different message:
                                        client.writer.write(new Instruction(CLIENT_WELCOME, content));
                                        client.writer.flush();
                                    }
                                }

                                //Register all current activeClients at new Client
                                for(ClientWrapper client : connectedClients) {
                                    if (!client.socket.equals(clientSocket)) {
                                        writer.write(new Instruction(CLIENT_REGISTER, client.name));
                                        writer.flush();
                                    }
                                }
                            }
                            break;

                              */

                        }

                        /* Ivan, this is yours! :-) Good luck!
                        case RECEIVED_PRIVATE_CHAT: {
                            //Stream to get client's name
                            String sendingClientName = connectedClients.stream().
                                    filter(clientWrapper -> clientWrapper.socket.equals(clientSocket)).
                                    findFirst().get().name;
                            for(ClientWrapper client : connectedClients){
                                if(instruction.getAddressedClient().equals(client.name)){
                                    client.writer.write(new Instruction(NEW_MESSAGE,
                                            sendingClientName + ": (private) " + content));
                                    client.writer.flush();
                                }
                                if(sendingClientName.equals(client.name)){
                                    client.writer.write(new Instruction(NEW_MESSAGE,
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

                            //Only if Server accepts clients after game has started
                            if (gameIsRunning) {
                                logger.info("Client " + content + " can't set status to ready, game is already running");
                                for (ClientWrapper client : connectedClients) {
                                    client.writer.write(new Instruction(GAME_JOIN_FAIL2, content));
                                    client.writer.flush();
                                }
                            } else if (gameIsRunning) {
                                logger.info("Client " + content + " can't join an already running game");
                                for (ClientWrapper client : connectedClients) {
                                    client.writer.write(new Instruction(GAME_JOIN_FAIL3, content));
                                    client.writer.flush();
                                }
                            } else {
                                logger.info(content + " joined the game");
                                //Get PlayerName
                                for (ClientWrapper client : connectedClients) {
                                    if(client.socket.equals(clientSocket)) {
                                        // TODO age is no longer needed, player figure has to be added instead
                                     //   players.add(new Player(client.name, client.age));
                                        break;
                                    }
                                }
                            }
                            break;
                        }

                         */


                        //Player plays a card
                        case PLAY_CARD: {
                            //TODO write code here
                        }

                        //Player chooses starting point and informs server of her or his choice
                        case SET_STARTING_POINT: {
                            //TODO write code here
                        }

                        /*Player selects cards, each selected card is sent to the server after five have been chosen.
                        If a register is emptied the card value is 'null'*/
                        case SELECT_CARDS: {
                            //TODO write code here
                        }

                        //Client informs client that a card has been put in the register
                        case CARDS_SELECTED: {
                            //TODO write code here
                        }

                        //Client informs server that a player has filled his or her full register
                        case SELECTION_FINISHED: {
                            //TODO write code here
                        }

                        // For animation purposes

                    }
                }
            } catch (SocketException exp) {
                if (exp.getMessage().contains("Socket closed"))
                    logger.info("Client at " + clientSocket.getInetAddress().getHostAddress() + " disconnected.");
            } catch (IOException e) {
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
        PrintWriter writer;
        int playerID;

        private ClientWrapper(Socket socket, String name, PrintWriter writer) {
            this.socket = socket;
            this.name = name;
            this.writer = writer;
        }

        public int getPlayerID(){
            return  this.playerID;
        }
    }
}
