package utils.json;

import java.io.IOException;
import server.game.Maps.Map;
import java.util.logging.Logger;

import client.Client;
import javafx.application.Platform;
import server.Server;
import utils.Parameter;
import utils.json.protocol.*;

/**
 * This class has the sole purpose to distribute the logic for each {@link JSONMessage} into seperate functions
 * that are called when the corresponding message was deserialized.
 *
 * @author Manuel Neumayer
 */
public class MessageDistributer {
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static final Logger logger = Logger.getLogger( MessageDistributer.class.getName() );


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                           HANDLERS FOR CLIENT MESSAGES                                        //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static void handleHelloServer(Server server, Server.ServerReaderTask task, HelloServerBody bodyObject) {
        System.out.println(ANSI_CYAN + "Entered handleHelloServer()" + ANSI_RESET);

        try {
            if (bodyObject.getProtocol().equals(server.getProtocolVersion())){
                logger.info("Protocol version test succeeded");

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

    public static void handlePlayerValues(Server server, Server.ServerReaderTask task, PlayerValuesBody playerValuesBody) {
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

            //Inform new Client about all current activeClients (send all connected clients to new client without own entry)
            for(Server.ClientWrapper client : server.getConnectedClients()) {
                if (!client.getClientSocket().equals(task.getClientSocket())) {
                    // TODO PlayerAdded message of every already active client is sent to new client
                }
            }
        }
    }

    public static void handleSetStatus(Server server, Server.ServerReaderTask task, SetStatusBody setStatusBody) {
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
            Map map = new Map();

            for (Server.ClientWrapper client : server.getConnectedClients()) {
                JSONMessage jsonMessage = new JSONMessage("GameStarted", new GameStartedBody(map));
                client.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                client.getWriter().flush();
            }

        }
    }

