package client;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import server.game.Player;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import server.game.Robot;
import server.game.decks.DeckDiscard;
import server.game.decks.DeckDraw;
import server.game.decks.DeckHand;
import server.game.decks.DeckRegister;
import utils.json.JSONDecoder;
import utils.json.JSONEncoder;
import utils.json.MessageDistributer;
import utils.json.protocol.*;
import viewmodels.*;

import java.net.Socket;

import static utils.Parameter.ORIENTATION_RIGHT;

/**
 * This class implements the clients. <br>
 * All clients connect to one server.
 *
 * @author Ivan Dovecar
 */
public class Client {

    private MessageDistributer messageDistributer = new MessageDistributer();
    private String name;
    private String serverIP;
    private String protocolVersion = "Version 0.1";
    private String group = "AstreineBarsche";

    private PlayerWrapper ownPlayer;
    private ArrayList<PlayerWrapper> otherPlayers;

    private int figure;
    private int serverPort;

    private boolean waitingForHelloClient;
    private Socket socket;
    private PrintWriter writer;

    private StringProperty chatHistory;
    private ListProperty<String> activeClients;
    private ListProperty<OtherPlayer> otherActivePlayers;
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    private StageController stageController;
    private ChatController chatController;
    private MapController mapController;
    private PlayerMatController playerMatController;
    private OpponentMatController opponentMatController;
    private ChooseRobotController chooseRobotController;

    public MessageDistributer getMessageDistributer() {
        return messageDistributer;
    }

    public void setMessageDistributer(MessageDistributer messageDistributer) {
        this.messageDistributer = messageDistributer;
    }

    public StageController getStageController() {
        return stageController;
    }

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    public ChatController getChatController() {
        return chatController;
    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    public MapController getMapController() {
        return mapController;
    }

    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }

    public PlayerMatController getPlayerMatController() {
        return playerMatController;
    }

    public void setPlayerMatController(PlayerMatController playerMatController) {
        this.playerMatController = playerMatController;
    }

    public OpponentMatController getOpponentMatController() {
        return opponentMatController;
    }

    public void setOpponentMatController(OpponentMatController opponentMatController) {
        this.opponentMatController = opponentMatController;
    }

    public ChooseRobotController getChooseRobotController() {
        return chooseRobotController;
    }

    public void setChooseRobotController(ChooseRobotController chooseRobotController) {
        this.chooseRobotController = chooseRobotController;
    }

    //TODO  public Client(String name, String serverIP, int serverPort) {
    public Client(String serverIP, int serverPort) {
        logger.info("Starting registration process...");
        this.serverIP = serverIP;
        this.serverPort = serverPort;

        chatHistory = new SimpleStringProperty("");
        activeClients = new SimpleListProperty<>(FXCollections.observableArrayList());

        //GAME:
        otherActivePlayers = new SimpleListProperty<>(FXCollections.observableArrayList());
    }


