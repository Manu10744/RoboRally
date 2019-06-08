package modelclient;

import java.util.logging.Logger;

import utils.instructions.Instruction;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class implements the clients. <br>
 * All clients connect to one server.
 *
 * @author Ivan Dovecar
 */
public class Client {

    private String name;
    private Socket socket;
    private String serverIP;
    private int serverPort;
    private ObjectOutputStream writer;

    private boolean waitingForAnswer;
    private boolean nameSuccess;

    private StringProperty chatHistory;
    private ListProperty<String> activeClients;
    private ListProperty<OtherPlayer> otherActivePlayers;
    private BooleanProperty gameInitialized;
    private BooleanProperty gameJoined;
    private BooleanProperty gameStarted;
    private static final Logger logger = Logger.getLogger( Client.class.getName() );


    public Client(String name, String serverIP, int serverPort) {
        this.name = name;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        chatHistory = new SimpleStringProperty("");
        activeClients = new SimpleListProperty<>(FXCollections.observableArrayList());

        //GAME:
        otherActivePlayers = new SimpleListProperty<>(FXCollections.observableArrayList());
        gameInitialized = new SimpleBooleanProperty(false);
        gameJoined = new SimpleBooleanProperty(false);
        gameStarted = new SimpleBooleanProperty(false);
    }


