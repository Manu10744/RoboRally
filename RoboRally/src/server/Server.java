package server;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import server.game.*;
import server.game.ProgrammingCards.*;
import server.game.Tiles.*;
import server.game.decks.DeckDiscard;
import server.game.decks.DeckDraw;
import server.game.decks.DeckHand;
import server.game.decks.DeckRegister;
import utils.Parameter;
import utils.json.JSONDecoder;
import utils.json.JSONEncoder;
import utils.json.MessageDistributer;
import utils.json.protocol.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private ArrayList<String> takenStartingPoints = new ArrayList<>();

    private String protocolVersion = "Version 0.1";

    private int counterPlayerID = 1;
    private int setterPlayerID;
    private int numberOfReadyClients;
    private int antennaXPos;
    private int antennaYPos;
    private int setStartPoints = 0;
    private int numOfRegistersFilled = 0;
    private int activeRound;
    private int cardsPlayed;
    private int mapHeight;
    private int mapWidth;
    private boolean cheatsActivated = false;
    private boolean firstAllRegistersFilled = false;

    private MessageDistributer messageDistributer = new MessageDistributer();
    private String gamePhase;
    private ArrayList<ArrayList<ArrayList<Tile>>> map;
    private Tile antenna;

    private Map<String, Wall> wallMap = new HashMap<>();
    private Map<String, Pit> pitMap = new HashMap<>();
    private Map<String, Gear> gearMap = new HashMap<>();
    private Map<String, Laser> laserMap = new HashMap<>();
    private Map<String, PushPanel> pushPanelMap = new HashMap<>();
    private Map<String, RestartPoint> rebootMap = new HashMap<>();
    private Map<String, CheckPoint> checkPointMap = new HashMap<>();
    private Map<String, EnergySpace> energySpaceMap = new HashMap<>();
    private Map<String, Belt> greenBeltMap = new HashMap<>();
    private Map<String, Belt> blueBeltMap = new HashMap<>();
    private Map<String, RotatingBelt> greenRotatingBeltMap = new HashMap<>();
    private Map<String, RotatingBelt> blueRotatingBeltMap = new HashMap<>();

    private static final Logger logger = Logger.getLogger(Server.class.getName());
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Starting server...");

        // Open socket for incoming connections, if socket already exists start aborts
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

        connectedClients = new ArrayList<>();
        boolean isAcceptingNewClients = true;

        while (isAcceptingNewClients) { // Runs forever at the moment
            logger.info("Waiting for new client...");
            //New client connects: (accept() waits for new client)
            Socket clientSocket = serverSocket.accept();
            logger.info("Client connected from: " + clientSocket.getInetAddress().getHostAddress());

            //ServerReaderTask that reads incoming messages from clients -> Every client has its own Task/Thread
            ServerReaderTask task = new ServerReaderTask(clientSocket, this);
            Thread clientHandlerThread = new Thread(task);
            clientHandlerThread.start();
            System.out.println("THREAD CREATED!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }

        //Server shuts down:
        serverSocket.close();
        logger.info("Server shut down.");


    }

    public boolean playerFellOffMap(Player player) {
        int playerXPos = player.getPlayerRobot().getxPosition();
        int playerYPos = player.getPlayerRobot().getyPosition();
        int mapHeight = this.map.get(0).size();
        int mapWidth = this.map.size();

        // Player fell off the map border
        if (playerXPos < 0 || playerYPos < 0 || playerXPos >= mapWidth || playerYPos >= mapHeight) {
            return true;
        }
        // Player fell into a gap
        else if (this.map.get(playerXPos).get(playerYPos).get(0) == null) {
            return true;
        } else {
            return false;
        }
    }

    public void activateGears() {
        logger.info(ANSI_GREEN + "( SERVER ): ACTIVATING GEARS!" + ANSI_RESET);

        for (ClientWrapper client : this.getConnectedClients()) {
            Player player = client.getPlayer();
            int playerID = player.getPlayerID();

            int xPos = player.getPlayerRobot().getxPosition();
            int yPos = player.getPlayerRobot().getyPosition();

            // Key for HashMap
            String playerPosition = xPos + "-" + yPos;

            // Landed on a gear
            if (this.gearMap.get(playerPosition) != null) {
                logger.info(ANSI_GREEN + "PLAYER " + player.getName() + " LANDED ON A GEAR!" + ANSI_RESET);
                // Orientation of Gear
                String turnDirection = this.gearMap.get(playerPosition).getOrientations().get(0);

                // Update player data in server
                if (turnDirection.equals("left")) {
                    switch (player.getPlayerRobot().getLineOfSight()) {
                        case "up":
                            player.getPlayerRobot().setLineOfSight("left");
                            break;
                        case "left":
                            player.getPlayerRobot().setLineOfSight("down");
                            break;
                        case "down":
                            player.getPlayerRobot().setLineOfSight("right");
                            break;
                        case "right":
                            player.getPlayerRobot().setLineOfSight("up");
                    }
                }

                if (turnDirection.equals("right")) {
                    switch (player.getPlayerRobot().getLineOfSight()) {
                        case "up":
                            player.getPlayerRobot().setLineOfSight("right");
                            break;
                        case "left":
                            player.getPlayerRobot().setLineOfSight("up");
                            break;
                        case "down":
                            player.getPlayerRobot().setLineOfSight("left");
                            break;
                        case "right":
                            player.getPlayerRobot().setLineOfSight("down");
                    }
                }

                System.out.println(player.getPlayerRobot().getLineOfSight());
                // Clients update themselves
                for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("PlayerTurning", new PlayerTurningBody(playerID, turnDirection));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }
            }
        }
    }

    public void activateCheckPoints() {
        logger.info(ANSI_GREEN + "ACTIVATING CHECKPOINTS" + ANSI_RESET);

        for (ClientWrapper client: this.getConnectedClients()) {
            Player player = client.getPlayer();
            int playerID = player.getPlayerID();

            int x = player.getPlayerRobot().getxPosition();
            int y = player.getPlayerRobot().getyPosition();

            String pos = x + "-" + y;

            System.out.println(pos);
            System.out.println(this.checkPointMap.get(pos));
            if (this.checkPointMap.get(pos) != null) {
                logger.info(ANSI_GREEN + "LANDED ON A CHECKPOINT!!!" + ANSI_RESET);
            }
        }
    }
    public void activateBelts() {
        logger.info(ANSI_GREEN + "( SERVER ): ACTIVATING BELTS!" + ANSI_RESET);
        for (ClientWrapper client : this.getConnectedClients()) {
            Player player = client.getPlayer();
            int playerID = player.getPlayerID();

            int currentXPos = player.getPlayerRobot().getxPosition();
            int currentYPos = player.getPlayerRobot().getyPosition();

            // Key for HashMap
            String playerPosition = currentXPos + "-" + currentYPos;
            if (this.greenBeltMap.get(playerPosition) != null) {
                logger.info(ANSI_GREEN + "PLAYER " + player.getName() + " LANDED ON A GREEN BELT!" + ANSI_RESET);

                // orientation of the belt
                String moveDirection = this.greenBeltMap.get(playerPosition).getOrientations().get(0);

                switch (moveDirection) {
                    case "up":
                        player.getPlayerRobot().setyPosition(currentYPos + 1);
                        break;
                    case "down":
                        player.getPlayerRobot().setyPosition(currentYPos - 1);
                        break;
                    case "left":
                        player.getPlayerRobot().setxPosition(currentXPos - 1);
                        break;
                    case "right":
                        player.getPlayerRobot().setxPosition(currentXPos + 1);
                        break;
                }
            }
            if (this.blueBeltMap.get(playerPosition) != null) {
                logger.info(ANSI_GREEN + "PLAYER " + player.getName() + " LANDED ON A BLUE BELT!" + ANSI_RESET);

                // orientation of the belt
                String moveDirection = this.blueBeltMap.get(playerPosition).getOrientations().get(0);

                switch (moveDirection) {
                    case "up":
                        player.getPlayerRobot().setyPosition(currentYPos + 2);
                        break;
                    case "down":
                        player.getPlayerRobot().setyPosition(currentYPos - 2);
                        break;
                    case "left":
                        player.getPlayerRobot().setxPosition(currentXPos - 2);
                        break;
                    case "right":
                        player.getPlayerRobot().setxPosition(currentXPos + 2);
                        break;
                }
            }
            int newXPos = player.getPlayerRobot().getxPosition();
            int newYPos = player.getPlayerRobot().getyPosition();
            logger.info(ANSI_GREEN + "NEW ROBOT POSITION: ( " + player.getPlayerRobot().getxPosition() + " | " +
                    player.getPlayerRobot().getyPosition() + " )" + ANSI_RESET);

            for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                JSONMessage jsonMessage = new JSONMessage("Movement", new MovementBody(playerID, newXPos, newYPos));
                clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                clientWrapper.getWriter().flush();
            }

        }
    }

    public void activateRotatingBelts() {
        logger.info(ANSI_GREEN + "( SERVER ): ACTIVATING ROTATINGBELTS!" + ANSI_RESET);
        for (ClientWrapper client : this.getConnectedClients()) {
            Player player = client.getPlayer();
            int playerID = player.getPlayerID();

            int currentXPos = player.getPlayerRobot().getxPosition();
            int currentYPos = player.getPlayerRobot().getyPosition();

            // Key for HashMap
            String playerPosition = currentXPos + "-" + currentYPos;
            if (this.greenRotatingBeltMap.get(playerPosition) != null) {
                logger.info(ANSI_GREEN + "PLAYER " + player.getName() + " LANDED ON A GREEN ROTATINGBELT!" + ANSI_RESET);

                // orientation of the belt
                String firstOrientation = this.greenRotatingBeltMap.get(playerPosition).getOrientations().get(0);
                String secondOrientation = this.greenRotatingBeltMap.get(playerPosition).getOrientations().get(1);

                if (this.greenRotatingBeltMap.get(playerPosition).getCrossing() == false) {
                    switch (firstOrientation) {
                        case "up":
                            if (secondOrientation == "right") {
                                player.getPlayerRobot().setxPosition(currentXPos + 1);
                                player.getPlayerRobot().setLineOfSight("right");
                            } else {
                                player.getPlayerRobot().setxPosition(currentXPos - 1);
                                player.getPlayerRobot().setLineOfSight("left");
                            }
                        case "down":
                            if (secondOrientation == "right") {
                                player.getPlayerRobot().setxPosition(currentXPos + 1);
                                player.getPlayerRobot().setLineOfSight("right");
                            } else {
                                player.getPlayerRobot().setxPosition(currentXPos - 1);
                                player.getPlayerRobot().setLineOfSight("left");
                            }
                        case "left":
                            if (secondOrientation == "up") {
                                player.getPlayerRobot().setyPosition(currentYPos + 1);
                                player.getPlayerRobot().setLineOfSight("up");
                            } else {
                                player.getPlayerRobot().setyPosition(currentYPos - 1);
                                player.getPlayerRobot().setLineOfSight("down");
                            }
                        case "right":
                            if (secondOrientation == "up") {
                                player.getPlayerRobot().setyPosition(currentYPos + 1);
                                player.getPlayerRobot().setLineOfSight("up");
                            } else {
                                player.getPlayerRobot().setyPosition(currentYPos - 1);
                                player.getPlayerRobot().setLineOfSight("down");
                            }
                    }
                }
            }
            int newXPos = player.getPlayerRobot().getxPosition();
            int newYPos = player.getPlayerRobot().getyPosition();
            logger.info(ANSI_GREEN + "NEW ROBOT POSITION: ( " + player.getPlayerRobot().getxPosition() + " | " +
                    player.getPlayerRobot().getyPosition() + " )" + ANSI_RESET);

            for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                JSONMessage jsonMessage = new JSONMessage("Movement", new MovementBody(playerID, newXPos, newYPos));
                clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                clientWrapper.getWriter().flush();
            }
        }
    }

    /**
     * This method performs the actions needed for a move cheat. Only executed when cheats on server are activated.
     * @param cheat The actual cheat (in this case 'move') plus the corresponding x - and y - coordinate that the player
     *              wants his robot to move to.
     * @param cheatingPlayer The player that activated this cheat.
     */
    public void execMoveCheat(String cheat, Player cheatingPlayer) {
        String[] cheatArgs = cheat.split(" ");
        int desiredXPos = Integer.parseInt(cheatArgs[1]);
        int desiredYPos = Integer.parseInt(cheatArgs[2]);

        logger.info("PLAYER CHEATED HIM ROBOT TO POSITION ( " + desiredXPos + " | " + desiredYPos + " )");

        if (desiredXPos >= 0 && desiredYPos >= 0 && desiredXPos < this.getMapWidth() && desiredYPos < this.getMapHeight()) {
            // Set cheated position
            cheatingPlayer.getPlayerRobot().setxPosition(desiredXPos);
            cheatingPlayer.getPlayerRobot().setyPosition(desiredYPos);

            for (ClientWrapper clientWrapper : this.connectedClients) {
                JSONMessage jsonMessage = new JSONMessage("Movement", new MovementBody(cheatingPlayer.getPlayerID(), desiredXPos, desiredYPos));
                clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                clientWrapper.getWriter().flush();
            }
        }
    }

    /**
     * This method performs the actions needed for a turn cheat. Only executed when cheats on server are activated.
     * @param cheat The actual cheat (in this case 'turn') plus the desired direction the player wants his robot to be
     *              turned.
     * @param cheatingPlayer The player that activated this cheat.
     */
    public void execTurnCheat(String cheat, Player cheatingPlayer) {
        String[] cheatArgs = cheat.split(" ");
        String turnDirection = cheatArgs[1];
        String playerLineOfSight = cheatingPlayer.getPlayerRobot().getLineOfSight();

        // Update player data in server
        if (turnDirection.equals("left")) {
            switch (playerLineOfSight) {
                case "up":
                    cheatingPlayer.getPlayerRobot().setLineOfSight("left");
                    break;
                case "left":
                    cheatingPlayer.getPlayerRobot().setLineOfSight("down");
                    break;
                case "down":
                    cheatingPlayer.getPlayerRobot().setLineOfSight("right");
                    break;
                case "right":
                    cheatingPlayer.getPlayerRobot().setLineOfSight("up");
            }
        } else if (turnDirection.equals("right")) {
            switch (playerLineOfSight) {
                case "up":
                    cheatingPlayer.getPlayerRobot().setLineOfSight("right");
                    break;
                case "left":
                    cheatingPlayer.getPlayerRobot().setLineOfSight("up");
                    break;
                case "down":
                    cheatingPlayer.getPlayerRobot().setLineOfSight("left");
                    break;
                case "right":
                    cheatingPlayer.getPlayerRobot().setLineOfSight("down");
            }
        }


        for (ClientWrapper clientWrapper : this.getConnectedClients()) {
            JSONMessage jsonMessage = new JSONMessage("PlayerTurning", new PlayerTurningBody(cheatingPlayer.getPlayerID(), turnDirection));
            clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
            clientWrapper.getWriter().flush();
        }
    }

    /**
     * This method performs the actions needed for a play card cheat. Only executed when cheats on server are activated.
     * @param cheat The actual cheat (in this case 'play') plus the desired card that the player wants to play.
     * @param cheatingPlayer The player that activated this cheat.
     */
    public void execPlayCheat(String cheat, Player cheatingPlayer) {
        String[] cheatArgs = cheat.split(" ");
        String desiredCard = cheatArgs[1];

        Robot cheatingPlayerRobot = cheatingPlayer.getPlayerRobot();
        switch (desiredCard) {
            case "move1":
                new MoveI().activateCard(cheatingPlayer, this.getPitMap(), this.getWallMap(), this.getPushPanelMap());

                for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedBody(cheatingPlayer.getPlayerID(), new MoveI()));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }
                break;

            case "move2":
                new MoveII().activateCard(cheatingPlayer, this.getPitMap(), this.getWallMap(), this.getPushPanelMap());

                for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedBody(cheatingPlayer.getPlayerID(), new MoveII()));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }
                break;

            case "move3":
                new MoveIII().activateCard(cheatingPlayer, this.getPitMap(), this.getWallMap(), this.getPushPanelMap());
                for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedBody(cheatingPlayer.getPlayerID(), new MoveIII()));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }
                break;

            case "backup":
                new BackUp().activateCard(cheatingPlayer, this.getPitMap(), this.getWallMap(), this.getPushPanelMap());

                for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedBody(cheatingPlayer.getPlayerID(), new BackUp()));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }
                break;

            case "uturn":
                new UTurn().activateCard(cheatingPlayer, this.getPitMap(), this.getWallMap(), this.getPushPanelMap());

                for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedBody(cheatingPlayer.getPlayerID(), new UTurn()));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }
                break;

            case "again":
                new Again().activateCard(cheatingPlayer, this.getPitMap(), this.getWallMap(), this.getPushPanelMap());

                for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedBody(cheatingPlayer.getPlayerID(), new Again()));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }
                break;

            case "powerup":
                new PowerUp().activateCard(cheatingPlayer, this.getPitMap(), this.getWallMap(), this.getPushPanelMap());

                for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedBody(cheatingPlayer.getPlayerID(), new PowerUp()));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }
                break;

            case "turnleft":
                new TurnLeft().activateCard(cheatingPlayer, this.getPitMap(), this.getWallMap(), this.getPushPanelMap());

                for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedBody(cheatingPlayer.getPlayerID(), new TurnLeft()));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }
                break;


            case "turnright":
                new TurnRight().activateCard(cheatingPlayer, this.getPitMap(), this.getWallMap(), this.getPushPanelMap());

                for (ClientWrapper clientWrapper : this.getConnectedClients()) {
                    JSONMessage jsonMessage = new JSONMessage("CardPlayed", new CardPlayedBody(cheatingPlayer.getPlayerID(), new TurnRight()));
                    clientWrapper.getWriter().println(JSONEncoder.serializeJSON(jsonMessage));
                    clientWrapper.getWriter().flush();
                }
                break;
        }
    }









    public void activateCheats() {
        this.cheatsActivated = true;
        logger.info("CHEATS HAVE BEEN ACTIVATED.");
    }

    public void deactivateCheats() {
        this.cheatsActivated = false;
        logger.info("CHEATS HAVE BEEN DEACTIVATED.");
    }

    public int getActiveRound() {
        return activeRound;
    }

    public void setActiveRound(int activeRound) {
        this.activeRound = activeRound;
    }

    public boolean isFirstAllRegistersFilled() {
        return firstAllRegistersFilled;
    }

    public void setFirstAllRegistersFilled(boolean firstAllRegistersFilled) {
        this.firstAllRegistersFilled = firstAllRegistersFilled;
    }

    public MessageDistributer getMessageDistributer() {
        return messageDistributer;
    }

    public ArrayList<ClientWrapper> getConnectedClients() {
        return connectedClients;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public int getCounterPlayerID() {
        return counterPlayerID;
    }

    public int getSetterPlayerID() {
        return setterPlayerID;
    }

    public int getNumberOfReadyClients() {
        return numberOfReadyClients;
    }

    public void setNumberOfReadyClients(int numberOfReadyClients) {
        this.numberOfReadyClients = numberOfReadyClients;
    }

    public void setSetterPlayerID(int setterPlayerID) {
        this.setterPlayerID = setterPlayerID;
    }

    public void setCounterPlayerID(int counterPlayerID) {
        this.counterPlayerID = counterPlayerID;
    }

    public ArrayList<String> getTakenStartingPoints() {
        return takenStartingPoints;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getSetStartPoints() {
        return setStartPoints;
    }

    public void setSetStartPoints(int setStartPoints) {
        this.setStartPoints = setStartPoints;
    }

    public void setConnectedClients(ArrayList<ClientWrapper> connectedClients) {
        this.connectedClients = connectedClients;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setTakenStartingPoints(ArrayList<String> takenStartingPoints) {
        this.takenStartingPoints = takenStartingPoints;
    }

    public String getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(String gamePhase) {
        this.gamePhase = gamePhase;
    }

    public ArrayList<ArrayList<ArrayList<Tile>>> getMap() {
        return map;
    }

    public void setMap(ArrayList<ArrayList<ArrayList<Tile>>> map) {
        this.map = map;
    }

    public int getMapHeight() { return mapHeight; }

    public void setMapHeight(int mapHeight) { this.mapHeight = mapHeight; }

    public int getMapWidth() { return mapWidth; }

    public void setMapWidth(int mapWidth) { this.mapWidth = mapWidth; }

    public Tile getAntenna() {
        return antenna;
    }

    public void setAntenna(Tile antenna) {
        this.antenna = antenna;
    }

    public int getAntennaXPos() {
        return antennaXPos;
    }

    public void setAntennaXPos(int antennaXPos) {
        this.antennaXPos = antennaXPos;
    }

    public int getAntennaYPos() {
        return antennaYPos;
    }

    public void setAntennaYPos(int antennaYPos) {
        this.antennaYPos = antennaYPos;
    }

    public Map<String, Wall> getWallMap() {
        return wallMap;
    }

    public Map<String, Pit> getPitMap() {
        return pitMap;
    }

    public Map<String, Gear> getGearMap() {
        return gearMap;
    }

    public Map<String, Laser> getLaserMap() {
        return laserMap;
    }

    public Map<String, PushPanel> getPushPanelMap() {
        return pushPanelMap;
    }

    public Map<String, RestartPoint> getRebootMap() {
        return rebootMap;
    }

    public Map<String, CheckPoint> getCheckPointMap() {
        return checkPointMap;
    }

    public Map<String, EnergySpace> getEnergySpaceMap() {
        return energySpaceMap;
    }

    public Map<String, Belt> getGreenBeltMap() {
        return greenBeltMap;
    }

    public Map<String, Belt> getBlueBeltMap() {
        return blueBeltMap;
    }

    public Map<String, RotatingBelt> getGreenRotatingBeltMap() {
        return greenRotatingBeltMap;
    }

    public Map<String, RotatingBelt> getBlueRotatingBeltMap() {
        return blueRotatingBeltMap;
    }


    public int getCardsPlayed() {
        return cardsPlayed;
    }

    public void setCardsPlayed(int cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }

    public boolean getCheatsActivated() { return cheatsActivated; }

    public class ServerReaderTask extends Thread {
        private Socket clientSocket;
        private Server server;
        JSONMessage jsonMessage;

        ServerReaderTask(Socket clientSocket, Server server) {
            this.clientSocket = clientSocket;
            this.server = server;
        }

        private PrintWriter writer;

        private BufferedReader reader;

        @Override
        public void run() {
            try {
                //WRITER:
                writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                //READER:
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //Server submits protocol version to client
                jsonMessage = new JSONMessage("HelloClient", new HelloClientBody(protocolVersion));
                writer.println(JSONEncoder.serializeJSON(jsonMessage));
                writer.flush();

                String jsonString;
                while ((jsonString = reader.readLine()) != null) {
                    // Deserialize the received JSON String into a JSON object
                    jsonMessage = JSONDecoder.deserializeJSON(jsonString);
                    logger.info("JSONDecoder in Server done: " + jsonString + jsonMessage);

                    // Cast messagebody dynamically by reflection
                    Class<?> reflection = Class.forName("utils.json.protocol." + jsonMessage.getMessageType() + "Body");
                    Object messageBodyObject = reflection.cast(jsonMessage.getMessageBody());

                    ClientMessageAction msg = (ClientMessageAction) jsonMessage.getMessageBody();
                    msg.triggerAction(this.server, this, messageBodyObject, messageDistributer);
                }
            } catch (SocketException exp) {
                if (exp.getMessage().contains("Socket closed"))
                    logger.info("Client at " + clientSocket.getInetAddress().getHostAddress() + " disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public Socket getClientSocket() {
            return clientSocket;
        }

        public Server getServer() {
            return server;
        }

        public PrintWriter getWriter() {
            return writer;
        }

        public BufferedReader getReader() {
            return reader;
        }



    }

    /**
     * Inner class to wrap the information from the client
     */
    public class ClientWrapper {
        private PrintWriter writer;
        private Socket socket;

        private Player player;
        private String name;
        private int playerID;
        private int figure;
        private boolean isReady;

        public ClientWrapper() {
        }

        public ClientWrapper(Socket socket, String name, PrintWriter writer, int figure, int playerID, boolean isReady) {
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

        public void setClientSocket(Socket clientSocket) {
            this.socket = clientSocket;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PrintWriter getWriter() {
            return writer;
        }

        public void setWriter(PrintWriter writer) {
            this.writer = writer;
        }

        public int getFigure() {
            return figure;
        }

        public void setFigure(int figure) {
            this.figure = figure;
        }

        public int getPlayerID() {
            return playerID;
        }

        public void setPlayerID(int playerID) {
            this.playerID = playerID;
        }

        public boolean isReady() {
            return isReady;
        }

        public void setReady(boolean isReady) {
            this.isReady = isReady;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }


    }
}