    /**
     * This method is responsible for connecting the client to the specified server.
     * It uses the {@link @FXML fieldServer} to get the IP and Port.
     *
     * @return <code>true</code> if connection could be established.
     */
    public boolean connectClient() {
        try {
            //Create socket to connect to server at serverIP:serverPort
            socket = new Socket(serverIP, serverPort);

            //Start new Thread, that reads incoming messages from server
            ClientReaderTask readerTask = new ClientReaderTask(this, socket);
            Thread readerThread = new Thread(readerTask);
            readerThread.start();

            //waiting for server response - waitingForHelloClient is changed by ClientReaderTask
            waitingForHelloClient = true;
            while (waitingForHelloClient) {
                logger.info("Waiting...");
                if (waitingForHelloClient) try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Inform the server about group, AI and the clients protocol version
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            JSONMessage jsonMessage = new JSONMessage("HelloServer", new HelloServerBody(group, false, protocolVersion));
            writer.println(JSONEncoder.serializeJSON(jsonMessage));
            writer.flush();

        } catch(IOException exp) {
            exp.printStackTrace();
        }
        return false;
    }

    /**
     * This method is responsible for sending playerValues to the server.
     * The server is going to process the values and if valid, it will return a playerAdded message.
     * It uses the {@link @FXML fieldName} to get the name and chooseRobot FXML to get figure.
     *
     * @author Ivan
     */
    public void sendPlayerValues(String name, int figure) {
        logger.info("Submitting player values");

        // Here the server gets the name and figure of a newly initialised playerServer
        ownPlayer = new PlayerWrapper();
        ownPlayer.setName(name);
        ownPlayer.initRobotByFigure(figure);

        JSONMessage jsonMessage = new JSONMessage("PlayerValues", new PlayerValuesBody(name, figure));
        writer.println(JSONEncoder.serializeJSON(jsonMessage));
        writer.flush();
        logger.info("Submitted player values");
    }

    public void sendStartingPoint(String id){
        logger.info("Submitting startinpoint coordinates");

        int x = Character.getNumericValue(id.charAt(0));
        int y = Character.getNumericValue(id.charAt(2));

        JSONMessage jsonMessage = new JSONMessage("SetStartingPoint", new SetStartingPointBody(x,y));
        writer.println(JSONEncoder.serializeJSON(jsonMessage));
        writer.flush();
        logger.info("Submitted StartingPoint coordinates");

    }

    /**
     * This method is responsible for sending ordinary messages to the server.
     * The server is going to process the messages based on whether it is
     * a private or ordinary message.
     * It uses the {@link @FXML chatInput} to get the message content.
     *
     * @param message The message that should be sent.
     * @author Mia
     */
    public void sendMessage(String message) {
        JSONMessage jsonMessage = new JSONMessage("SendChat", new SendChatBody(message, -1));

        writer.println(JSONEncoder.serializeJSON(jsonMessage));
        writer.flush();

    }


    /**
     * This method is responsible for sending private messages to the server.
     * The server is going to process the messages based on whether it is
     * a private or ordinary message.
     * It uses the {@link @FXML chatInput} to get the message content.
     *
     * @param message The message that should be sent.
     * @param receiverID The playerID of the Player who should receive the private message.
     * @author Mia
     */
    public void sendPrivateMessage(String message, int receiverID) {
        JSONMessage jsonMessage = new JSONMessage("SendChat", new SendChatBody(message, receiverID));

        writer.println(JSONEncoder.serializeJSON(jsonMessage));
        writer.flush();
    }


    /**
     * This method is responsible for sending the players ready status to the server.
     *
     * <b>Note:</b> A game starts automatically when all players (at least 2 players) are ready.
     *
     * @author Ivan Dovecar
     */
    public void sendReadyStatus(boolean readyStatus) {
        // Inform the server about changed ready status
        JSONMessage jsonMessage = new JSONMessage("SetStatus", new SetStatusBody(readyStatus));
        writer.println(JSONEncoder.serializeJSON(jsonMessage));
        writer.flush();
    }


    /**
     * This method is responsible for printing a message to the chat history.
     *
     * @param message The message that should be printed.
     */
    public void receiveMessage(String message) {
        String oldHistory = chatHistory.get();
        String newHistory = oldHistory + "\n" + message;
        chatHistory.setValue(newHistory);
    }


    //Todo check if needed
    private void addOtherPlayer(int playerID) {
        otherActivePlayers.add(new OtherPlayer(playerID));
    }

    /**
     * Adds an client to activeClients
     * @param playerID of joining client
     */
    //Todo check if needed
    private void addActiveClient(String playerID) {
        activeClients.add(playerID);
    }

    /**
     * Remove client from list
     * @param name Name of leaving client
     */
    //Todo check if needed
    private void removeActiveClient(Player name) {
        activeClients.remove(name);
/*
        for(int i = 0; i < otherActivePlayers.size(); i++) {
            if(otherActivePlayers.get(i).getName().equals(name)) {
                otherActivePlayers.remove(name);
                break;
            }
        }
 */
    }

    public String getName() { return name; }

    public StringProperty getChatHistoryProperty() { return chatHistory; }

    public ListProperty<OtherPlayer> getOtherActivePlayers() { return otherActivePlayers; }

    public OtherPlayer getOtherPlayerByName(int playerID) {
        return (OtherPlayer) otherActivePlayers.stream();
    }

    public ListProperty<String> getActiveClientsProperty() { return activeClients; }

    public boolean isWaitingForHelloClient() {
        return waitingForHelloClient;
    }

    public void setWaitingForHelloClient(boolean isWaiting) {
        this.waitingForHelloClient = isWaiting;
    }

    public int getFigure(){return figure;}

    public void setFigure(int figure){this.figure = figure;}

    /**
     * Inner class to define ReaderTask with Server
     */
    public class ClientReaderTask extends Thread {

        private Socket socket; //This Client's socket
        private int playerID;
        private String name;
        private int figure;
        private Client client;

        public ClientReaderTask(Client client, Socket socket) {
            this.client = client;
            this.socket = socket;
        }

        public void setPlayerID(int ID) {
            this.playerID = ID;
        }

        /**
         * The String input of the BufferedReader is in JSON-Format. In order to get the content of it we format it (deserialize it) into a JSONMessage.
         * The type of the JSONMessage is equal to the instruction we are getting from the server (serverInstruction). Its ServerInstructionType then is used to differentiate
         * between different  cases like we would normally do with instructions themselves.
         * The body of the message then contains various content. For more info check out the attributes of the
         *
         * @link MessageBody class.
         * @author Ivan, Manu, Mia
         */
        @Override
        public void run() {
            try {
                //Reads input stream from server
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String jsonString;
                while ((jsonString = reader.readLine()) != null) {
                    // Deserialize the received JSON String into a JSON object
                    JSONMessage jsonMessage = JSONDecoder.deserializeJSON(jsonString);
                    logger.info("JSONDecoder in Client done: " + jsonString + jsonMessage);

                    // Cast messagebody dynamically by reflection
                    Class<?> reflection = (Class<?>) Class.forName("utils.json.protocol." + jsonMessage.getMessageType() + "Body");
                    Object messageBodyObject = reflection.cast(jsonMessage.getMessageBody());

                    ServerMessageAction msg = (ServerMessageAction) jsonMessage.getMessageBody();
                    msg.triggerAction(client, this, messageBodyObject, messageDistributer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public PlayerWrapper getOwnPlayer() {
        return ownPlayer;
    }

    public void setOwnPlayer(PlayerWrapper ownPlayer) {
        this.ownPlayer = ownPlayer;
    }

    public ArrayList<PlayerWrapper> getOtherPlayers() {
        return otherPlayers;
    }

    public void setOtherPlayers(ArrayList<PlayerWrapper> otherPlayers) {
        this.otherPlayers = otherPlayers;
    }

    public class OtherPlayer {
        //  StringProperty name;
        IntegerProperty playerID;

        public OtherPlayer(int playerID) {
            this.playerID = new SimpleIntegerProperty(playerID);
        }

        //  public String getName() { return name.get();}
    }

    public class PlayerWrapper{

        private String name;
        private int energy;
        private int playerID;
        private int figure;
        private boolean isReady;

        private Robot playerRobot;

        private DeckDraw deckDraw;
        private DeckDiscard deckDiscard;
        private DeckHand deckHand;
        private DeckRegister deckRegister;


        public PlayerWrapper(){
            this.deckDraw = new DeckDraw();
            deckDraw.initializeDeckDraw();

            this.deckDiscard = new DeckDiscard();
            this.deckHand = new DeckHand();
            this.deckRegister = new DeckRegister();
        }


        public int getFigure() {
            return figure;
        }

        //Initialises Robot through setting the figure
        public void initRobotByFigure(int figure){
            this.figure = figure;

            Image robotImage;

            if (figure == 1) {
                robotImage= new Image("/resources/images/robots/HammerBot.PNG");
            } else if (figure == 2) {
                robotImage = new Image("/resources/images/robots/HulkX90.PNG");
            } else if (figure == 3) {
                robotImage = new Image("/resources/images/robots/SmashBot.PNG");
            } else if (figure == 4) {
                robotImage = new Image("/resources/images/robots/Twonky.PNG");
            } else if (figure == 5) {
                robotImage = new Image("/resources/images/robots/Spinbot.PNG");
            } else {
                robotImage = new Image("/resources/images/robots/ZoomBot.PNG");
            }

            this.playerRobot = new Robot(robotImage, ORIENTATION_RIGHT, 0, 0);

        }

        public int getPlayerID(){
            return playerID;
        }

        public void setPlayerID(int playerID) {
            this.playerID = playerID;
        }

        public boolean isReady() { return isReady; }

        public void setReady(boolean isReady) {
            this.isReady = isReady;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Robot getPlayerRobot() {
            return playerRobot;
        }

        public void setPlayerRobot(Robot playerRobot) {
            this.playerRobot = playerRobot;
        }
    }

}