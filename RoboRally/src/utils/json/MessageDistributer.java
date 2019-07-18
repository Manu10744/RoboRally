package utils.json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;
import static utils.Parameter.*;

import client.Client;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import server.Server;
import server.game.Card;
import server.game.Player;
import server.game.Robot;
import server.game.Tiles.Antenna;
import server.game.Tiles.Tile;
import utils.Countdown;
import utils.Parameter;
import utils.json.protocol.*;
import viewmodels.ChooseRobotController;
import viewmodels.IController;
import viewmodels.MapController;
import viewmodels.PlayerMatController;


/**
 * This class has the sole purpose to distribute the logic for each {@link JSONMessage} into seperate functions
 * that are called when the corresponding message was deserialized.
 *
 * @author Manuel Neumayer
 * @author Vincent Tafferner
 */
public class MessageDistributer {
    public static Map<String, IController> controllerMap;

    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger(MessageDistributer.class.getName());


    /**
     * This method is used to get all the controllers that are saved in the stage. With those, the individual gui-elements, e.g. chat and map can be referenced
     *
     * @param stageControllerMap
     */
    public void setControllerMap(Map stageControllerMap) {
        controllerMap = stageControllerMap;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                           HANDLERS FOR CLIENT MESSAGES                                        //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method contains the logic that comes into action when a 'HelloServer' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server          The Server itself.
     * @param task            The ReaderTask of the distributerServer (Gives access to the PrintWriter).
     * @param helloServerBody The message body of the message which is of type {@link HelloServerBody}.
     * @author Ivan Dovecar
     * @author Manu
     * @author Mia
     */
    public void handleHelloServer(Server server, Server.ServerReaderTask task, HelloServerBody helloServerBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleHelloServer()" + ANSI_RESET);

        try {
            if (helloServerBody.getProtocol().equals(server.getProtocolVersion())) {
                logger.info("Protocol version test succeeded");

                // First, assign the client a playerID
                JSONMessage welcomeMessage = new JSONMessage("Welcome", new WelcomeBody(server.getCounterPlayerID()));
                task.getWriter().println(JSONEncoder.serializeJSON(welcomeMessage));
                task.getWriter().flush();

                // Inform about already connected clients, also to disable already assigned robots before loading the chooseRobot view
                for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                    int playerID = clientWrapper.getPlayerID();
                    String name = clientWrapper.getName();
                    int figure = clientWrapper.getFigure();

                    JSONMessage informerMessage = new JSONMessage("PlayerAdded", new PlayerAddedBody(playerID, name, figure));
                    task.getWriter().println(JSONEncoder.serializeJSON(informerMessage));
                    task.getWriter().flush();
                }

                // TODO: REPLACE THIS WITH AN EVENT INFORMED_ABOUT_ALREADY_CONNECTED_PLAYERS
                sleep(300);

                // Inform every freshly connected client about ready status of already connected players
                for (Server.ClientWrapper client : server.getConnectedClients()) {
                    int playerID = client.getPlayer().getPlayerID();
                    boolean isReady = client.getPlayer().isReady();

                    JSONMessage readyMessage = new JSONMessage("PlayerStatus", new PlayerStatusBody(playerID, isReady));
                    task.getWriter().println(JSONEncoder.serializeJSON(readyMessage));
                    task.getWriter().flush();
                }

                // TODO: MAYBE CREATE AN EVENT INFORMED_ABOUT_READYSTATUS_OF_ALREADY_CONNECTED_CLIENTS

                // Server creates his player instance
                Player newPlayer = new Player();
                newPlayer.setPlayerID(server.getCounterPlayerID());
                server.getPlayers().add(newPlayer);

                // Create a ClientWrapper containing the player object in order to keep track and update later on
                Server.ClientWrapper newClientWrapper = server.new ClientWrapper();
                newClientWrapper.setPlayer(newPlayer);
                newClientWrapper.setClientSocket(task.getClientSocket());
                server.getConnectedClients().add(newClientWrapper);

                logger.info("SERVER CREATED HIS PLAYER WITH PLAYER ID: " + newPlayer.getPlayerID());


                // Save the playerID before incrementing the counter so the proper ID is given to the ClientWrapper
                server.setSetterPlayerID(server.getCounterPlayerID());

                // Counter is adjusted for next registration process
                server.setCounterPlayerID(server.getCounterPlayerID() + 1);
            } else {
                logger.info("Protocol version test failed");
                task.getClientSocket().close();
                logger.info("Server connection terminated");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method contains the logic that comes into action when a 'PlayerValues' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server           The Server itself.
     * @param task             The ReaderTask of the distributerServer (Gives access to the PrintWriter).
     * @param playerValuesBody The message body of the message which is of type {@link PlayerValuesBody}.
     * @author Ivan Dovecar
     * @author Manu
     */
    public static void handlePlayerValues(Server server, Server.ServerReaderTask task, PlayerValuesBody playerValuesBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handlePlayerValues()" + ANSI_RESET);

        String playerValueName = playerValuesBody.getName();
        int playerValueFigure = playerValuesBody.getFigure();

        boolean playerValueSuccess = true;

        for (Player player : server.getPlayers()) {
            // Checks if by PLAYER-VALUES received client' name is available
            if (player.getName().equals(playerValueName)) {
                logger.info("Player " + playerValueName + " refused (name already exists)");

                JSONMessage jsonMessage = new JSONMessage("Error", new ErrorBody("Error: name already exists"));
                task.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                task.getWriter().flush();

                playerValueSuccess = false;
                break;
            }

            // TODO: CHECKING FIGURE PROBABLY MADE REDUNDANT BY DISABLING ALREADY ASSIGNED ROBOTS
            // Checks if by PLAYER-VALUES received client' figure is available
            else if (player.getFigure() == playerValueFigure) {
                logger.info("Player " + playerValueName + " refused (figure already exists)");

                JSONMessage jsonMessage = new JSONMessage("Error", new ErrorBody("Error: figure already exists"));
                task.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                task.getWriter().flush();

                playerValueSuccess = false;
                break;
            }
        }

        // If by PLAYER_VALUES received name and figure are valid...
        if (playerValueSuccess) {
            logger.info(playerValueName + " successfully registered");

            // Update the ClientWrapper
            for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                if (clientWrapper.getClientSocket().equals(task.getClientSocket())) {
                    clientWrapper.setName(playerValueName);
                    clientWrapper.setWriter(task.getWriter());
                    clientWrapper.setFigure(playerValueFigure);
                    clientWrapper.setPlayerID(server.getSetterPlayerID());
                    clientWrapper.setReady(false);
                }
            }

            // Server needs to update his player instance
            Player playerToUpdate;
            for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                if (clientWrapper.getClientSocket().equals(task.getClientSocket())) {
                    playerToUpdate = clientWrapper.getPlayer();
                    playerToUpdate.setName(playerValueName);
                    playerToUpdate.setFigure(playerValueFigure);
                    playerToUpdate.initRobotByFigure(playerValueFigure);

                    logger.info("SERVER UPDATED HIS PLAYER WITH PLAYERID " + clientWrapper.getPlayerID()
                            + ". UPDATES: FIGURE: " + playerValueFigure + ", ROBOT: " + playerToUpdate.getPlayerRobot() + ", NAME: " + playerValueName);
                }
            }

            // Inform every connected client about new added player
            for (Server.ClientWrapper client : server.getConnectedClients()) {
                JSONMessage jsonMessage = new JSONMessage("PlayerAdded", new PlayerAddedBody(server.getSetterPlayerID(), playerValueName, playerValueFigure));
                client.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                client.getWriter().flush();
            }

            // Only for testing reasons to see if server keeps track of all players and updates them
            // TODO: Remove when not needed anymore
            for (Player player : server.getPlayers()) {
                System.out.println("PLAYERID: " + player.getPlayerID());
                System.out.println("NAME: " + player.getName());
                System.out.println("FIGURE: " + player.getFigure());
                System.out.println("ROBOT: " + player.getPlayerRobot());
            }

            // Inform new client with private chat message about all current active clients(without own entry)
            for (Server.ClientWrapper client : server.getConnectedClients()) {
                if (playerValueName.equals(client.getName())) {
                    for (Server.ClientWrapper otherClient : server.getConnectedClients()) {
                        if (!playerValueName.equals(otherClient.getName())) {
                            String content = "Active playerServer " + otherClient.getName() + " has ID " + otherClient.getPlayerID()
                                    + " and figure " + otherClient.getFigure() + ". \n"
                                    + otherClient.getName() + " is ready to play: " + otherClient.isReady() + "!\n";
                            JSONMessage jsonMessageAlreadyConnectedPlayers = new JSONMessage("ReceivedChat",
                                    new ReceivedChatBody(content, otherClient.getPlayerID(), true));
                            client.getWriter().println(JSONEncoder.serializeJSON(jsonMessageAlreadyConnectedPlayers));
                            client.getWriter().flush();
                        }
                    }
                }
            }
        }
    }

    /**
     * This method contains the logic that comes into action when a 'SetStatus' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server        The Server itself.
     * @param task          The ReaderTask of the distributerServer (Gives access to the PrintWriter).
     * @param setStatusBody The message body of the message which is of type {@link SetStatusBody}.
     */
    public void handleSetStatus(Server server, Server.ServerReaderTask task, SetStatusBody setStatusBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleSetStatus()" + ANSI_RESET);


        boolean clientReady = setStatusBody.isReady();
        int playerID = server.getConnectedClients().stream().filter(clientWrapper -> clientWrapper.getClientSocket().equals(task.getClientSocket()))
                .findFirst().get().getPlayerID();

        // TODO: probably not needed anymore
        // Update the ClientWrapper due to the ready status change
        server.getConnectedClients().stream().filter(clientWrapper -> clientWrapper.getClientSocket().equals(task.getClientSocket())).findFirst().get().setReady(clientReady);

        // Update the server's corresponding player instance
        Player playerToUpdate;
        for (Server.ClientWrapper client : server.getConnectedClients()) {
            if (client.getClientSocket().equals(task.getClientSocket())) {
                playerToUpdate = client.getPlayer();
                playerToUpdate.setReady(clientReady);

                logger.info("SERVER UPDATED HIS PLAYER WITH PLAYERID " + client.getPlayerID()
                        + ". UPDATES: " + "READY STATUS: " + clientReady);
            }
        }

        // Inform every connected client about changed ready status
        for (Server.ClientWrapper client : server.getConnectedClients()) {
            JSONMessage jsonMessage = new JSONMessage("PlayerStatus", new PlayerStatusBody(playerID, clientReady));
            client.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
            client.getWriter().flush();
        }

        // Increase number of ready clients when true, else decrease
        int numberOfReadyClients = server.getNumberOfReadyClients();
        if (clientReady) {
            server.setNumberOfReadyClients(++numberOfReadyClients);
            System.out.println(ANSI_CYAN + "Ready clients: " + server.getNumberOfReadyClients() + ANSI_RESET);
        } else {
            server.setNumberOfReadyClients(--numberOfReadyClients);
            System.out.println(ANSI_CYAN + "Ready clients: " + server.getNumberOfReadyClients() + ANSI_RESET);
        }

        // If required number of players are ready, game starts and map is created
        // TODO: Check case when 6 players connected and another one connects
        if (numberOfReadyClients >= Parameter.MIN_PLAYERSIZE && numberOfReadyClients == server.getConnectedClients().size()) {
            Path path = Paths.get("RoboRally/src/resources/maps/deathTrap.json");
            try {
                // Sets Map in server
                String map = Files.readString(path, StandardCharsets.UTF_8);
                JSONMessage jsonMessage = JSONDecoder.deserializeJSON(map);
                GameStartedBody gameStartedBody = ((GameStartedBody) jsonMessage.getMessageBody());
                server.setMap(gameStartedBody.getXArray());

                // Set Antenna in server
                for (int xPos = 0; xPos < server.getMap().size(); xPos++) {
                    for (int yPos = 0; yPos < server.getMap().get(yPos).size(); yPos++) {
                        for (Tile tile : server.getMap().get(xPos).get(yPos)) {
                            if (tile instanceof Antenna) {
                                server.setAntenna(tile);
                                server.setAntennaXPos(xPos);
                                server.setAntennaYPos(yPos);
                                logger.info(ANSI_GREEN + "ANTENNA IN SERVER HAS BEEN SET! COORDINATES: " + "( " + xPos + " | " + yPos + " )" + ANSI_RESET);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (Server.ClientWrapper client : server.getConnectedClients()) {
                try {
                    String map = Files.readString(path, StandardCharsets.UTF_8);
                    client.getWriter().println(map);
                    client.getWriter().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Set Construction phase
            for (Server.ClientWrapper client : server.getConnectedClients()) {
                JSONMessage jsonMessage = new JSONMessage("ActivePhase", new ActivePhaseBody(BUILD_UP_PHASE));
                client.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                client.getWriter().flush();
            }
        }
    }


    /**
     * This method contains the logic that comes into action when a 'SendChat' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server       The Server itself.
     * @param task         The ReaderTask of the distributerServer (Gives access to the PrintWriter).
     * @param sendChatBody The message body of the message which is of type {@link SendChatBody}.
     * @author Ivan Dovecar
     * @author Manu
     */
    public void handleSendChat(Server server, Server.ServerReaderTask task, SendChatBody sendChatBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleSendChat()" + ANSI_RESET);

        // Stream to get client's playerID (because atm only the socket is known)
        int senderID = server.getConnectedClients().stream().
                filter(clientWrapper -> clientWrapper.getClientSocket().equals(task.getClientSocket())).
                findFirst().get().getPlayerID();

        // Stream to get client's name (because atm only the socket is known)
        String senderName = server.getConnectedClients().stream().
                filter(clientWrapper -> clientWrapper.getClientSocket().equals(task.getClientSocket())).
                findFirst().get().getName();

        // Build new string from client's name and message content, to show name in chat
        String messageContent = sendChatBody.getMessage();
        String content = senderName + " (@" + senderID + ") : " + messageContent;

        int to = sendChatBody.getTo();

        if (to == -1) {
            // Send message to all clients:
            for (Server.ClientWrapper client : server.getConnectedClients()) {
                JSONMessage jsonMessage = new JSONMessage("ReceivedChat", new ReceivedChatBody(content, senderID, false));
                client.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                client.getWriter().flush();
                logger.info("SEND_CHAT: Content of ReceivedChat: " + content + " " + senderID);
            }
        } else {
            // Send private message to client:
            for (Server.ClientWrapper client : server.getConnectedClients()) {
                if (client.getPlayerID() == to) {
                    JSONMessage jsonMessage = new JSONMessage("ReceivedChat", new ReceivedChatBody(content, senderID, true));
                    client.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    client.getWriter().flush();
                    logger.info("SEND_PRIVATE_CHAT: Content of ReceivedChat: " + content + " " + senderID);
                }
            }
        }
    }

    /**
     * This method contains the logic that comes into action when a 'PlayCard' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server       The Server itself.
     * @param task         The ReaderTask of the distributerServer (Gives access to the PrintWriter).
     * @param playCardBody The message body of the message which is of type {@link PlayCardBody}.
     */
    public void handlePlayCard(Server server, Server.ServerReaderTask task, PlayCardBody playCardBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handlePlayCard()" + ANSI_RESET);

        Card playedCard = playCardBody.getCard();

        for (Server.ClientWrapper client : server.getConnectedClients()) {
            if (client.getClientSocket().equals(task.getClientSocket())) {
                //Todo delete playerID of Client
                Player player = client.getPlayer();
                int playerID = player.getPlayerID();

                //update the player of the server
                playedCard.activateCard(player);
                logger.info(ANSI_GREEN + "SERVER UPDATING FINISHED" + ANSI_RESET);

                //Sends played card to all clients with id of the one playing it
                for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedBody(playerID, playedCard));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }
            }
        }
    }

    /**
     * This method contains the logic that comes into action when a 'SetStartingPoint' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server               The Server itself.
     * @param task                 The ReaderTask of the distributerServer (Gives access to the PrintWriter).
     * @param setStartingPointBody The message body of the message which is of type {@link PlayerValuesBody}.
     */
    public void handleSetStartingPoint(Server server, Server.ServerReaderTask task, SetStartingPointBody setStartingPointBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleSetStartingPoint()" + ANSI_RESET);

        int x = setStartingPointBody.getX();
        int y = setStartingPointBody.getY();

        String desiredStartPoint = x + "-" + y;
        Player player = server.getConnectedClients().stream().filter(clientWrapper -> clientWrapper.getClientSocket()
                .equals(task.getClientSocket())).findFirst().get().getPlayer();

        logger.info(ANSI_GREEN + " ( HANDLESETSTARTINGPOINT ): PLAYER WITH ID " + player.getPlayerID() + " WANTS TO SET STARTPOINT ON COORDINATES: ( " + x + " | " + y + " )" + ANSI_RESET);

        if (server.getTakenStartingPoints().contains(desiredStartPoint)) {
            logger.info(ANSI_GREEN + "ERROR: STARTPOINT IS ALREADY TAKEN. REQUEST DENIED." + ANSI_RESET);

            JSONMessage jsonMessage = new JSONMessage("Error", new ErrorBody("Sorry, this StartingPoint is already taken!"));
            task.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
            task.getWriter().flush();
        } else {
            // StartingPoint is available
            logger.info(ANSI_GREEN + "( HANDLESETSTARTINGPOINT ): STARTPOINT ON COORDINATES ( " + x + " | " + y + " ) WAS GIVEN TO PLAYER WITH ID " + player.getPlayerID() + ANSI_RESET);
            server.setSetStartPoints(server.getSetStartPoints() + 1);
            logger.info(ANSI_GREEN + "( HANDLESETSTARTINGPOINT ): INCREMENTED SETSTARTPOINT COUNTER TO " + server.getSetStartPoints() + ANSI_RESET);

            // Update servers robot position
            player.getPlayerRobot().setxPosition(x);
            player.getPlayerRobot().setyPosition(y);


            // Update servers robot orientation
            String antennaOrientation = server.getAntenna().getOrientations().get(0);
            player.getPlayerRobot().setLineOfSight(antennaOrientation);
            logger.info(ANSI_GREEN + "SERVER ROBOT INSTANCE UPDATED! NEW LINE OF SIGHT: " + antennaOrientation + ANSI_RESET);

            for (Server.ClientWrapper client : server.getConnectedClients()) {
                JSONMessage jsonMessage = new JSONMessage("StartingPointTaken", new StartingPointTakenBody(x, y, player.getPlayerID()));
                client.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                client.getWriter().flush();
            }

            if (server.getSetStartPoints() == server.getPlayers().size()) {
                //TODO replace this with upgrade phase
                for (Server.ClientWrapper client : server.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("ActivePhase", new ActivePhaseBody(2));
                    client.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    client.getWriter().flush();
                }

                for (Server.ClientWrapper test : server.getConnectedClients()) {
                    System.out.println(test.getPlayer().getPlayerRobot().getxPosition());
                    System.out.println(test.getPlayer().getPlayerRobot().getyPosition());
                }

                for (Server.ClientWrapper client : server.getConnectedClients()) {
                    Player eachPlayer = client.getPlayer();

                    logger.info(ANSI_GREEN + "( HANDLESETSTARTINGPOINT ): SIZE OF DRAW PILE BEFORE DRAWING CARDS: " + eachPlayer.getDeckDraw().getDeck().size() + ANSI_RESET);
                    client.getPlayer().drawHandCards(eachPlayer.getDeckHand(), eachPlayer.getDeckDraw(), eachPlayer.getDeckDiscard());
                    logger.info(ANSI_GREEN + "( HANDLESETSTARTINGPOINT ): SIZE OF DRAW PILE AFTER DRAWING CARDS: " + eachPlayer.getDeckDraw().getDeck().size() + ANSI_RESET);

                    ArrayList<Card> cardsInHand = eachPlayer.getDeckHand().getDeck();
                    int cardsInPile = eachPlayer.getDeckDraw().getDeck().size();

                    for (Server.ClientWrapper otherClient : server.getConnectedClients()) {
                        if (client.getClientSocket().equals(otherClient.getClientSocket())) {
                            JSONMessage jsonMessage = new JSONMessage("YourCards", new YourCardsBody(cardsInHand, cardsInPile));
                            client.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                            client.getWriter().flush();
                        } else {
                            JSONMessage jsonMessage = new JSONMessage("NotYourCards", new NotYourCardsBody(client.getPlayer().getPlayerID(), cardsInHand.size(), cardsInPile));
                            client.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                            client.getWriter().flush();
                        }
                    }

                }
            }


            // Startpoint is now taken
            server.getTakenStartingPoints().add(desiredStartPoint);
        }
    }


    /**
     * This method contains the logic that comes into action when a 'SelectCard' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server           The Server itself.
     * @param task             The ReaderTask of the distributerServer (Gives access to the PrintWriter).
     * @param selectedCardBody The message body of the message which is of type {@link SelectedCardBody}.
     */
    public void handleSelectedCard(Server server, Server.ServerReaderTask task, SelectedCardBody selectedCardBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleSelectedCard()" + ANSI_RESET);

        int register = selectedCardBody.getRegister();
        Card selectedCard = selectedCardBody.getCard();

        for (Server.ClientWrapper client : server.getConnectedClients()) {
            if (client.getClientSocket().equals(task.getClientSocket())) {
                Player player = client.getPlayer();
                int selectedCardsNumber = player.getSelectedCards();

                // Update the player's register deck
                player.getDeckRegister().getDeck().set(register - 1, selectedCard);

                if (selectedCard == null) {
                    selectedCardsNumber--;
                    player.setSelectedCards(selectedCardsNumber);
                } else {
                    selectedCardsNumber++;
                    player.setSelectedCards(selectedCardsNumber);
                }

                // Send CardSelected to everyone
                for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardSelected", new CardSelectedBody(player.getPlayerID(), register));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();

                    if (selectedCardsNumber == REGISTER_CARDS_AMOUNT) {
                        JSONMessage jsonMsg = new JSONMessage("SelectionFinished", new SelectionFinishedBody(player.getPlayerID()));
                        clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMsg));
                        clientWrapper.getWriter().flush();

                        //Timer started message sent after first player fills five registers
                        JSONMessage jsonMessageTimerStarted = new JSONMessage("TimerStarted", new TimerStartedBody());
                        clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessageTimerStarted));
                        clientWrapper.getWriter().flush();
                    }
                }

                if (selectedCard == null) {
                    logger.info(ANSI_GREEN + "( HANDLESELECTEDCARD ): SET CARD null " + " FOR PLAYER " + player.getName() + " IN REGISTER " + register + ANSI_RESET);
                    logger.info(ANSI_GREEN + "( HANDLESELECTEDCARD ): DECK FOR PLAYER " + player.getName() + ": " + player.getDeckRegister().getDeck() + ANSI_RESET);
                } else {
                    logger.info(ANSI_GREEN + "( HANDLESELECTEDCARD ): SET CARD " + selectedCard.getCardName() + " FOR PLAYER " + player.getName() + " IN REGISTER " + register + ANSI_RESET);
                    logger.info(ANSI_GREEN + "( HANDLESELECTEDCARD ): DECK FOR PLAYER " + player.getName() + ": " + player.getDeckRegister().getDeck() + ANSI_RESET);
                }
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                           HANDLERS FOR SERVER MESSAGES                                        //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method contains the logic that comes into action when a 'HelloClient' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client          The Client itself.
     * @param task            The ReaderTask of the client (Gives access to the PrintWriter).
     * @param helloClientBody The message body of the message which is of type {@link HelloClientBody}.
     */
    public void handleHelloClient(Client client, Client.ClientReaderTask task, HelloClientBody helloClientBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleHelloClient()" + ANSI_RESET);

        logger.info("Server has protocol " + helloClientBody.getProtocol());
        client.setWaitingForHelloClient(false);
    }

    /**
     * This method contains the logic that comes into action when a 'Welcome' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client      The Client itself.
     * @param task        The ReaderTask of the client (Gives access to the PrintWriter).
     * @param welcomeBody The message body of the message which is of type {@link WelcomeBody}.
     */
    public void handleWelcome(Client client, Client.ClientReaderTask task, WelcomeBody welcomeBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleWelcome()" + ANSI_RESET);
        logger.info("PlayerID: " + welcomeBody.getPlayerID());

        // Client creates his player instance
        Player player = new Player();
        player.setPlayerID(welcomeBody.getPlayerID());
        client.setPlayer(player);

        logger.info("CLIENT CREATED HIS PLAYER WITH PLAYER ID: " + player.getPlayerID());

        task.setPlayerID(welcomeBody.getPlayerID());

    }

    /**
     * This method contains the logic that comes into action when a 'PlayerAdded' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client          The Client itself.
     * @param task            The ReaderTask of the client (Gives access to the PrintWriter).
     * @param playerAddedBody The message body of the message which is of type {@link PlayerAddedBody}.
     */
    public void handlePlayerAdded(Client client, Client.ClientReaderTask task, PlayerAddedBody playerAddedBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handlePlayedAdded()" + ANSI_RESET);

        Platform.runLater(() -> {
            client.getActiveClientsProperty().add(String.valueOf(playerAddedBody.getPlayerID()));
            Client.OtherPlayer newPlayer = client.new OtherPlayer(playerAddedBody.getPlayerID());
            client.getOtherActivePlayers().add(client.getOtherActivePlayers().size(), newPlayer);

            // Extracting message content
            String messageName = playerAddedBody.getName();
            int messageFigure = playerAddedBody.getFigure();
            int messagePlayerID = playerAddedBody.getPlayerID();

            if (playerAddedBody.getPlayerID() == client.getPlayer().getPlayerID()) {
                // PlayerAdded message due to own player has been added
                client.getPlayer().setName(messageName);
                client.getPlayer().initRobotByFigure(messageFigure);

                logger.info("CLIENT " + ANSI_GREEN + client.getPlayer().getName() + ANSI_RESET + " UPDATED HIS OWN PLAYER. UPDATES: "
                        + "FIGURE: " + messageFigure + ", ROBOT: " + client.getPlayer().getPlayerRobot() + ", NAME: " + messageName);
            } else {
                // PlayerAdded message due to other player has been added
                Player otherPlayer = new Player();
                otherPlayer.setPlayerID(messagePlayerID);
                otherPlayer.setName(messageName);
                otherPlayer.initRobotByFigure(messageFigure);

                client.getOtherPlayers().add(otherPlayer);

                if (client.getPlayer().getName() == "") {
                    logger.info("CLIENT " + ANSI_GREEN + "- NO NAME YET - " + ANSI_RESET
                            + " ADDED A NEW OTHERPLAYER WITH PROPERTIES: PLAYERID: " + messagePlayerID + ", NAME: " + messageName + ", FIGURE: " + messageFigure);
                } else {
                    logger.info("CLIENT " + ANSI_GREEN + client.getPlayer().getName() + ANSI_RESET
                            + " ADDED A NEW OTHERPLAYER WITH PROPERTIES: PLAYERID: " + messagePlayerID + ", NAME: " + messageName + ", FIGURE: " + messageFigure);
                }

                ChooseRobotController chooseRobotController = client.getChooseRobotController();

                // Update the chooseRobot view because a robot has been assigned
                if (messageFigure == 1) {
                    chooseRobotController.getHammerBot().setDisable(true);
                    chooseRobotController.getHammerBot().setOpacity(0.5);
                } else if (messageFigure == 2) {
                    chooseRobotController.getHulkX90().setDisable(true);
                    chooseRobotController.getHulkX90().setOpacity(0.5);
                } else if (messageFigure == 3) {
                    chooseRobotController.getSmashBot().setDisable(true);
                    chooseRobotController.getSmashBot().setOpacity(0.5);
                } else if (messageFigure == 4) {
                    chooseRobotController.getSpinBot().setDisable(true);
                    chooseRobotController.getSpinBot().setOpacity(0.5);
                } else if (messageFigure == 5) {
                    chooseRobotController.getTwonky().setDisable(true);
                    chooseRobotController.getTwonky().setOpacity(0.5);
                } else if (messageFigure == 6) {
                    chooseRobotController.getZoomBot().setDisable(true);
                    chooseRobotController.getZoomBot().setOpacity(0.5);
                }
            }
        });
    }

    /**
     * This method contains the logic that comes into action when a 'PlayerStatus' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client           The Client itself.
     * @param task             The ReaderTask of the client (Gives access to the PrintWriter).
     * @param playerStatusBody The message body of the message which is of type {@link PlayerStatusBody}.
     */
    public void handlePlayerStatus(Client client, Client.ClientReaderTask task, PlayerStatusBody playerStatusBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handlePlayerStatus()" + ANSI_RESET);

        int messagePlayerID = playerStatusBody.getPlayerID();
        boolean readyStatus = playerStatusBody.isReady();

        // Update client's player instance (either own or corresponding OtherPlayer)
        if (client.getPlayer().getPlayerID() == playerStatusBody.getPlayerID()) {
            client.getPlayer().setReady(readyStatus);

            logger.info("CLIENT " + ANSI_GREEN + client.getPlayer().getName() + ANSI_RESET
                    + " UPDATED HIS OWN PLAYER. UPDATES: PLAYERSTATUS: " + readyStatus);
        } else {
            // Ready status of other player has changed
            for (Player otherPlayer : client.getOtherPlayers()) {
                if (otherPlayer.getPlayerID() == messagePlayerID) {
                    otherPlayer.setReady(readyStatus);

                    if (client.getPlayer().getName() == "") {
                        logger.info("CLIENT " + ANSI_GREEN + "- NO NAME YET -" + ANSI_RESET
                                + " UPDATED HIS OTHERPLAYER. UPDATES: PLAYERSTATUS: " + readyStatus);
                    } else {
                        logger.info("CLIENT " + ANSI_GREEN + client.getPlayer().getName() + ANSI_RESET
                                + " UPDATED HIS OTHERPLAYER. UPDATES: PLAYERSTATUS: " + readyStatus);
                    }
                }
            }
        }

        Platform.runLater(() -> {
            if (readyStatus) {
                client.receiveMessage("Player " + playerStatusBody.getPlayerID() + " is ready!");
            } else {
                client.receiveMessage("Player " + playerStatusBody.getPlayerID() + " is not ready!");
            }
        });
    }

    /**
     * This method contains the logic that comes into action when a 'GameStarted' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client          The Client itself.
     * @param task            The ReaderTask of the client (Gives access to the PrintWriter).
     * @param gameStartedBody The message body of the message which is of type {@link GameStartedBody}.
     */
    public void handleGameStarted(Client client, Client.ClientReaderTask task, GameStartedBody gameStartedBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleGameStarted()" + ANSI_RESET);

        MapController mapController = (MapController) controllerMap.get("Map");
        mapController.fillGridPaneWithMap(gameStartedBody);
    }


    /**
     * This method contains the logic that comes into action when a 'ReceivedChat' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client           The Client itself.
     * @param task             The ReaderTask of the client (Gives access to the PrintWriter).
     * @param receivedChatBody The message body of the message which is of type {@link ReceivedChatBody}.
     */
    public void handleReceivedChat(Client client, Client.ClientReaderTask task, ReceivedChatBody receivedChatBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleReceivedChat()" + ANSI_RESET);

        // Works for both ordinary and private messages
        Platform.runLater(() -> client.receiveMessage(receivedChatBody.getMessage()));
    }

    /**
     * This method contains the logic that comes into action when a 'Error' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client    The Client itself.
     * @param task      The ReaderTask of the client (Gives access to the PrintWriter).
     * @param errorBody The message body of the message which is of type {@link ErrorBody}.
     * @author Ivan Dovecar
     * @author Manu
     */
    public void handleError(Client client, Client.ClientReaderTask task, ErrorBody errorBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleError()" + ANSI_RESET);

        String errorMessage = errorBody.getError();

        Platform.runLater(() -> {
            if (errorMessage.equals("Error: name already exists")) {
                logger.info(errorMessage);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error occured");
                alert.setHeaderText("Username already taken!");
                alert.setContentText(errorMessage);
                alert.show();

                client.getChatController().nameSettingFinishedProperty().setValue(false);
                client.getChatController().getFieldName().setDisable(false);
                //TODO write code here for proper reaction
            }
            if (errorMessage.equals("Error: figure already exists")) {
                logger.info(errorMessage);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error occured");
                alert.setHeaderText("Figure already taken!");
                alert.setContentText(errorMessage);
                alert.show();

                //TODO write code here for proper reaction
            }
            if (errorMessage.equals("Sorry, this StartingPoint is already taken!")) {
                logger.info(errorMessage);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error occured");
                alert.setHeaderText("StartPoint already taken!");
                alert.setContentText(errorMessage);
                alert.show();
            }
        });
    }

    /**
     * This method contains the logic that comes into action when a 'CardPlayed' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client         The Client itself.
     * @param task           The ReaderTask of the client (Gives access to the PrintWriter).
     * @param cardPlayedBody The message body of the message which is of type {@link CardPlayedBody}.
     */
    public void handleCardPlayed(Client client, Client.ClientReaderTask task, CardPlayedBody cardPlayedBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleCardPlayed()" + ANSI_RESET);

        int messagePlayerID = cardPlayedBody.getPlayerID();
        Card playedCard = cardPlayedBody.getCard();
        String cardName = playedCard.getCardName();
        String oldPosition = null;

        if (messagePlayerID == client.getPlayer().getPlayerID()) {
            // Update own robot
            oldPosition = client.getPlayer().getPlayerRobot().getxPosition() + "-" + client.getPlayer().getPlayerRobot().getyPosition();
            playedCard.activateCard(client.getPlayer());
            logger.info(ANSI_GREEN + "( HANDLECARDPLAYED ): CLIENT UPDATED OWN ROBOT!" + ANSI_RESET);
        } else {
            // Update OtherPlayer robot
            for (Player player : client.getOtherPlayers()) {
                if (player.getPlayerID() == messagePlayerID) {
                    oldPosition = client.getPlayer().getPlayerRobot().getxPosition() + "-" + client.getPlayer().getPlayerRobot().getyPosition();
                    playedCard.activateCard(player);
                    logger.info(ANSI_GREEN + "( HANDLECARDPLAYED ): CLIENT UPDATED OTHER ROBOT!" + ANSI_RESET);
                }
            }
        }

        // New position of player's robot
        int x = client.getPlayer().getPlayerRobot().getxPosition();
        int y = client.getPlayer().getPlayerRobot().getyPosition();

        //TODO check that final
        String finalOldPosition = oldPosition;
        Platform.runLater(() -> {
            // Update GUI
            if (cardName.equals("MoveI") || cardName.equals("MoveII") || cardName.equals("MoveIII")) {
                String newPosition = x + "-" + y;
                MapController mapController = client.getMapController();
                mapController.moveRobot(finalOldPosition, newPosition);


            } else if (cardName.equals("BackUp")) {
                String newPosition = x + "-" + y;
                MapController mapController = client.getMapController();
                mapController.moveRobot(finalOldPosition, newPosition);

            } else if (cardName.equals("TurnLeft")) {

                String robotPosition = x + "-" + y;
                String turnDirection = "left";

                MapController mapController = client.getMapController();
                mapController.turnRobot(robotPosition, turnDirection);

            } else if (cardName.equals("TurnRight")) {

                String robotPosition = x + "-" + y;
                String turnDirection = "right";

                MapController mapController = client.getMapController();
                mapController.turnRobot(robotPosition, turnDirection);

            } else if (cardName.equals("UTurn")) {
                String robotPosition = x + "-" + y;
                MapController mapController = client.getMapController();
                for (int i = 0; i < 2; i++) {
                    mapController.turnRobot(robotPosition, "right");
                }

            } else if (cardName.equals("PowerUp")) {

            } else if (cardName.equals("Again")) {

            }
        });
    }

    /**
     * This method contains the logic that comes into action when a 'CurrentPlayer' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client            The Client itself.
     * @param task              The ReaderTask of the client (Gives access to the PrintWriter).
     * @param currentPlayerBody The message body of the message which is of type {@link CurrentPlayerBody}.
     */
    public void handleCurrentPlayer(Client client, Client.ClientReaderTask task, CurrentPlayerBody
            currentPlayerBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleCurrentPlayer()" + ANSI_RESET);


        Platform.runLater(() -> {
            //Todo
        });
    }

    /**
     * This method contains the logic that comes into action when a 'ActivePhase' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client          The Client itself.
     * @param task            The ReaderTask of the client (Gives access to the PrintWriter).
     * @param activePhaseBody The message body of the message which is of type {@link ActivePhaseBody}.
     */
    public void handleActivePhase(Client client, Client.ClientReaderTask task, ActivePhaseBody activePhaseBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleActivePhase()" + ANSI_RESET);

        int activePhase = activePhaseBody.getPhase();

        // Construction phase
        if (activePhase == BUILD_UP_PHASE) {
            ArrayList<Card> deck = client.getPlayer().getDeckDraw().getDeck();
            client.getPlayer().getDeckDraw().shuffleDeck(deck);
            System.out.println(client.getPlayer().getDeckDraw().getDeck());
        }
        // Upgrade phase
        else if (activePhase == UPGRADE_PHASE) {

        }
        // Programming phase
        else if (activePhase == PROGRAMMING_PHASE) {

        }
        // Activation phase
        else if (activePhase == ACTIVATION_PHASE) {

        }
    }

    /**
     * This method contains the logic that comes into action when a 'StartingPointTaken' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client                 The Client itself.
     * @param task                   The ReaderTask of the client (Gives access to the PrintWriter).
     * @param startingPointTakenBody The message body of the message which is of type {@link StartingPointTakenBody}.
     */
    public void handleStartingPointTaken(Client client, Client.ClientReaderTask task, StartingPointTakenBody startingPointTakenBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleStartingPointTaken()" + ANSI_RESET);

        System.out.println("OWN PLAYER ID: " + client.getPlayer().getPlayerID());
        Platform.runLater(() -> {
            MapController mapController = (MapController) controllerMap.get("Map");
            String startPosition = startingPointTakenBody.getX() + "-" + startingPointTakenBody.getY();
            String antennaOrientation = client.getMapController().getAntenna().getOrientations().get(0);

            int xPos = startingPointTakenBody.getX();
            int yPos = startingPointTakenBody.getY();

            // For client that chose that StartPoint
            if (client.getPlayer().getPlayerID() == startingPointTakenBody.getPlayerID()) {
                mapController.setStartingPoint(client.getPlayer().getPlayerRobot(), startPosition);

                // Set StartPoint position of clients robot
                client.getPlayer().getPlayerRobot().setxPosition(xPos);
                client.getPlayer().getPlayerRobot().setyPosition(yPos);

                // Set orientation of clients robot
                client.getPlayer().getPlayerRobot().setLineOfSight(antennaOrientation);
                logger.info(ANSI_GREEN + "CLIENT UPDATED HIS OWN ROBOT! NEW LINE OF SIGHT: " + antennaOrientation + ANSI_RESET);

                mapController.setAllowedToSetStart(false);
                logger.info(ANSI_GREEN + "( HANDLESETSTARTINGPOINT ): STARTPOINT FOR THIS CLIENT SET. DISABLED OPTION TO SET STARTPOINT." + ANSI_RESET);
            } else {
                // For everyone else
                Robot otherPlayerRobot;

                for (Player otherPlayer : client.getOtherPlayers()) {
                    if (otherPlayer.getPlayerID() == startingPointTakenBody.getPlayerID()) {
                        otherPlayerRobot = otherPlayer.getPlayerRobot();

                        // Update position of OtherPlayer's robot
                        otherPlayerRobot.setxPosition(xPos);
                        otherPlayerRobot.setyPosition(yPos);

                        // Update orientation of OtherPlayer's robot
                        otherPlayerRobot.setLineOfSight(antennaOrientation);
                        logger.info(ANSI_GREEN + "CLIENT UPDATED OTHER PLAYERS ROBOT! NEW LINE OF SIGHT: " + antennaOrientation + ANSI_RESET);

                        mapController.setStartingPoint(otherPlayerRobot, startPosition);
                    }
                }
            }
        });
    }

    /**
     * This method contains the logic that comes into action when a 'YourCards' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client        The Client itself.
     * @param task          The ReaderTask of the client (Gives access to the PrintWriter).
     * @param yourCardsBody The message body of the message which is of type {@link YourCardsBody}.
     */
    public void handleYourCards(Client client, Client.ClientReaderTask task, YourCardsBody yourCardsBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleYourCards()" + ANSI_RESET);

        Platform.runLater(() -> {
            ArrayList<Card> deck = yourCardsBody.getCardsInHand();
            ((PlayerMatController) controllerMap.get("PlayerMat")).initializeCards(deck);
        });

    }

    /**
     * This method contains the logic that comes into action when a 'NotYourCards' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client           The Client itself.
     * @param task             The ReaderTask of the client (Gives access to the PrintWriter).
     * @param notYourCardsBody The message body of the message which is of type {@link NotYourCardsBody}.
     */
    public void handleNotYourCards(Client client, Client.ClientReaderTask task, NotYourCardsBody notYourCardsBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleNotYourCards()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    /**
     * This method contains the logic that comes into action when a 'ShuffleCoding' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client            The Client itself.
     * @param task              The ReaderTask of the client (Gives access to the PrintWriter).
     * @param shuffleCodingBody The message body of the message which is of type {@link ShuffleCodingBody}.
     */
    public void handleShuffleCoding(Client client, Client.ClientReaderTask task, ShuffleCodingBody
            shuffleCodingBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleShuffleCoding()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    /**
     * This method contains the logic that comes into action when a 'CardSelected' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client           The Client itself.
     * @param task             The ReaderTask of the client (Gives access to the PrintWriter).
     * @param cardSelectedBody The message body of the message which is of type {@link CardSelectedBody}.
     */
    public void handleCardSelected(Client client, Client.ClientReaderTask task, CardSelectedBody cardSelectedBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleCardSelected()" + ANSI_RESET);

        Platform.runLater(() -> {
            int register = cardSelectedBody.getRegister();
            int playerID = cardSelectedBody.getPlayerID();
            client.getOpponentMatController().updateOpponentRegister(register, playerID);
        });
    }

    /**
     * This method contains the logic that comes into action when a 'SelectionFinished' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client                The Client itself.
     * @param task                  The ReaderTask of the client (Gives access to the PrintWriter).
     * @param selectionFinishedBody The message body of the message which is of type {@link SelectionFinishedBody}.
     */
    public void handleSelectionFinished(Client client, Client.ClientReaderTask task, SelectionFinishedBody
            selectionFinishedBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleSelectionFinished()" + ANSI_RESET);

        Platform.runLater(() -> {
            //todo
        });
    }

    /**
     * This method contains the logic that comes into action when a 'TimerStarted' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client           The Client itself.
     * @param task             The ReaderTask of the client (Gives access to the PrintWriter).
     * @param timerStartedBody The message body of the message which is of type {@link TimerStartedBody}.
     */
    public void handleTimerStarted(Client client, Client.ClientReaderTask task, TimerStartedBody timerStartedBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleTimerStarted()" + ANSI_RESET);

        Platform.runLater(() -> {
            //Todo If times up, this block should be activated
            /*
            //Remove register cards from hand
            client.getPlayer().getDeckHand().getDeck().removeAll(client.getPlayer().getDeckRegister().getDeck());
            ArrayList<Card> remainingCardsInHand = client.getPlayer().getDeckHand().getDeck();
            DeckDiscard clientDiscards = client.getPlayer().getDeckDiscard();
            //Cards from hand are added to discard pile
            clientDiscards.getDeck().addAll(remainingCardsInHand);

            //Todo: Timer activation and test with if else here
            client.getPlayerMatController().emptyCards();

             */

            client.getChatController().getTimer().setVisible(true);
            client.getChatController().getTimer().setTranslateY(-200);

            Countdown timer = new Countdown(30, 0, 1000);
            timer.startTimer(client.getChatController().getTimer());
        });
    }

    /**
     * This method contains the logic that comes into action when a 'TimerEnded' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client         The Client itself.
     * @param task           The ReaderTask of the client (Gives access to the PrintWriter).
     * @param timerEndedBody The message body of the message which is of type {@link TimerEndedBody}.
     */
    public void handleTimerEnded(Client client, Client.ClientReaderTask task, TimerEndedBody timerEndedBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleTimerEnded()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    /**
     * This method contains the logic that comes into action when a 'CardsYouGotNow' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client             The Client itself.
     * @param task               The ReaderTask of the client (Gives access to the PrintWriter).
     * @param cardsYouGotNowBody The message body of the message which is of type {@link CardsYouGotNowBody}.
     */
    public void handleCardsYouGotNow(Client client, Client.ClientReaderTask task, CardsYouGotNowBody
            cardsYouGotNowBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ):  handleCardsYouGotNow()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    /**
     * This method contains the logic that comes into action when a 'CurrentCards' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client           The Client itself.
     * @param task             The ReaderTask of the client (Gives access to the PrintWriter).
     * @param currentCardsBody The message body of the message which is of type {@link CurrentCardsBody}.
     */
    public void handleCurrentCards(Client client, Client.ClientReaderTask task, CurrentCardsBody currentCardsBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleCurrentCards()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    /**
     * This method contains the logic that comes into action when a 'Movement' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client       The Client itself.
     * @param task         The ReaderTask of the client (Gives access to the PrintWriter).
     * @param movementBody The message body of the message which is of type {@link MovementBody}.
     */
    public void handleMovement(Client client, Client.ClientReaderTask task, MovementBody movementBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleMovement()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    /**
     * This method contains the logic that comes into action when a 'DrawDamage' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client         The Client itself.
     * @param task           The ReaderTask of the client (Gives access to the PrintWriter).
     * @param drawDamageBody The message body of the message which is of type {@link DrawDamageBody}.
     */
    public void handleDrawDamage(Client client, Client.ClientReaderTask task, DrawDamageBody drawDamageBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleDrawDamage()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    /**
     * This method contains the logic that comes into action when a 'PlayerShooting' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client             The Client itself.
     * @param task               The ReaderTask of the client (Gives access to the PrintWriter).
     * @param playerShootingBody The message body of the message which is of type {@link PlayerShootingBody}.
     */
    public void handlePlayerShooting(Client client, Client.ClientReaderTask task, PlayerShootingBody
            playerShootingBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handlePlayerShooting()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    /**
     * This method contains the logic that comes into action when a 'RestartPoint' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client     The Client itself.
     * @param task       The ReaderTask of the client (Gives access to the PrintWriter).
     * @param rebootBody The message body of the message which is of type {@link RebootBody}.
     */
    public void handleReboot(Client client, Client.ClientReaderTask task, RebootBody rebootBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleReboot()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    /**
     * This method contains the logic that comes into action when a 'PlayerTurning' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client            The Client itself.
     * @param task              The ReaderTask of the client (Gives access to the PrintWriter).
     * @param playerTurningBody The message body of the message which is of type {@link PlayerTurningBody}.
     */
    public void handlePlayerTurning(Client client, Client.ClientReaderTask task, PlayerTurningBody
            playerTurningBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handlePlayerTurning()" + ANSI_RESET);

        Platform.runLater(() -> {

        });
    }

    /**
     * This method contains the logic that comes into action when a 'Energy' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client     The Client itself.
     * @param task       The ReaderTask of the client (Gives access to the PrintWriter).
     * @param energyBody The message body of the message which is of type {@link EnergyBody}.
     */
    public void handleEnergy(Client client, Client.ClientReaderTask task, EnergyBody energyBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleEnergy()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    /**
     * This method contains the logic that comes into action when a 'CheckPointReached' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client                The Client itself.
     * @param task                  The ReaderTask of the client (Gives access to the PrintWriter).
     * @param checkPointReachedBody The message body of the message which is of type {@link CheckPointReachedBody}.
     */
    public void handleCheckPointReached(Client client, Client.ClientReaderTask task, CheckPointReachedBody
            checkPointReachedBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleCheckPointReached()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    /**
     * This method contains the logic that comes into action when a 'StartingPointTaken' protocol message was received and
     * deserialized by the {@link Client}. It is triggered by {@link ServerMessageAction#triggerAction(Client, Client.ClientReaderTask, Object, MessageDistributer)}.
     *
     * @param client           The Client itself.
     * @param task             The ReaderTask of the client (Gives access to the PrintWriter).
     * @param gameFinishedBody The message body of the message which is of type {@link GameFinishedBody}.
     */
    public void handleGameFinished(Client client, Client.ClientReaderTask task, GameFinishedBody gameFinishedBody) {
        System.out.println(ANSI_CYAN + "( MESSAGEDISTRIBUTER ): Entered handleGameFinished()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }
}