    /**
     * This method is responsible for connecting the client to the specified server.
     * It uses the {link fieldServer} to get the IP and Port.
     * @return connection Success: True, connection Failed: False
     */
    public boolean connect() {
        logger.info("Starting client...");

        try {
            //Create socket to connect to server at serverIP:serverPort
            socket = new Socket(serverIP, serverPort);

            //Start new Thread, that reads incoming messages from server
            ClientReaderTask readerTask = new ClientReaderTask(socket);
            Thread readerThread = new Thread(readerTask);
            readerThread.start();

            //Send ChatInstruction "Check_Name" to server with client name
            writer = new ObjectOutputStream(socket.getOutputStream());
            writer.writeObject( new Instruction(Instruction.ClientToServerInstructionType.CHECK_NAME, name) );
            writer.flush(); // ist nötig, damit was geschickt wird

            waitingForAnswer = true;
            while(waitingForAnswer) {
                //WAIT FOR ANSWER FROM SERVER, waitingForAnswer is changed by ClientReaderTask once finished
                logger.info("WAITING...");
                if (waitingForAnswer) try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.info("Connected to server");

            return nameSuccess; //gets set by ClientReaderTask
        } catch(IOException exp) {
            exp.printStackTrace();
        }
        return false;
    }

    /**
     * This method is responsible for sending ordinary messages to the server.
     * The server is going to process the messages based on whether it is
     * a private or ordinary message.
     * It uses the {@link @FXML chatInput} to get the message content.
     */
    public void sendMessage(String message)  {
        try {
            writer.writeObject( new Instruction(Instruction.ClientToServerInstructionType.SEND_MESSAGE, message) );
            writer.flush();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending private messages to the server.
     * The server is going to process the messages based on whether it is
     * a private or ordinary message.
     * It uses the {@link @FXML chatInput} to get the message content.
     * @param message
     * @param addressedClient
     */
    public void sendPrivateMessage(String message, String addressedClient)  {
        try {
            writer.writeObject( new Instruction(Instruction.ClientToServerInstructionType.SEND_PRIVATE_MESSAGE, message, addressedClient) );
            writer.flush();
        } catch (IOException exp) {
            //TODO
            exp.printStackTrace();
        }
    }

    /** This method is responsible for initializing a game. It is triggered by
     * clicking on the {link buttonInit} button.
     */
    public void init() {
        try {
            writer.writeObject( new Instruction(Instruction.ClientToServerInstructionType.INIT_GAME, name));
            writer.flush();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }


    /**
     * This method is responsible for joining a game. It is triggered by
     * clicking on the {link buttonJoin} button.
     */
    public void join(String name, int age) {
        try {
            writer.writeObject( new Instruction(Instruction.ClientToServerInstructionType.JOIN_GAME, name, age));
            writer.flush();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    /**
     * This method is responsible for starting a game. It is triggered by
     * clicking on the {link buttonStart} button. <b>Note:</b> A game can
     * only be started when at least 2 players have joined.
     */
    public void start() {
        try {
            writer.writeObject( new Instruction(Instruction.ClientToServerInstructionType.START_GAME, name));
            writer.flush();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    /**
     * Sends "BYE" to the server and thereby quits the connection
     */
    public void sayBye() {
        try {
            writer.writeObject( new Instruction(Instruction.ClientToServerInstructionType.BYE, name) );
            writer.flush();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
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

    private void addOtherPlayer(String name) {
        otherActivePlayers.add(new OtherPlayer(name));
    }

    /**
     * Adds an client to activeClients
     * @param name of joining client
     */
    private void addActiveClient(String name) {
        activeClients.add(name);
    }

    /**
     * Remove client from list
     * @param name Name of leaving client
     */
    private void removeActiveClient(String name) {
        activeClients.remove(name);
        for(int i = 0; i < otherActivePlayers.size(); i++) {
            if(otherActivePlayers.get(i).name.equals(name)) {
                otherActivePlayers.remove(name);
                break;
            }
        }
    }



    public StringProperty chatHistoryProperty() { return chatHistory; }

    public ListProperty<OtherPlayer> otherActivePlayers() { return otherActivePlayers; }

    public String getName() {
        return name;
    }

    public BooleanProperty gameInitializedProperty() {
        return gameInitialized;
    }

    public BooleanProperty gameJoinedProperty() {
        return gameJoined;
    }

    public BooleanProperty gameStartedProperty() {
        return gameStarted;
    }

    public OtherPlayer getOtherPlayerByName(String name) {
        return otherActivePlayers.stream().filter(otherPlayer -> otherPlayer.name.get().equals(name)).findFirst().get();
    }

    public ListProperty<String> activeClientsProperty() {
        return activeClients;
    }


    /**
     * Inner class to define ReaderTask with Server
     */
    private class ClientReaderTask extends Thread {

        private Socket socket; //This Client's socket

        ClientReaderTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                //Reads input stream from server
                ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());

                Instruction chatInstruction;
                while ((chatInstruction = (Instruction) reader.readObject()) != null) {
                    Instruction.ServerToClientInstructionType serverToClientInstructionType = chatInstruction.getServerToClientInstructionType();
                    String content = chatInstruction.getContent();

                    switch (serverToClientInstructionType) {
                        case NAME_INVALID: {
                            waitingForAnswer = false;
                            nameSuccess = false;
                            break;
                        }
                        case NAME_SUCCESS: {
                            waitingForAnswer = false;
                            nameSuccess = true;
                            break;
                        }
                        case NEW_MESSAGE: {
                            /*
                            Platform.runLater(() -> ...) is necessary, because only the JavaFX Thread can manipulate the UI
                            and when a message is received, the UI is immediately updated. This way, the task is switched to the
                            JAVA FX Thread (not the current ClientReaderTask-Thread - that's my explanation at least)
                             */
                            Platform.runLater(() -> receiveMessage(content)); //Otherwise error because not JavaFX Thread
                            break;
                        }
                        case CLIENT_JOINED: {
                            Instruction finalChatInstruction = chatInstruction;
                            Platform.runLater(() -> {
                                receiveMessage(content + finalChatInstruction.getAddendum(serverToClientInstructionType));
                                addActiveClient(name);
                            });
                            break;
                        }
                        case CLIENT_WELCOME: {
                            Instruction finalChatInstruction = chatInstruction;
                            Platform.runLater(() -> {
                                receiveMessage(finalChatInstruction.getAddendum(serverToClientInstructionType) + content);
                                activeClients.add(content);
                            });
                            break;
                        }
                        case CLIENT_LEAVES: {
                            Instruction finalChatInstruction = chatInstruction;
                            Platform.runLater(() -> {
                                receiveMessage(content + finalChatInstruction.getAddendum(serverToClientInstructionType));
                                removeActiveClient(content);
                            });
                            break;
                        }
                        case CLIENT_REGISTER: {
                            Platform.runLater(() -> {
                                activeClients.add(content);
                            });
                            break;
                        }
                        case KILL_CLIENT: {
                            writer.close();
                            socket.close();
                            Platform.exit();
                            break;
                        }
                        case GAME_INIT: {
                            Instruction finalChatInstruction = chatInstruction;
                            Platform.runLater(() -> {
                                receiveMessage(content + finalChatInstruction.getAddendum(serverToClientInstructionType));
                                gameInitialized.set(true);
                            });
                            break;
                        }
                        case GAME_JOIN: {
                            Instruction finalChatInstruction = chatInstruction;
                            Platform.runLater(() -> {
                                receiveMessage(content + finalChatInstruction.getAddendum(serverToClientInstructionType));
                                if(!content.equals(name)) addOtherPlayer(content);
                            });
                            break;
                        }
                        case GAME_START: {
                            Instruction finalChatInstruction = chatInstruction;
                            Platform.runLater(() -> {
                                receiveMessage(content + finalChatInstruction.getAddendum(serverToClientInstructionType));
                                gameStarted.set(true);
                            });
                            break;
                        }
                        // FAIL CASES
                        case GAME_INIT_FAIL1:
                        case GAME_JOIN_FAIL1: case GAME_JOIN_FAIL2: case GAME_JOIN_FAIL3:
                        case GAME_START_FAIL1: case GAME_START_FAIL2: case GAME_START_FAIL3:    {
                            Instruction finalChatInstruction = chatInstruction;
                            Platform.runLater(() -> {
                                receiveMessage(content + finalChatInstruction.getAddendum(serverToClientInstructionType));
                            });
                            break;
                        }
                    }
                }
            } catch (SocketException exp) {
                if (exp.getMessage().contains("Socket closed"))
                    System.out.println("Disconnected");
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class OtherPlayer {
        StringProperty name;

        OtherPlayer(String name) {
            this.name = new SimpleStringProperty(name);
        }

        public String getName() { return name.get();}
    }
}