    public static void handleSendChat(Server server, Server.ServerReaderTask task, SendChatBody sendChatBody) {
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
        String content = senderName + ": " + messageContent;

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
            // TODO: PRIVATE MESSAGE
        }
    }

    public static void handlePlayCard(Server server, Server.ServerReaderTask task, PlayCardBody playCardBody) {
        System.out.println(ANSI_CYAN + "Entered handlePlayCard()" + ANSI_RESET);

        // TODO: Write code here
    }

    public static void handleSetStartingPoint(Server server, Server.ServerReaderTask task, SetStartingPointBody setStartingPointBody) {
        System.out.println(ANSI_CYAN + "Entered handleSetStartingPoint()" + ANSI_RESET);


        // TODO: Write code here
    }

    public static void handleSelectCard(Server server, Server.ServerReaderTask task, SelectCardBody selectCardBody) {
        System.out.println(ANSI_CYAN + "Entered handleSelectCard()" + ANSI_RESET);

        // TODO: Write code here
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                           HANDLERS FOR SERVER MESSAGES                                        //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static void handleHelloClient(Client client, HelloClientBody helloClientBody) {
        System.out.println(ANSI_CYAN + "Entered handleHelloClient()" + ANSI_RESET);

        logger.info("Server has protocol " + helloClientBody.getProtocol());
        client.setWaitingForHelloClient(false);
    }

    public static void handleWelcome(Client client, Client.ClientReaderTask task, WelcomeBody welcomeBody) {
        System.out.println(ANSI_CYAN + "Entered handleWelcome()" + ANSI_RESET);
        logger.info("PlayerID: " + welcomeBody.getPlayerID());

        task.setPlayerID(welcomeBody.getPlayerID());
    }

    public static void handlePlayerAdded(Client client, Client.ClientReaderTask task, PlayerAddedBody playerAddedBody) {
        System.out.println(ANSI_CYAN + "Entered handlePlayedAdded()" + ANSI_RESET);

        Platform.runLater(() -> {
            client.getActiveClientsProperty().add(String.valueOf(playerAddedBody.getPlayerID()));
            Client.OtherPlayer newPlayer = client.new OtherPlayer(playerAddedBody.getPlayerID());
            client.getOtherActivePlayers().add(client.getOtherActivePlayers().size(), newPlayer);
        });

    }

    public static void handlePlayerStatus(Client client, Client.ClientReaderTask task, PlayerStatusBody playerStatusBody) {
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

    public static void handleGameStarted(Client client, Client.ClientReaderTask task, GameStartedBody gameStartedBody) {
        System.out.println(ANSI_CYAN + "Entered handleGameStarted()" + ANSI_RESET);

        // TODO: Write code heree
    }

    public static void handleReceivedChat(Client client, Client.ClientReaderTask task, ReceivedChatBody receivedChatBody) {
        System.out.println(ANSI_CYAN + "Entered handleReceivedChat()" + ANSI_RESET);

        // Works for both ordinary and private messages
        Platform.runLater(() -> client.receiveMessage(receivedChatBody.getMessage()));
    }

    public static void handleError(Client client, Client.ClientReaderTask task, ErrorBody errorBody) {
        System.out.println(ANSI_CYAN + "Entered handleError()" + ANSI_RESET);

        String errorMessage = errorBody.getError();

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
    }

    public static void handleCardPlayed(Client client, Client.ClientReaderTask task, CardPlayedBody cardPlayedBody) {
        System.out.println(ANSI_CYAN + "Entered handleCardPlayed()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleCurrentPlayer(Client client, Client.ClientReaderTask task, CurrentPlayerBody currentPlayerBody) {
        System.out.println(ANSI_CYAN + "Entered handleCurrentPlayer()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleActivePhase(Client client, Client.ClientReaderTask task, ActivePhaseBody activePhaseBody) {
        System.out.println(ANSI_CYAN + "Entered handleActivePhase()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleStartingPointTaken(Client client, Client.ClientReaderTask task, StartingPointTakenBody startingPointTakenBody) {
        System.out.println(ANSI_CYAN + "Entered handleStartingPointTaken()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleYourCards(Client client, Client.ClientReaderTask task, YourCardsBody yourCardsBody) {
        System.out.println(ANSI_CYAN + "Entered handleYourCards()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleNotYourCards(Client client, Client.ClientReaderTask task, NotYourCardsBody notYourCardsBody) {
        System.out.println(ANSI_CYAN + "Entered handleNotYourCards()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleShuffleCoding(Client client, Client.ClientReaderTask task, ShuffleCodingBody shuffleCodingBody) {
        System.out.println(ANSI_CYAN + "Entered handleShuffleCoding()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleCardSelected(Client client, Client.ClientReaderTask task, CardSelectedBody cardSelectedBody) {
        System.out.println(ANSI_CYAN + "Entered handleCardSelected()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleSelectionFinished(Client client, Client.ClientReaderTask task, SelectionFinishedBody selectionFinishedBody) {
        System.out.println(ANSI_CYAN + "Entered handleSelectionFinished()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleTimerStarted(Client client, Client.ClientReaderTask task, TimerStartedBody timerStartedBody) {
        System.out.println(ANSI_CYAN + "Entered handleTimerStarted()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleTimerEnded(Client client, Client.ClientReaderTask task, TimerEndedBody timerEndedBody) {
        System.out.println(ANSI_CYAN + "Entered handleTimerEnded()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleCardsYouGotNow(Client client, Client.ClientReaderTask task, CardsYouGotNowBody cardsYouGotNowBody) {
        System.out.println(ANSI_CYAN + "Entered handleCardsYouGotNow()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleCurrentCards(Client client, Client.ClientReaderTask task, CurrentCardsBody currentCardsBody) {
        System.out.println(ANSI_CYAN + "Entered handleCurrentCards()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleMovement(Client client, Client.ClientReaderTask task, MovementBody movementBody) {
        System.out.println(ANSI_CYAN + "Entered handleMovement()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleDrawDamage(Client client, Client.ClientReaderTask task, DrawDamageBody drawDamageBody) {
        System.out.println(ANSI_CYAN + "Entered handleDrawDamage()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handlePlayerShooting(Client client, Client.ClientReaderTask task, PlayerShootingBody playerShootingBody) {
        System.out.println(ANSI_CYAN + "Entered handlePlayerShooting()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleReboot(Client client, Client.ClientReaderTask task, RebootBody rebootBody) {
        System.out.println(ANSI_CYAN + "Entered handleReboot()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handlePlayerTurning(Client client, Client.ClientReaderTask task, PlayerTurningBody playerTurningBody) {
        System.out.println(ANSI_CYAN + "Entered handlePlayerTurning()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleEnergy(Client client, Client.ClientReaderTask task, EnergyBody energyBody) {
        System.out.println(ANSI_CYAN + "Entered handleEnergy()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleCheckPointReached(Client client, Client.ClientReaderTask task, CheckPointReachedBody checkPointReachedBody) {
        System.out.println(ANSI_CYAN + "Entered handleCheckPointReached()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }

    public static void handleGameFinished(Client client, Client.ClientReaderTask task, GameFinishedBody gameFinishedBody) {
        System.out.println(ANSI_CYAN + "Entered handleGameFinished()" + ANSI_RESET);

        Platform.runLater(() -> {
            //TODO write code here
        });
    }
}
