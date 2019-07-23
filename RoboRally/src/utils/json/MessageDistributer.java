package utils.json;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;
import static utils.Parameter.*;

import client.Client;
import com.google.gson.JsonDeserializer;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import server.Server;
import server.game.Card;
import server.game.Player;
import server.game.Robot;
import server.game.Tiles.*;
import server.game.Tiles.Antenna;
import server.game.Tiles.Tile;
import server.game.decks.DeckDiscard;
import server.game.decks.DeckDraw;
import server.game.decks.DeckHand;
import server.game.decks.DeckRegister;
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
            Path path = Paths.get("RoboRally/src/resources/maps/burnRun.json");
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
                            if (tile instanceof Wall){
                                String ID = xPos + "-" + yPos;
                                Wall wall = (Wall) tile;
                                server.getWallMap().put(ID, wall);
                            }
                            if(tile instanceof Pit){
                                String ID = xPos + "-" + yPos;
                                Pit pit = (Pit) tile;
                                server.getPitMap().put(ID, pit);
                            }
                            if(tile instanceof Gear){
                                String ID = xPos + "-" + yPos;
                                Gear gear = (Gear) tile;
                                server.getGearMap().put(ID, gear);
                            }
                            if(tile instanceof Laser){
                                String ID = xPos + "-" + yPos;
                                Laser laser = (Laser) tile;
                                server.getLaserMap().put(ID, laser);
                            }
                            if(tile instanceof PushPanel){
                                String ID = xPos + "-" + yPos;
                                PushPanel pushPanel = (PushPanel) tile;
                                server.getPushPanelMap().put(ID, pushPanel);
                            }
                            if(tile instanceof RestartPoint){
                                String ID = xPos + "-" + yPos;
                                RestartPoint restartPoint = (RestartPoint) tile;
                                server.getRebootMap().put(ID, restartPoint);
                            }
                            if(tile instanceof CheckPoint){
                                String ID = xPos + "-" + yPos;
                                CheckPoint checkPoint = (CheckPoint) tile;
                                server.getCheckPointMap().put(ID, checkPoint);
                            }
                            if(tile instanceof EnergySpace){
                                String ID = xPos + "-" + yPos;
                                EnergySpace energySpace = (EnergySpace) tile;
                                server.getEnergySpaceMap().put(ID, energySpace);
                            }


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

                // Update the player of the server
                playedCard.activateCard(player, server.getPitMap(), server.getWallMap(), server.getPushPanelMap());
                logger.info(ANSI_GREEN + "SERVER UPDATING FINISHED" + ANSI_RESET);

                // Sends played card to all clients with id of the one playing it
                for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedBody(playerID, playedCard));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }

                String playerPos = player.getPlayerRobot().getxPosition() + "-" + player.getPlayerRobot().getyPosition();
                // Player fell into a pit or out of map
                if (server.getPitMap().get(playerPos) != null || server.playerFellOffMap(player)) {
                    for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                        JSONMessage jsonMessage = new JSONMessage("Reboot", new RebootBody(playerID));
                        clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                        clientWrapper.getWriter().flush();
                    }

                    // Choose a random restartPoint
                    Random random = new Random();
                    int randomVal = random.nextInt(server.getRebootMap().size());

                    // Get the random restartPoint's coordinates
                    String rebootPos = ((String) server.getRebootMap().keySet().toArray()[randomVal]);
                    int rebootXPos = Integer.parseInt(rebootPos.split("-")[0]);
                    int rebootYPos = Integer.parseInt(rebootPos.split("-")[1]);

                    for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                        JSONMessage jsonMessage = new JSONMessage("Movement", new MovementBody(playerID, rebootXPos, rebootYPos));
                        clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                        clientWrapper.getWriter().flush();
                    }

                    // Update server data of that player due to Reboot
                    player.getPlayerRobot().setxPosition(rebootXPos);
                    player.getPlayerRobot().setyPosition(rebootYPos);
                    player.getPlayerRobot().setLineOfSight("up");
                }

                if (server.getGearMap().get(playerPos) != null) {
                    String gearOrientation = server.getGearMap().get(playerPos).getOrientations().get(0);

                    for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                        JSONMessage jsonMessage = new JSONMessage("PlayerTurning", new PlayerTurningBody(playerID, gearOrientation));
                        clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                        clientWrapper.getWriter().flush();
                    }

                    String currLineOfSight = player.getPlayerRobot().getLineOfSight();

                    if (gearOrientation.equals("left")) {
                        switch(currLineOfSight) {
                            case "up": player.getPlayerRobot().setLineOfSight("left");
                            break;
                            case "left": player.getPlayerRobot().setLineOfSight("down");
                            break;
                            case "down": player.getPlayerRobot().setLineOfSight("right");
                            break;
                            case "right": player.getPlayerRobot().setLineOfSight("up");
                        }
                    }

                    if (gearOrientation.equals("right")) {
                        switch(currLineOfSight) {
                            case "up": player.getPlayerRobot().setLineOfSight("right");
                                break;
                            case "left": player.getPlayerRobot().setLineOfSight("up");
                                break;
                            case "down": player.getPlayerRobot().setLineOfSight("left");
                                break;
                            case "right": player.getPlayerRobot().setLineOfSight("down");
                        }
                    }
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
                    JSONMessage jsonMessage = new JSONMessage("ActivePhase", new ActivePhaseBody(PROGRAMMING_PHASE));
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
                    logger.info(ANSI_GREEN + "( HANDLESELECTEDCARD ): SET CARD null " + " FOR PLAYER " + player.getName() + " IN REGISTER " + register + ANSI_RESET);
                    logger.info(ANSI_GREEN + "( HANDLESELECTEDCARD ): DECK FOR PLAYER " + player.getName() + ": " + player.getDeckRegister().getDeck() + ANSI_RESET);
                } else {
                    logger.info(ANSI_GREEN + "( HANDLESELECTEDCARD ): SET CARD " + selectedCard.getCardName() + " FOR PLAYER " + player.getName() + " IN REGISTER " + register + ANSI_RESET);
                    logger.info(ANSI_GREEN + "( HANDLESELECTEDCARD ): DECK FOR PLAYER " + player.getName() + ": " + player.getDeckRegister().getDeck() + ANSI_RESET);
                }

                //Counts how many cards are in total selected
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
                }

                //This counter is initialised with 0 and each time someone fills all their registers it adds to it, so it is only 0 once
                //and hence the message "SelectionFinished" is only sent once
                boolean firstAllregistersFilled = server.isFirstAllRegistersFilled();

                if (selectedCardsNumber == REGISTER_CARDS_AMOUNT && firstAllregistersFilled == false) {
                    firstAllregistersFilled = true;
                    server.setFirstAllRegistersFilled(firstAllregistersFilled);

                    for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                        JSONMessage jsonMsg = new JSONMessage("SelectionFinished", new SelectionFinishedBody(player.getPlayerID()));
                        clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMsg));
                        clientWrapper.getWriter().flush();
                    }

                    // Set up Server countdown
                    int secs = 10;
                    final CountDownLatch latch = new CountDownLatch(secs);

                    // Creates a timer that can then be started to decrease the CountDownLatch each second
                    Countdown timer = new Countdown(secs, 400, 1000);
                    timer.startTimer(latch);

                    // Inform clients that timer has been started
                    for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                        //Timer started message sent after first player fills five registers
                        JSONMessage jsonMessageTimerStarted = new JSONMessage("TimerStarted", new TimerStartedBody());
                        clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessageTimerStarted));
                        clientWrapper.getWriter().flush();
                    }

                    // Stop code execution until CountDown is finished
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("TIMER IN SERVER HAS FINISHED!");
                    //TODO: Give proper information about not finished players
                    ArrayList<Integer> bla = new ArrayList<>();
                    bla.add(1337);

                    boolean timerEnded = false;
                    for (Server.ClientWrapper clientWrapper : server.getConnectedClients()) {
                        JSONMessage jsonMessage = new JSONMessage("TimerEnded", new TimerEndedBody(bla));
                        clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                        clientWrapper.getWriter().flush();

                        timerEnded = true;

                    }

                    boolean cardsYouGotNowIsSent = false;
                    //Cards you got now is sent for those whose registers have not yet been filled
                    //Additionally the remaining cards in hand are added to the discard pile
                    if (timerEnded) {
                        for (Server.ClientWrapper clientToNotify : server.getConnectedClients()) {
                            if (clientToNotify.getPlayer().getSelectedCards() < REGISTER_FIVE) {

                                Player clientPlayer = clientToNotify.getPlayer();

                                //Remove register cards from hand
                                clientPlayer.getDeckHand().getDeck().removeAll(clientPlayer.getDeckRegister().getDeck());
                                ArrayList<Card> remainingCardsInHand = clientPlayer.getDeckHand().getDeck();

                                //Cards from hand are added to discard pile
                                DeckDiscard deckDiscard = clientPlayer.getDeckDiscard();
                                deckDiscard.getDeck().addAll(remainingCardsInHand);
                                clientPlayer.setDeckDiscard(deckDiscard);

                                //now fill empty registers with rest cards of draw pile, if not enough, put discrad on draw pile and reshuffle
                                DeckDraw deckDraw = clientPlayer.getDeckDraw();
                                ArrayList<Card> playerRegister = clientPlayer.getDeckRegister().getDeck();

                                //empty registers are counted
                                int fullRegisters = 0;
                                for (Card registerCard : playerRegister) {
                                    if (registerCard != null) {
                                        fullRegisters++;
                                    }
                                }

                                //when there are less cards to draw then needed for filling, the discards are added to the draw pile and it is reshuffled
                                if ((REGISTER_FIVE - fullRegisters) > deckDraw.getDeck().size()) {
                                    deckDraw.getDeck().addAll(deckDiscard.getDeck());
                                    deckDraw.shuffleDeck();

                                    clientPlayer.setDeckDraw(deckDraw);

                                    //Shuffle coding is sent
                                    JSONMessage jsonMessageShuffle = new JSONMessage("ShuffleCoding", new ShuffleCodingBody(clientPlayer.getPlayerID()));
                                    client.getWriter().println(JSONEncoder.serializeJSON(jsonMessageShuffle));
                                }

                                //the difference of cards to a full register is saved in cardsToFillIn-Array
                                ArrayList<Card> cardsToFillInArray = new ArrayList<>();

                                int i = 0;
                                while (i < REGISTER_FIVE) {
                                    if(playerRegister.get(i) == null){
                                        //card to fill in array is added to array which is sent when yourCards is snet
                                        Card registerCard = deckDraw.getTopCard();
                                        cardsToFillInArray.add(registerCard);

                                        //update player register
                                        playerRegister.add(i, registerCard);
                                        player.getDeckRegister().getDeck().add(i, registerCard);

                                        //PlayerDraw deck is updated
                                        deckDraw.getDeck().remove(deckDraw.getTopCard());
                                        player.getDeckDraw().getDeck().remove(deckDraw.getTopCard());
                                    }

                                    i++;
                                }

                                //Missing register-cards are filled in
                                JSONMessage jsonMessageCardsYouGot = new JSONMessage("CardsYouGotNow", new CardsYouGotNowBody(cardsToFillInArray));
                                clientToNotify.getWriter().println(JSONEncoder.serializeJSON(jsonMessageCardsYouGot));
                                clientToNotify.getWriter().flush();
                            }
                        }
                        cardsYouGotNowIsSent = true;
                    }

                        //Current cards
                        ArrayList<CurrentCardsBody.ActiveCardsObject> activeCardsObjects = new ArrayList<>();

                        if(cardsYouGotNowIsSent){
                            System.out.println("You here?");
                        for (Server.ClientWrapper clientToUpdate : server.getConnectedClients()) {
                            {                        //Todo only issue if there is two players

                                Player playerToUpdate = clientToUpdate.getPlayer();

                                //active round is updated, first call from 0 to 1, else always between 1 and 5
                                int activeRound = server.getActiveRound();
                                if (activeRound == REGISTER_FIVE) {
                                    server.setActiveRound(REGISTER_ONE);
                                }
                                activeRound++;
                                server.setActiveRound(activeRound);

                                //Card and player ID are saved in activeCardObject
                                Card cardInRegister = playerToUpdate.getDeckRegister().getDeck().get(activeRound - 1);
                                System.out.println("Card in register of player " + playerToUpdate.getPlayerID() + ": " + cardInRegister);
                                //the -1 is used on activeRound because the registers start with index 0 and end with index 4

                                int playerID = playerToUpdate.getPlayerID();
                                CurrentCardsBody.ActiveCardsObject activeCardsObject = new CurrentCardsBody.ActiveCardsObject(playerID, cardInRegister);

                                //activeCardObjects is added to list of all activeCardObjects so that those can be used
                                activeCardsObjects.add(activeCardsObject);
                            }
                            JSONMessage jsonMessageCurrentCards = new JSONMessage("CurrentCards", new CurrentCardsBody(activeCardsObjects));
                            clientToUpdate.getWriter().println(JSONEncoder.serializeJSON(jsonMessageCurrentCards));
                            clientToUpdate.getWriter().flush();


                        }
                        }
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
        String cardToActivateName = playedCard.getCardName();
        String oldPosition = null;
        String cardName = playedCard.getCardName();

        String currentPosition = null;
        String newPosition = null;

        int newXPos = 0;
        int newYPos = 0;

        if (messagePlayerID == client.getPlayer().getPlayerID()) {
            // Update own robot

            int currentXPos = client.getPlayer().getPlayerRobot().getxPosition();
            int currentYPos = client.getPlayer().getPlayerRobot().getyPosition();

            currentPosition = client.getPlayer().getPlayerRobot().getxPosition() + "-" + client.getPlayer().getPlayerRobot().getyPosition();
            playedCard.activateCard(client.getPlayer(), client.getMapController().getPitMap(), client.getMapController().getWallMap(), client.getMapController().getPushPanelMap());

            // New position of own robot
            newPosition = client.getPlayer().getPlayerRobot().getxPosition() + "-" + client.getPlayer().getPlayerRobot().getyPosition();

            newXPos = client.getPlayer().getPlayerRobot().getxPosition();
            newYPos = client.getPlayer().getPlayerRobot().getyPosition();

            // Player fell of the map, so set the coordinates back to the old ones so no error occurs when moving player's robot image due to Reboot
            if (newXPos < 0 || newYPos < 0 || newXPos >= client.getMapController().getMap().size() || newYPos >= client.getMapController().getMap().get(0).size()) {
                logger.info(ANSI_GREEN + "RESET OF PLAYER COORDINATES BECAUSE PLAYER FELL OFF MAP!" + ANSI_RESET);
                client.getPlayer().getPlayerRobot().setxPosition(currentXPos);
                client.getPlayer().getPlayerRobot().setyPosition(currentYPos);
            }

            logger.info(ANSI_GREEN + "( HANDLECARDPLAYED ): CLIENT UPDATED OWN ROBOT!" + ANSI_RESET);
        } else {
            // Update OtherPlayer robot
            for (Player player : client.getOtherPlayers()) {
                if (player.getPlayerID() == messagePlayerID) {
                    int currentXPos = player.getPlayerRobot().getxPosition();
                    int currentYPos = player.getPlayerRobot().getyPosition();

                    currentPosition = player.getPlayerRobot().getxPosition() + "-" + player.getPlayerRobot().getyPosition();
                    playedCard.activateCard(player, client.getMapController().getPitMap(), client.getMapController().getWallMap(), client.getMapController().getPushPanelMap());

                    // New position of OtherPlayer's robot
                    newPosition = player.getPlayerRobot().getxPosition() + "-" + player.getPlayerRobot().getyPosition();

                    newXPos = player.getPlayerRobot().getxPosition();
                    newYPos = player.getPlayerRobot().getyPosition();

                    // Player fell of the map, so set the coordinates back to the old ones so no error occurs when moving player's robot image due to Reboot
                    if (newXPos < 0 || newYPos < 0 || newXPos >= client.getMapController().getMap().size() || newYPos >= client.getMapController().getMap().get(0).size()) {
                        logger.info(ANSI_GREEN + "RESET OF PLAYER COORDINATES BECAUSE PLAYER FELL OFF MAP!" + ANSI_RESET);
                        player.getPlayerRobot().setxPosition(currentXPos);
                        player.getPlayerRobot().setyPosition(currentYPos);
                    }

                    logger.info(ANSI_GREEN + "( HANDLECARDPLAYED ): CLIENT UPDATED OTHER ROBOT!" + ANSI_RESET);
                }
            }
        }

        //TODO check that final
        String finalCurrentPosition = currentPosition;
        String finalNewPosition = newPosition;
        int finalNewYPos = newYPos;
        int finalNewXPos = newXPos;

        int mapWidth = client.getMapController().getMap().size();
        int mapHeight = client.getMapController().getMap().get(0).size();

        Platform.runLater(() -> {
            // Update GUI
            if (cardToActivateName.equals("MoveI") || cardToActivateName.equals("MoveII") || cardToActivateName.equals("MoveIII")) {
                MapController mapController = client.getMapController();

                // Only move a robot when it's not out going of the map
                if (finalNewXPos >= 0 && finalNewYPos >= 0 && finalNewXPos < mapWidth && finalNewYPos < mapHeight) {
                    mapController.moveRobot(finalCurrentPosition, finalNewPosition);
                }

            } else if (cardToActivateName.equals("BackUp")) {
                MapController mapController = client.getMapController();

                // Only move a robot when it's not going out of the map
                if (finalNewXPos >= 0 && finalNewYPos >= 0 && finalNewXPos < mapWidth && finalNewYPos < mapHeight) {
                    mapController.moveRobot(finalCurrentPosition, finalNewPosition);
                }

            } else if (cardToActivateName.equals("TurnLeft")) {
                String turnDirection = "left";

                MapController mapController = client.getMapController();
                mapController.turnRobot(finalCurrentPosition, turnDirection);

            } else if (cardToActivateName.equals("TurnRight")) {
                String turnDirection = "right";

                MapController mapController = client.getMapController();
                mapController.turnRobot(finalCurrentPosition, turnDirection);

            } else if (cardToActivateName.equals("UTurn")) {
                MapController mapController = client.getMapController();
                for (int i = 0; i < 2; i++) {
                    mapController.turnRobot(finalCurrentPosition, "right");
                }

            } else if (cardToActivateName.equals("PowerUp")) {
                //Todo power up

            } else if (cardToActivateName.equals("Again")) {
                int register = client.getPlayer().getActivaPhase();
                Player player = client.getPlayer();

                if (register == REGISTER_ONE) {
                    //do nothing as there is no card to repeat
                } else {
                    Card cardToActivate = player.getDeckRegister().getDeck().get(register - 2);
                    //here minus 2 as the active phase goes from 1-5 but the registers go from 0-4, so you have to go back
                    //1 for the actual phase and two for the register before and three for the phase before that register

                    if (cardToActivate.getCardName().equals("Again")) {
                        if (register < REGISTER_THREE) {
                            //do nothing as for two times to use again one has to
                        } else {
                            cardToActivate = player.getDeckRegister().getDeck().get(register - 3);
                        }
                    }
                    //here the if-else block is called recursively
                    //Todo recursvie: cardToActivateName = cardToActivate.getCardName();

                }

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
            client.getPlayer().getDeckDraw().shuffleDeck();
            System.out.println(client.getPlayer().getDeckDraw().getDeck());

            client.getChatHistoryProperty().set("The game has commenced - choose a STARTING POINT while you still can, muhahaha!");

        }
        // Upgrade phase
        else if (activePhase == UPGRADE_PHASE) {
        }
        // Programming phase
        else if (activePhase == PROGRAMMING_PHASE) {
            client.getChatHistoryProperty().set("All players have chosen their starting points, it is now time to programm your robot," +
                    "but beware and be fast!");
        }
        // Activation phase
        else if (activePhase == ACTIVATION_PHASE) {
            client.getPlayer().setActivaPhase(REGISTER_ONE);
            client.getChatHistoryProperty().set("Your robot has been activated, may it survive!");
            client.getChatHistoryProperty().set("Register ONE controls now your fate!");
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
            //Client shuffles deckDraw after putting deckDiscard on top
            DeckDiscard deckDiscard = client.getPlayer().getDeckDiscard();
            DeckDraw deckDraw = client.getPlayer().getDeckDraw();
            deckDraw.getDeck().addAll(deckDiscard.getDeck());
            deckDraw.shuffleDeck();
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
            //todo: now clicking is possible until the timer starts
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

            Label timerLabel = new Label("60");
            timerLabel.setStyle("-fx-font-size: 15em");
            timerLabel.setTextFill(Color.LIGHTGRAY);

            client.getStageController().getStage().getChildren().add(timerLabel);
            client.getStageController().getStage().setHalignment(timerLabel, HPos.CENTER);

            // Set up Client countdown with animation
            int countDownTimer = 10;
            timerLabel.setText(Integer.toString(countDownTimer));

            ScaleTransition transition = new ScaleTransition(Duration.millis(100), timerLabel);
            transition.setByX(1.5f);
            transition.setByY(1.5f);
            transition.setCycleCount(4);
            transition.setAutoReverse(true);

            Timeline timer = new Timeline(
                    new KeyFrame(Duration.seconds(1), ae -> {
                        // Repeat Heartbeat animation
                        transition.play();

                        int time = Integer.parseInt(timerLabel.getText());
                        time--;

                        timerLabel.setText(Integer.toString(time));
                    }));

            // Set timer execution count
            timer.setCycleCount(countDownTimer);

            timer.play();
            transition.play();

            timer.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.out.println("TIMER IN CLIENT HAS FINISHED!");
                    client.getStageController().getStage().getChildren().remove(timerLabel);
                }
            });
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

        Player player = client.getPlayer();
        PlayerMatController playerMatController = client.getPlayerMatController();

        Platform.runLater(() -> {
            //No differentiation bewteen too slow players and finished players necessary, as all remaining cards in hand
            //are put on discard pile as follows:

            //Remove register cards from hand
            player.getDeckHand().getDeck().removeAll(player.getDeckRegister().getDeck());

            //Remaining cards in hand are added to discard pile
            ArrayList<Card> remainingCardsInHand = player.getDeckHand().getDeck();
            player.getDeckDiscard().getDeck().addAll(remainingCardsInHand);

            //Hand is emptied
            playerMatController.emptyHand();


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

        PlayerMatController playerMatController = client.getPlayerMatController();
        Player player = client.getPlayer();

        System.out.println("Cardsyougot now" + cardsYouGotNowBody.getCards());

        Platform.runLater(() -> {


            //in register missing cards from DeckDraw are filled in
            ArrayList<Integer> emptyRegisterIndexes = playerMatController.getEmptyRegisterNumbers();

            int i = 0;
            while (i < emptyRegisterIndexes.size()) {

                for (Card card : cardsYouGotNowBody.getCards()) {
                    //Player register is updated
                    player.getDeckRegister().getDeck().add(emptyRegisterIndexes.get(i), card);

                    //Gui is updated
                    Image cardImage = playerMatController.getCardImage(card, client.getPlayer().getColor());
                    playerMatController.putImageInRegister(emptyRegisterIndexes.get(i), cardImage);

                    i++;
                }
            }
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

        //The current round is set which is important for implementing again
        int activeRegister = client.getPlayer().getActivaPhase();

        Platform.runLater(() -> {
            //TODO write code here
        });
        //after every card in the current register is shown, the register is updated
        activeRegister++;
        client.getPlayer().setActivaPhase(activeRegister);
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

        int rebootXPos = movementBody.getX();
        int rebootYPos = movementBody.getY();
        int messagePlayerID = movementBody.getPlayerID();

        Platform.runLater(() -> {
        // own robot has to reboot
        if (messagePlayerID == client.getPlayer().getPlayerID()) {

            int playerXPos = client.getPlayer().getPlayerRobot().getxPosition();
            int playerYPos = client.getPlayer().getPlayerRobot().getyPosition();

            int mapWidth = client.getMapController().getMap().size();
            int mapHeight = client.getMapController().getMap().get(0).size();
            Robot playerRobot = client.getPlayer().getPlayerRobot();

            String oldPos = playerXPos + "-" + playerYPos;

            // Update client player data due to Reboot
            client.getPlayer().getPlayerRobot().setxPosition(rebootXPos);
            client.getPlayer().getPlayerRobot().setyPosition(rebootYPos);
            client.getPlayer().getPlayerRobot().setLineOfSight("up");

            String newPos = rebootXPos + "-" + rebootYPos;

            client.getMapController().rebootRobot(oldPos, newPos);

        }
        else {
            // other player's robot has to reboot
            for (Player otherPlayer : client.getOtherPlayers()) {
                if (otherPlayer.getPlayerID() == messagePlayerID) {
                    int otherPlayerXPos = otherPlayer.getPlayerRobot().getxPosition();
                    int otherPlayerYPos = otherPlayer.getPlayerRobot().getyPosition();

                    int mapWidth = client.getMapController().getMap().size();
                    int mapHeight = client.getMapController().getMap().get(0).size();
                    Robot playerRobot = otherPlayer.getPlayerRobot();

                    String oldPos = otherPlayerXPos + "-" + otherPlayerYPos;

                    otherPlayer.getPlayerRobot().setxPosition(rebootXPos);
                    otherPlayer.getPlayerRobot().setyPosition(rebootYPos);

                    String newPos = rebootXPos + "-" + rebootYPos;

                    client.getMapController().rebootRobot(oldPos, newPos);

                    // Set robot orientation to north
                    otherPlayer.getPlayerRobot().setLineOfSight("up");
                }
            }
        }
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

        int messagePlayerID = playerTurningBody.getPlayerID();
        String turnOrientation = playerTurningBody.getDirection();

        String oldPos;
        if (client.getPlayer().getPlayerID() == messagePlayerID) {
            // Own player is turning
            oldPos = client.getPlayer().getPlayerRobot().getxPosition() + "-" + client.getPlayer().getPlayerRobot().getyPosition();

            String currLineOfSight = client.getPlayer().getPlayerRobot().getLineOfSight();

            if (turnOrientation.equals("left")) {
                switch(currLineOfSight) {
                    case "up":
                        client.getPlayer().getPlayerRobot().setLineOfSight("left");
                        break;
                    case "left":
                        client.getPlayer().getPlayerRobot().setLineOfSight("down");
                        break;
                    case "down":
                        client.getPlayer().getPlayerRobot().setLineOfSight("right");
                        break;
                    case "right":
                        client.getPlayer().getPlayerRobot().setLineOfSight("up");
                }
            }

            if (turnOrientation.equals("right")) {
                switch(currLineOfSight) {
                    case "up":
                        client.getPlayer().getPlayerRobot().setLineOfSight("right");
                        break;
                    case "left":
                        client.getPlayer().getPlayerRobot().setLineOfSight("up");
                        break;
                    case "down":
                        client.getPlayer().getPlayerRobot().setLineOfSight("left");
                        break;
                    case "right":
                        client.getPlayer().getPlayerRobot().setLineOfSight("down");
                        break;
                }
            }

            // TODO: Check why this is needed (Why is handlePlayerTurning faster than handleCardPlayed??)

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            client.getMapController().turnRobot(oldPos, turnOrientation);
            // TODO: Update orientation with switch block
        } else {
            // Other player is turning
            for (Player otherPlayer : client.getOtherPlayers()) {
                if (otherPlayer.getPlayerID() == messagePlayerID) {
                    oldPos = otherPlayer.getPlayerRobot().getxPosition() + "-" + otherPlayer.getPlayerRobot().getyPosition();

                    String currLineOfSight = otherPlayer.getPlayerRobot().getLineOfSight();

                    if (turnOrientation.equals("left")) {
                        switch(currLineOfSight) {
                            case "up":
                                otherPlayer.getPlayerRobot().setLineOfSight("left");
                                break;
                            case "left":
                                otherPlayer.getPlayerRobot().setLineOfSight("down");
                                break;
                            case "down":
                                otherPlayer.getPlayerRobot().setLineOfSight("right");
                                break;
                            case "right":
                                otherPlayer.getPlayerRobot().setLineOfSight("up");
                        }
                    }

                    if (turnOrientation.equals("right")) {
                        switch(currLineOfSight) {
                            case "up":
                                otherPlayer.getPlayerRobot().setLineOfSight("right");
                                break;
                            case "left":
                                otherPlayer.getPlayerRobot().setLineOfSight("up");
                                break;
                            case "down":
                                otherPlayer.getPlayerRobot().setLineOfSight("left");
                                break;
                            case "right":
                                otherPlayer.getPlayerRobot().setLineOfSight("down");
                                break;
                        }
                    }

                    // TODO: Check why this is needed (Why is handlePlayerTurning faster than handleCardPlayed??)

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    client.getMapController().turnRobot(oldPos, turnOrientation);
                    // TODO: Update orientation with switch block
                }
            }
        }

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
