package modelserver;

import javafx.application.Application;
import javafx.stage.Stage;
import modelserver.game.Card;
import modelserver.game.Game;
import modelserver.game.Maps.DizzyHighway;
import modelserver.game.Maps.Map;
import modelserver.game.Player;
import utils.Parameter;
import utils.instructions.ClientInstruction;
import utils.instructions.ServerInstruction;
import utils.json.JSONDecoder;
import utils.json.JSONEncoder;
import utils.json.protocol.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Logger;

import static utils.Parameter.*;

/**
 * This class implements the server. <br>
 * It will communicate with the clients.
 *
 * @author Ivan Dovecar
 */
public class Server extends Application {
    private ArrayList<ClientWrapper> connectedClients;
    private ArrayList<Player> players = new ArrayList<>();

    private String protocolVersion = "Version 0.1";
    private int counterPlayerID = 1;
    private int numberOfReadyClients = 0;
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
        JSONMessage jsonMessage;

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

                //Server submits protocol version to client
                jsonMessage = new JSONMessage("HelloClient", new HelloClientBody(protocolVersion));
                writer.println(JSONEncoder.serializeJSON(jsonMessage));
                writer.flush();


                String jsonString;
                while ((jsonString = reader.readLine()) != null) {
                    // Deserialize the received JSON String into a JSON object
                    jsonMessage = JSONDecoder.deserializeJSON(jsonString);
                    logger.info("JSONDecoder done: "+jsonString+jsonMessage);

                    // Here we get the instruction from the received JSON Object
                    ClientInstruction clientInstruction = JSONDecoder.getClientInstructionByMessageType(jsonMessage);
                    logger.info("clientInstruction: "+clientInstruction);

                    // Here we get its instruction type (enum)
                    ClientInstruction.ClientInstructionType clientInstructionType = clientInstruction.getClientInstructionType();
                    logger.info("clientInstructionType: "+clientInstructionType);

                    switch (clientInstructionType) {
                        //////////////////////////////////////////////
                        /*  This part handles C2S CHAT instructions  */
                        ///////////////////////////////////////////////

                        //Client sends group name, protocol-vs and KI-on/off to Server
                        case HELLO_SERVER: {
                            logger.info("CASE HELLO_SERVER successfully entered");
                            HelloServerBody messageBody = (HelloServerBody) jsonMessage.getMessageBody();

                            if (messageBody.getProtocol().equals(protocolVersion)){
                                logger.info("Protocol version test succeeded");

                                jsonMessage = new JSONMessage("Welcome", new WelcomeBody(counterPlayerID));
                                writer.println(JSONEncoder.serializeJSON(jsonMessage));
                                writer.flush();

                                //counter is adjusted for next registration process
                                counterPlayerID++;

                                break;
                            } else {
                                logger.info("Protocol version test failed");
                                clientSocket.close();
                                logger.info("Server connection terminated");
                                break;
                            }
                        }

                        //Client sends public message to all, the value of "to" of the JSON-message must be -1
                        case SEND_CHAT: {
                            logger.info("CASE SEND_CHAT successfully entered");
                            SendChatBody messageBody = (SendChatBody) jsonMessage.getMessageBody();

                            //Stream to get client's playerID (because atm only the socket is known)
                            int senderID = connectedClients.stream().
                                    filter(clientWrapper -> clientWrapper.socket.equals(clientSocket)).
                                    findFirst().get().playerID;

                            //Stream to get client's name (because atm only the socket is known)
                            String senderName = connectedClients.stream().
                                    filter(clientWrapper -> clientWrapper.socket.equals(clientSocket)).
                                    findFirst().get().name;

                            //Build new string from client's name and message content, to show name in chat
                            String messageContent = messageBody.getMessage();
                            String content = senderName + ": " + messageContent;

                            //Send message to all clients:
                            for (ClientWrapper client : connectedClients) {

                                //Send message to all clients:
                                jsonMessage = new JSONMessage("ReceivedChat", new ReceivedChatBody(content, senderID, false));
                                client.writer.println(JSONEncoder.serializeJSON(jsonMessage));
                                client.writer.flush();
                                logger.info("SEND_CHAT: Content of ReceivedChat: " + content + " " + senderID);
                            }
                            break;
                        }

                        //Clients sends private message to another player via the server
                        case SEND_PRIVATE_CHAT: {
                            logger.info("CASE SEND_PRIVATE_CHAT successfully entered");
                            SendChatBody messageBody = (SendChatBody) jsonMessage.getMessageBody();

                            //Stream to get client's name
                            String sendingClientName = connectedClients.stream().
                                    filter(clientWrapper -> clientWrapper.socket.equals(clientSocket)).
                                    findFirst().get().name;
                            int senderID = connectedClients.stream().filter(clientWrapper -> clientWrapper.socket.equals(clientSocket)).findFirst().get().playerID;

                            for (ClientWrapper client : connectedClients) {
                                String content = messageBody.getMessage();
                                if (messageBody.getTo() == client.getPlayerID()) {
                                    jsonMessage = new JSONMessage("ReceivedChat", new ReceivedChatBody(content, senderID, true));
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

                        ///////////////////////////////////////////////
                        /*  This part handles C2S GAME instructions  */
                        ///////////////////////////////////////////////

                        /**
                         * This case processes the by client submitted PLAYER_VALUES name and figure.
                         * It checks if name or figure are already in use by another client.
                         * If the player values are valid, client gets registered in connected clients.
                         * Every connected client receives the PlayerAdded message, additionally the new
                         * client receives PlayerAdded messages for all already connected clients.
                         *
                         * @author Ivan Dovecar
                         */
                        case PLAYER_VALUES: {
                            logger.info("CASE PLAYER_VALUES entered successfully");
                            PlayerValuesBody messageBody = (PlayerValuesBody) jsonMessage.getMessageBody();

                            String playerValueName = messageBody.getName();
                            int playerValueFigure = messageBody.getFigure();

                            boolean playerValueSuccess = true;

                            for (ClientWrapper client : connectedClients) {
                                // Checks if by PLAYER-VALUES received client' name is available
                                if (client.name.equals(playerValueName)) {
                                    logger.info("Client " + playerValueName + " refused (name already exists)");

                                    jsonMessage = new JSONMessage("Error", new ErrorBody("Error: name already exists"));
                                    writer.println(JSONEncoder.serializeJSON(jsonMessage));
                                    writer.flush();

                                    playerValueSuccess = false;
                                    break;
                                }

                                // Checks if by PLAYER-VALUES received client' figure is available
                                else if (client.figure == playerValueFigure) {
                                    logger.info("Client " + playerValueName + " refused (figure already exists)");

                                    jsonMessage = new JSONMessage("Error", new ErrorBody("Error: figure already exists"));
                                    writer.println(JSONEncoder.serializeJSON(jsonMessage));
                                    writer.flush();

                                    playerValueSuccess = false;
                                    break;
                                }
                            }

                            // If by PLAYER_VALUES received name and figure are valid...
                             if (playerValueSuccess) {
                                logger.info("Client " + playerValueName + " successfully registered");

                                //Add new Client to list connected clients
                                connectedClients.add(new ClientWrapper(clientSocket, playerValueName, writer, playerValueFigure, counterPlayerID, false));

                                 //Send message to all active clients
                                 jsonMessage = new JSONMessage("PlayerAdded", new PlayerAddedBody(counterPlayerID, playerValueName, playerValueFigure));
                                 writer.println(JSONEncoder.serializeJSON(jsonMessage));
                                 writer.flush();

                                //Inform new Client about all current activeClients (send all connected clients to new client without own entry)
                                for(ClientWrapper client : connectedClients) {
                                    if (!client.socket.equals(clientSocket)) {
                                        // TODO PlayerAdded message of every already active client is sent to new client
                                    }
                                }
                            }
                            break;
                        }


                        // Client signals the server that he's ready (ready = true) or revokes his ready statement (ready = false)
                        case SET_STATUS: {
                            logger.info("CASE SET_STATUS entered successfully");
                            SetStatusBody messageBody = (SetStatusBody) jsonMessage.getMessageBody();

                            boolean clientReady = messageBody.isReady();
                            logger.info("IS READY STATUS :" + clientReady);
                            int playerID = connectedClients.stream().filter(clientWrapper -> clientWrapper.socket.equals(clientSocket))
                                    .findFirst().get().playerID;

                            // Update the ClientWrapper due to the ready status change
                            connectedClients.stream().filter(clientWrapper -> clientWrapper.socket.equals(clientSocket)).findFirst().get().isReady = clientReady;

                            for (ClientWrapper client : connectedClients) {
                                logger.info("READY STATUS OF PLAYER " + client.name + ": " + client.isReady);
                                logger.info("PLAYER ID: " + client.playerID);

                                jsonMessage = new JSONMessage("PlayerStatus", new PlayerStatusBody(playerID, clientReady));
                                client.writer.println(JSONEncoder.serializeJSON(jsonMessage));
                                client.writer.flush();
                            }

                            // Increase number of ready clients when true
                            if (clientReady) {
                                ++numberOfReadyClients;
                            } else {
                                --numberOfReadyClients;
                            }

                            // If required number of players are ready, game starts and map is created
                            // TODO: Check case when 6 players connected and another one connects
                            if (numberOfReadyClients >= MIN_PLAYERSIZE && numberOfReadyClients == connectedClients.size()) {
                                Map map = new Map();

                                for (ClientWrapper client : connectedClients) {
                                    jsonMessage = new JSONMessage("GameStarted", new GameStartedBody(map));
                                    client.writer.println(JSONEncoder.serializeJSON(jsonMessage));
                                    client.writer.flush();
                                }

                            }
                            break;
                        }

                        //Player plays a card
                        case PLAY_CARD: {
                            logger.info("CASE PLAY_CARD entered successfully");
                            PlayCardBody messageBody = (PlayCardBody) jsonMessage.getMessageBody();

                            //TODO write code here
                            break;
                        }

                        //Player chooses starting point and informs server of her or his choice
                        case SET_STARTING_POINT: {
                            logger.info("CASE SET_STARTING_POINT entered successfully");
                            SetStartingPointBody messageBody = (SetStartingPointBody) jsonMessage.getMessageBody();

                            //TODO write code here
                            break;
                        }

                        /*Player selects cards, each selected card is sent to the server after five have been chosen.
                        If a register is emptied the card value is 'null'*/
                        case SELECT_CARD: {
                            logger.info("CASE SELECT_CARD entered successfully");
                            SelectCardBody messageBody = (SelectCardBody) jsonMessage.getMessageBody();

                            //TODO write code here
                            break;
                        }

                        //Client informs client that a card has been put in the register
                        case CARD_SELECTED: {
                            logger.info("CASE CARD_SELECTED entered successfully");
                            CardSelectedBody messageBody = (CardSelectedBody) jsonMessage.getMessageBody();

                            //TODO write code here
                            break;
                        }

                        //Client informs server that a player has filled his or her full register
                        case SELECTION_FINISHED: {
                            logger.info("CASE SELECTION_FINISHED entered successfully");
                            SelectionFinishedBody messageBody = (SelectionFinishedBody) jsonMessage.getMessageBody();

                            //TODO write code here
                            break;
                        }

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
class ClientWrapper {
    private Socket socket;
    private String name;
    private PrintWriter writer;
    private int playerID;
    private int figure;
    private boolean isReady;

        private ClientWrapper(Socket socket, String name, PrintWriter writer, int figure, int playerID, boolean isReady) {
            this.socket = socket;
            this.name = name;
            this.writer = writer;
            this.figure = figure;
            this.playerID = playerID;
            this.isReady = isReady;
        }

        public Socket getClientSocket() {
            return socket;
        }

        public String getName() {
            return name;
        }

        public PrintWriter getWriter() {
            return writer;
        }

        public int getFigure() {
            return figure;
        }

        public int getPlayerID(){
            return  this.playerID;
        }

        public boolean isReady() { return isReady; }
    }
}

