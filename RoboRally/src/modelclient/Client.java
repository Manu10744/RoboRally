package modelclient;

import java.io.*;
import java.util.logging.Logger;

import modelserver.game.Player;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import utils.instructions.ServerInstruction;
import utils.json.JSONDecoder;
import utils.json.JSONEncoder;
import utils.json.protocol.*;

import java.net.Socket;

/**
 * This class implements the clients. <br>
 * All clients connect to one server.
 *
 * @author Ivan Dovecar
 */
public class Client {
    private String name;
    private int figure;
    private Socket socket;
    private String serverIP;
    private int serverPort;
    private PrintWriter writer;

    private boolean waitingForHelloClient;

    private String protocolVersion = "Version 0.1";
    private String group = "AstreineBarsche";

    private StringProperty chatHistory;
    private ListProperty<String> activeClients;
    private ListProperty<OtherPlayer> otherActivePlayers;
    private BooleanProperty gameReady;
    private static final Logger logger = Logger.getLogger( Client.class.getName() );

    //TODO     public Client(String name, String serverIP, int serverPort) {
    public Client(String serverIP, int serverPort) {
        logger.info("Starting registration process...");
        this.serverIP = serverIP;
        this.serverPort = serverPort;

        chatHistory = new SimpleStringProperty("");
        activeClients = new SimpleListProperty<>(FXCollections.observableArrayList());

        //GAME:
        otherActivePlayers = new SimpleListProperty<>(FXCollections.observableArrayList());
        gameReady = new SimpleBooleanProperty(false);
    }


