package server;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import server.game.Deck;
import server.game.Game;
import server.game.Player;
import server.game.Robot;
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
    private int numberOfReadyClients = 0;
    private boolean gameIsRunning = false;
    private MessageDistributer messageDistributer = new MessageDistributer();
    private static final Logger logger = Logger.getLogger( Server.class.getName() );

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Starting server...");

        // Open socket for incoming connections, if socket already exists start aborts
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

        connectedClients = new ArrayList<>();
        boolean isAcceptingNewClients = true;

        while(isAcceptingNewClients) { // Runs forever at the moment
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

    public void setSetterPlayerID(int setterPlayerID) {
        this.setterPlayerID = setterPlayerID;
    }

    public void setCounterPlayerID(int counterPlayerID) {
        this.counterPlayerID = counterPlayerID;
    }

    public void setNumberOfReadyClients(int number) {
        this.numberOfReadyClients = number;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<String> getTakenStartingPoints() {
        return takenStartingPoints;
    }

    public void setTakenStartingPoints(ArrayList<String> takenStartingPoints) {
        this.takenStartingPoints = takenStartingPoints;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

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
 * Inner class to wrap the information from the client (socket + name)
 */
public class ClientWrapper {
    private Socket socket;
    private String name;
    private PrintWriter writer;
    private int playerID;
    private int figure;
    private boolean isReady;

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
            return playerID;
        }

        public boolean isReady() { return isReady; }

        public void setReady(boolean isReady) {
            this.isReady = isReady;
        }
    }


}


