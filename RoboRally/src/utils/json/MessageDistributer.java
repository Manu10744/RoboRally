package utils.json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import client.Client;
import javafx.application.Platform;
import server.Server;
import server.game.Card;
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
 */
public class MessageDistributer {
    public static Map<String, IController> controllerMap;
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    private Server server;

    private static final Logger logger = Logger.getLogger(MessageDistributer.class.getName());


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                           HANDLERS FOR CLIENT MESSAGES                                        //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method contains the logic that comes into action when a 'HelloServer' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server          The Server itself.
     * @param task            The ReaderTask of the server (Gives access to the PrintWriter).
     * @param helloServerBody The message body of the message which is of type {@link HelloServerBody}.
     *
     * @author Ivan Dovecar
     * @author Manu
     * @author Mia
     */

    /**
     * This method is used to get all the controllers that are saved in the stage. With those, the individual gui-elements, e.g. chat and map can be referenced
     *
     * @param stageControllerMap
     */
    public void setControllerMap(Map stageControllerMap) {
        controllerMap = stageControllerMap;
    }

    public void handleHelloServer(Server server, Server.ServerReaderTask task, HelloServerBody helloServerBody) {
        System.out.println(ANSI_CYAN + "Entered handleHelloServer()" + ANSI_RESET);

        try {
            if (helloServerBody.getProtocol().equals(server.getProtocolVersion())) {
                logger.info("Protocol version test succeeded");

                server = server;

                JSONMessage jsonMessage = new JSONMessage("Welcome", new WelcomeBody(server.getCounterPlayerID()));
                task.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                task.getWriter().flush();


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
        }
    }

    /**
     * This method contains the logic that comes into action when a 'PlayerValues' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server           The Server itself.
     * @param task             The ReaderTask of the server (Gives access to the PrintWriter).
     * @param playerValuesBody The message body of the message which is of type {@link PlayerValuesBody}.
     * @author Ivan Dovecar
     * @author Manu
     */
    public void handlePlayerValues(Server server, Server.ServerReaderTask task, PlayerValuesBody playerValuesBody) {
        System.out.println(ANSI_CYAN + "Entered handlePlayerValues()" + ANSI_RESET);

        String playerValueName = playerValuesBody.getName();
        int playerValueFigure = playerValuesBody.getFigure();

        boolean playerValueSuccess = true;

        for (Server.ClientWrapper client : server.getConnectedClients()) {
            // Checks if by PLAYER-VALUES received client' name is available
            if (client.getName().equals(playerValueName)) {
                logger.info("Client " + playerValueName + " refused (name already exists)");

                JSONMessage jsonMessage = new JSONMessage("Error", new ErrorBody("Error: name already exists"));
                task.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                task.getWriter().flush();

                playerValueSuccess = false;
                break;
            }

            // Checks if by PLAYER-VALUES received client' figure is available
            else if (client.getFigure() == playerValueFigure) {
                logger.info("Client " + playerValueName + " refused (figure already exists)");

                JSONMessage jsonMessage = new JSONMessage("Error", new ErrorBody("Error: figure already exists"));
                task.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                task.getWriter().flush();

                playerValueSuccess = false;
                break;
            }
        }

        // If by PLAYER_VALUES received name and figure are valid...
        if (playerValueSuccess) {
            logger.info("Client " + playerValueName + " successfully registered");

            //Add new Client to list connected clients
            server.getConnectedClients().add(server.new ClientWrapper(task.getClientSocket(), playerValueName, task.getWriter(), playerValueFigure, server.getSetterPlayerID(), false));

            //Send message to all active clients
            JSONMessage jsonMessage = new JSONMessage("PlayerAdded", new PlayerAddedBody(server.getCounterPlayerID(), playerValueName, playerValueFigure));
            task.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
            task.getWriter().flush();

            //Inform new client with private chat message about all current active clients(without own entry)
            for (Server.ClientWrapper client : server.getConnectedClients()) {
                if (playerValueName.equals(client.getName())) {
                    for (Server.ClientWrapper otherClient : server.getConnectedClients()) {
                        if (!playerValueName.equals(otherClient.getName())) {
                            String content = "Active player " + otherClient.getName() + " has ID " + otherClient.getPlayerID()
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
     * @param task          The ReaderTask of the server (Gives access to the PrintWriter).
     * @param setStatusBody The message body of the message which is of type {@link SetStatusBody}.
     */
    public void handleSetStatus(Server server, Server.ServerReaderTask task, SetStatusBody setStatusBody) {
        System.out.println(ANSI_CYAN + "Entered handleSetStatus()" + ANSI_RESET);

        boolean clientReady = setStatusBody.isReady();
        logger.info("IS READY STATUS :" + clientReady);
        int playerID = server.getConnectedClients().stream().filter(clientWrapper -> clientWrapper.getClientSocket().equals(task.getClientSocket()))
                .findFirst().get().getPlayerID();

        // Update the ClientWrapper due to the ready status change
        server.getConnectedClients().stream().filter(clientWrapper -> clientWrapper.getClientSocket().equals(task.getClientSocket())).findFirst().get().setReady(clientReady);

        for (Server.ClientWrapper client : server.getConnectedClients()) {
            logger.info("READY STATUS OF PLAYER " + client.getName() + ": " + client.isReady());
            logger.info("PLAYER ID: " + client.getPlayerID());

            JSONMessage jsonMessage = new JSONMessage("PlayerStatus", new PlayerStatusBody(playerID, clientReady));
            client.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
            client.getWriter().flush();
        }

        // Increase number of ready clients when true
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


            for (Server.ClientWrapper client : server.getConnectedClients()) {
                Path path = Paths.get("RoboRally/src/resources/maps/dizzyHighway.json");

                try {
                    String map = Files.readString(path, StandardCharsets.UTF_8);
                    client.getWriter().println(map);
                    client.getWriter().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    /**
     * This method contains the logic that comes into action when a 'SendChat' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server       The Server itself.
     * @param task         The ReaderTask of the server (Gives access to the PrintWriter).
     * @param sendChatBody The message body of the message which is of type {@link SendChatBody}.
     * @author Ivan Dovecar
     * @author Manu
     */
    public void handleSendChat(Server server, Server.ServerReaderTask task, SendChatBody sendChatBody) {
        System.out.println(ANSI_CYAN + "Entered handleSendChat()" + ANSI_RESET);

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
     * @param task         The ReaderTask of the server (Gives access to the PrintWriter).
     * @param playCardBody The message body of the message which is of type {@link PlayCardBody}.
     */
    public void handlePlayCard(Server server, Server.ServerReaderTask task, PlayCardBody playCardBody) {
        System.out.println(ANSI_CYAN + "Entered handlePlayCard()" + ANSI_RESET);

        // TODO: Write code here
    }

    /**
     * This method contains the logic that comes into action when a 'SetStartingPoint' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server               The Server itself.
     * @param task                 The ReaderTask of the server (Gives access to the PrintWriter).
     * @param setStartingPointBody The message body of the message which is of type {@link PlayerValuesBody}.
     */
    public void handleSetStartingPoint(Server server, Server.ServerReaderTask task, SetStartingPointBody setStartingPointBody) {


        /*
        (
        // TODO: should work here! But we do not have client, we have server

         */
    }

    /**
     * This method contains the logic that comes into action when a 'SelectCard' protocol message was received and
     * deserialized by the {@link Server}. It is triggered by {@link ClientMessageAction#triggerAction(Server, Server.ServerReaderTask, Object, MessageDistributer)}.
     *
     * @param server         The Server itself.
     * @param task           The ReaderTask of the server (Gives access to the PrintWriter).
     * @param selectCardBody The message body of the message which is of type {@link SelectCardBody}.
     */
    public void handleSelectCard(Server server, Server.ServerReaderTask task, SelectCardBody selectCardBody) {
        System.out.println(ANSI_CYAN + "Entered handleSelectCard()" + ANSI_RESET);

        // TODO: Write code here
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
        System.out.println(ANSI_CYAN + "Entered handleHelloClient()" + ANSI_RESET);

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
        System.out.println(ANSI_CYAN + "Entered handleWelcome()" + ANSI_RESET);
        logger.info("PlayerID: " + welcomeBody.getPlayerID());

        task.setPlayerID(welcomeBody.getPlayerID());

        // Inform about already connected clients, also to disable already assigned robots before loading the chooseRobot view
        /*for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
            int playerID = clientWrapper.getPlayerID();
            String name = clientWrapper.getName();
            int figure = clientWrapper.getFigure();

            JSONMessage informerMessage = new JSONMessage("PlayerAdded", new PlayerAddedBody(playerID, name, figure));
            task.getWriter().println(JSONEncoder.serializeJSON(informerMessage));
            task.getWriter().flush();
        }

         */

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
        System.out.println(ANSI_CYAN + "Entered handlePlayedAdded()" + ANSI_RESET);

        Platform.runLater(() -> {
            client.getActiveClientsProperty().add(String.valueOf(playerAddedBody.getPlayerID()));
            Client.OtherPlayer newPlayer = client.new OtherPlayer(playerAddedBody.getPlayerID());
            client.getOtherActivePlayers().add(client.getOtherActivePlayers().size(), newPlayer);

            int figure = playerAddedBody.getFigure();
            ChooseRobotController chooseRobotController = client.getChooseRobotController();

            // Update the chooseRobot view because a robot has been assigned
            if (figure == 1) {
                chooseRobotController.getHammerBot().setDisable(true);
                chooseRobotController.getHammerBot().setOpacity(0.5);
            } else if (figure == 2) {
                chooseRobotController.getHulkX90().setDisable(true);
                chooseRobotController.getHulkX90().setOpacity(0.5);
            } else if (figure == 3) {
                chooseRobotController.getSmashBot().setDisable(true);
                chooseRobotController.getSmashBot().setOpacity(0.5);
            } else if (figure == 4) {
                chooseRobotController.getSpinBot().setDisable(true);
                chooseRobotController.getSpinBot().setOpacity(0.5);
            } else if (figure == 5) {
                chooseRobotController.getTwonky().setDisable(true);
                chooseRobotController.getTwonky().setOpacity(0.5);
            } else if (figure == 6) {
                chooseRobotController.getZoomBot().setDisable(true);
                chooseRobotController.getZoomBot().setOpacity(0.5);
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
        System.out.println(ANSI_CYAN + "Entered handlePlayerStatus()" + ANSI_RESET);

        boolean readyStatus = playerStatusBody.isReady();

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
        System.out.println(ANSI_CYAN + "Entered handleGameStarted()" + ANSI_RESET);

        MapController mapController = (MapController) controllerMap.get("Map");
        mapController.fillGridPaneWithMap(gameStartedBody);
        //Todo: Those two method calls have to deleted when the game logic progresses to the right method in the distributer according to protocol

        // Popup of 9 cards to choose from
        ((PlayerMatController) controllerMap.get("PlayerMat")).openPopupCards(null); //handleYourCards
        System.out.println(ANSI_CYAN + "Entered handleSetStartingPoint()" + ANSI_RESET);

        // Choose Starting point, send message setStartingpoint
        int figure = client.getFigure();

        // NOTE: THIS IS A TEMPORARY SOLUTION. EVENT MAP_LOADED NEEDED
        // Make the thread wait a second so setting StartPoint happens definitely after map has been loaded...
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mapController.setStartingPoint(figure);
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
        System.out.println(ANSI_CYAN + "Entered handleReceivedChat()" + ANSI_RESET);

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
        System.out.println(ANSI_CYAN + "Entered handleError()" + ANSI_RESET);

        String errorMessage = errorBody.getError();

        Platform.runLater(() -> {
            if (errorMessage.equals("Error: name already exists")) {
                logger.info(errorMessage);
                //TODO write code here for proper reaction
            }
            if (errorMessage.equals("Error: figure already exists")) {
                logger.info(errorMessage);
                //TODO write code here for proper reaction
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
        System.out.println(ANSI_CYAN + "Entered handleCardPlayed()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
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
    public void handleCurrentPlayer(Client client, Client.ClientReaderTask task, CurrentPlayerBody currentPlayerBody) {
        System.out.println(ANSI_CYAN + "Entered handleCurrentPlayer()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
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
        System.out.println(ANSI_CYAN + "Entered handleActivePhase()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
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
        System.out.println(ANSI_CYAN + "Entered handleStartingPointTaken()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
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

        System.out.println(ANSI_CYAN + "Entered handleYourCards()" + ANSI_RESET);

        ArrayList<Card> deck = yourCardsBody.getCardsInHand();
        Platform.runLater(() -> {
            ((PlayerMatController) controllerMap.get("PlayerMat")).openPopupCards(deck);
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
        System.out.println(ANSI_CYAN + "Entered handleNotYourCards()" + ANSI_RESET);

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
    public void handleShuffleCoding(Client client, Client.ClientReaderTask task, ShuffleCodingBody shuffleCodingBody) {
        System.out.println(ANSI_CYAN + "Entered handleShuffleCoding()" + ANSI_RESET);

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
        System.out.println(ANSI_CYAN + "Entered handleCardSelected()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
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
    public void handleSelectionFinished(Client client, Client.ClientReaderTask task, SelectionFinishedBody selectionFinishedBody) {
        System.out.println(ANSI_CYAN + "Entered handleSelectionFinished()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
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
        System.out.println(ANSI_CYAN + "Entered handleTimerStarted()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
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
        System.out.println(ANSI_CYAN + "Entered handleTimerEnded()" + ANSI_RESET);

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
    public void handleCardsYouGotNow(Client client, Client.ClientReaderTask task, CardsYouGotNowBody cardsYouGotNowBody) {
        System.out.println(ANSI_CYAN + "Entered handleCardsYouGotNow()" + ANSI_RESET);

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
        System.out.println(ANSI_CYAN + "Entered handleCurrentCards()" + ANSI_RESET);

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
        System.out.println(ANSI_CYAN + "Entered handleMovement()" + ANSI_RESET);

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
        System.out.println(ANSI_CYAN + "Entered handleDrawDamage()" + ANSI_RESET);

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
    public void handlePlayerShooting(Client client, Client.ClientReaderTask task, PlayerShootingBody playerShootingBody) {
        System.out.println(ANSI_CYAN + "Entered handlePlayerShooting()" + ANSI_RESET);

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
        System.out.println(ANSI_CYAN + "Entered handleReboot()" + ANSI_RESET);

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
    public void handlePlayerTurning(Client client, Client.ClientReaderTask task, PlayerTurningBody playerTurningBody) {
        System.out.println(ANSI_CYAN + "Entered handlePlayerTurning()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
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
        System.out.println(ANSI_CYAN + "Entered handleEnergy()" + ANSI_RESET);

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
    public void handleCheckPointReached(Client client, Client.ClientReaderTask task, CheckPointReachedBody checkPointReachedBody) {
        System.out.println(ANSI_CYAN + "Entered handleCheckPointReached()" + ANSI_RESET);

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
        System.out.println(ANSI_CYAN + "Entered handleGameFinished()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }
}