    /**
     * This method is responsible for connecting the client to the specified server.
     * It uses the {link fieldServer} to get the IP and Port.
     * @return connection Success: True, connection Failed: False
     */
    public boolean connectClient() {
        try {
            //Create socket to connect to server at serverIP:serverPort
            socket = new Socket(serverIP, serverPort);

            //Start new Thread, that reads incoming messages from server
            ClientReaderTask readerTask = new ClientReaderTask(socket);
            Thread readerThread = new Thread(readerTask);
            readerThread.start();

            //waiting for server response - waitingForHelloClient is changed by ClientReaderTask
            waitingForHelloClient = true;
            while(waitingForHelloClient) {
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
    public void playerValue(String name) {
        //TODO until "chooseRobot" isn't finished PRESET ROBOT NO.1, when finished add "int figure" param to method
        logger.info("Submit player values (DUMMY FIGURE 1 PRESET ATM) and name " + name);
        JSONMessage jsonMessage = new JSONMessage("PlayerValues", new PlayerValuesBody(name, 1));
        writer.println(JSONEncoder.serializeJSON(jsonMessage));
        writer.flush();
    }

    /**
     * This method is responsible for sending ordinary messages to the server.
     * The server is going to process the messages based on whether it is
     * a private or ordinary message.
     * It uses the {@link @FXML chatInput} to get the message content.
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
     * @param message that is to be sent
     * @param receiverID of the Player who receives private message
     * @author Mia
     */
    public void sendPrivateMessage(String message, int receiverID)  {
           JSONMessage jsonMessage = new JSONMessage("SendChat", new SendChatBody(message, receiverID));

            writer.println(JSONEncoder.serializeJSON(jsonMessage));
            writer.flush();

    }

    //TODO This part has to be adapted to RoboRally needs - should handle the ChooseRobot part

    /**
     * This method is responsible for submitting player values to the server. It is triggered by
     * clicking on the chosen robot image during registration process which represents the player figure.
     * It submits the players' name along with the selected robot (player figure).
     *
     * @author Ivan Dovecar, Mia
     */
    public void join(String name, int robot) { //TODO check how player figure is submitted Sring Int aso
            JSONMessage jsonMessage = new JSONMessage("PlayerValues", new PlayerValuesBody(name, robot));

            writer.println(JSONEncoder.serializeJSON(jsonMessage));
            writer.flush();

    }

    /**
     * This method is responsible for setting the gamer status on ready or not ready.
     * It is triggered by clicking on the ready button below the chat.
     *
     * <b>Note:</b> A game starts automatically when all players (at least 2 players) are ready.
     *
     * @author Ivan Dovecar
     */
    public void ready() {

    }


    /**
     * Receive message from clients
     * @param message (contents clients name)
     */
    private void receiveMessage(String message) {
        String oldHistory = chatHistory.get();
        String newHistory = oldHistory + "\n" + message;
        chatHistory.setValue(newHistory);
    }


    private void addOtherPlayer(int playerID) {
        otherActivePlayers.add(new OtherPlayer(playerID));
    }

    /**
     * Adds an client to activeClients
     * @param playerID of joining client
     */
    private void addActiveClient(String playerID) {
        activeClients.add(playerID);
    }

    /**
     * Remove client from list
     * @param name Name of leaving client
     */
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



    public StringProperty chatHistoryProperty() { return chatHistory; }

    public ListProperty<OtherPlayer> otherActivePlayers() { return otherActivePlayers; }

    public String getName() { return name; }

    public BooleanProperty gameReadyProperty() { return gameReady; }

    public OtherPlayer getOtherPlayerByName(int playerID) {
        return (OtherPlayer) otherActivePlayers.stream();
    }

    public ListProperty<String> activeClientsProperty() { return activeClients; }


    /**
     * Inner class to define ReaderTask with Server
     */
    private class ClientReaderTask extends Thread {

        private Socket socket; //This Client's socket
        private int playerID;
        private String name;
        private int figure;

        ClientReaderTask(Socket socket) {
            this.socket = socket;
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
                    try {
                        // Deserialize the received JSON String into a JSON object
                        JSONMessage jsonMessage = JSONDecoder.deserializeJSON(jsonString.toString());
                        logger.info("JSONMessage: " + jsonMessage);

                        // Here we get the instruction from the received JSON Object
                        ServerInstruction serverInstruction = JSONDecoder.getServerInstructionByMessageType(jsonMessage);
                        logger.info("ServerInstruction: " + serverInstruction);

                        // Here we get the enum (ServerInstructionType) from the instruction above
                        ServerInstruction.ServerInstructionType serverInstructionType = serverInstruction.getServerInstructionType();
                        logger.info("ServerInstructionType: " + serverInstructionType);

                        switch (serverInstructionType) {

                            ////////////////////////////////////////////////
                            /*  This part handles S2C CHAT instructions   */
                            ////////////////////////////////////////////////

                            //Server sends protocol version to client
                            case HELLO_CLIENT: {
                                logger.info("CASE HELLO_CLIENT successfully entered");
                                HelloClientBody messageBody = (HelloClientBody) jsonMessage.getMessageBody();
                                logger.info("Server has protocol " + messageBody.getProtocol());
                                waitingForHelloClient = false;
                                break;
                            }

                            // Client gets a unique player ID from the server
                            case WELCOME: {
                                logger.info("CASE WELCOME successfully entered");
                                WelcomeBody messageBody = (WelcomeBody) jsonMessage.getMessageBody();
                                logger.info("PlayerID: " + messageBody.getPlayerID());

                                playerID = messageBody.getPlayerID();

                                break;
                            }

                            //Server distributes message to all
                            case RECEIVED_CHAT: {
                                ReceivedChatBody messageBody = (ReceivedChatBody) jsonMessage.getMessageBody();
                                Platform.runLater(() -> receiveMessage(messageBody.getMessage()));
                                break;
                            }

                            // Server distributes private message to the appropriate player
                            case RECEIVED_PRIVATE_CHAT: {
                                ReceivedChatBody messageBody = (ReceivedChatBody) jsonMessage.getMessageBody();
                                Platform.runLater(() -> {
                                    //TODO write code here (look up exisiting method: sendPrivateMessage)
                                });
                                break;
                            }

                            // Server informs client that a transmission error occurred
                            case ERROR: {
                                logger.info("CASE ERROR successfully entered");

                                ErrorBody messageBody = (ErrorBody) jsonMessage.getMessageBody();
                                String errorMessage = messageBody.getError();

                                Platform.runLater(() -> {
                                    if (errorMessage.equals("Error: name already exists")){
                                        logger.info(errorMessage);
                                        //TODO write code here for proper reaction
                                    }
                                    if (errorMessage.equals("Error: figure already exists")){
                                        logger.info(errorMessage);
                                        //TODO write code here for proper reaction
                                    }
                                });
                                break;
                            }
                                /* Commented because no corresponding protocol defined yet
                                // Server informs client that a game has already started


                               case PLAYER_STATUS_FAIL: {
                                    Instruction finalChatInstruction = chatInstruction;
                                    Platform.runLater(() -> {
                                        receiveMessage(content + finalChatInstruction.getAddendum(serverToClientInstructionType));
                                    });
                                    break;
                                }

                                // Server removes client from active clients and closes socket
                                case CLIENT_LEAVES: {
                                    Instruction finalChatInstruction = chatInstruction;
                                    Platform.runLater(() -> {
                                        receiveMessage(content + finalChatInstruction.getAddendum(serverToClientInstructionType));
                                        removeActiveClient(content);
                                    });
                                    writer.close();
                                    socket.close();
                                    Platform.exit();
                                    break;
                                }
                                */

                            ////////////////////////////////////////////////
                            /*  This part handles S2C game instructions   */
                            ////////////////////////////////////////////////

                            //Server confirms player_name and player_figure
                            case PLAYER_ADDED: {
                                PlayerAddedBody messageBody = (PlayerAddedBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    activeClients.add(messageBody.getPlayerID(), messageBody.getName());
                                    OtherPlayer newPlayer = new OtherPlayer(messageBody.getPlayerID());
                                    otherActivePlayers.add(otherActivePlayers.size(), newPlayer);
                                });
                                break;
                            }

                            //Server informs all other players of the status of the new player
                            case PLAYER_STATUS: {
                                PlayerStatusBody messageBody = (PlayerStatusBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO This part has to be edited to enable ongoing status changing
                                    // receiveMessage(messageBody.getMessage());
                                    gameReady.set(true);
                                });
                                break;
                            }

                            //Server sends maps to clients
                            case GAME_STARTED: {
                                // GameStartedBody messageBody = (GameStartedBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server notifies all other players of the played card
                            case CARD_PLAYED: {
                                CardPlayedBody messageBody = (CardPlayedBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs all of the player that is to move
                            case CURRENT_PLAYER: {
                                CurrentPlayerBody messageBody = (CurrentPlayerBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs all about the current game phase
                            case ACTIVE_PHASE: {
                                ActivePhaseBody messageBody = (ActivePhaseBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server confirms starting point and informs other players of it
                            case STARTING_POINT_TAKEN: {
                                StartingPointTakenBody messageBody = (StartingPointTakenBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs player of her or his hand
                            case YOUR_CARDS: {
                                YourCardsBody messageBody = (YourCardsBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs other players of the number of cards another has
                            case NOT_YOUR_CARD: {
                                NotYourCardsBody messageBody = (NotYourCardsBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //If not enough cards are on the draw pile, discarded pile has to be reshuffled
                            case SHUFFLE_CODING: {
                                ShuffleCodingBody messageBody = (ShuffleCodingBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server starts timer as soon as someone has a full register
                            case TIMER_STARTED: {
                                TimerStartedBody messageBody = (TimerStartedBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            // Server informs all clients that time for choosing programming cards has run out; player IDs of too slow players are saved
                            case TIMER_ENDED: {
                                TimerEndedBody messageBody = (TimerEndedBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server fills empty registers after timer ended
                            case CARDS_YOU_GOT_NOW: {
                                CardsYouGotNowBody messageBody = (CardsYouGotNowBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs all players of the cards in the current register
                            case CURRENT_CARDS: {
                                CurrentCardsBody messageBody = (CurrentCardsBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs other players of a move made (just moving, not turning)
                            case MOVEMENT: {
                                MovementBody messageBody = (MovementBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs all clients if a player turns (left, right)
                            case PLAYER_TURNING: {
                                PlayerTurningBody messageBody = (PlayerTurningBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //For animation purposes (?)
                            case PLAYER_SHOOTING: {
                                PlayerShootingBody messageBody = (PlayerShootingBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs player of damage suffered in round; the damage will be handed out in fixed bundles, no individual damage is given
                            case DRAW_DAMAGE: {
                                DrawDamageBody messageBody = (DrawDamageBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs all player if another one has to reboot
                            case REBOOT: {
                                RebootBody messageBody = (RebootBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs client of new energy level and its reason for changing
                            case ENERGY: {
                                EnergyBody messageBody = (EnergyBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs all players if a player reached a checkpoint
                            case CHECK_POINT_REACHED: {
                                CheckPointReachedBody messageBody = (CheckPointReachedBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }

                            //Server informs players if a player has won
                            case GAME_FINISHED: {
                                GameFinishedBody messageBody = (GameFinishedBody) jsonMessage.getMessageBody();

                                Platform.runLater(() -> {
                                    //TODO write code here
                                });
                                break;
                            }
                        }
                    } catch (Exception e) {
                        // No need to handle this exception because it is forced
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class OtherPlayer {
      //  StringProperty name;
        IntegerProperty playerID;

        OtherPlayer(int playerID) {
            this.playerID = new SimpleIntegerProperty(playerID);
        }

      //  public String getName() { return name.get();}
    }
}