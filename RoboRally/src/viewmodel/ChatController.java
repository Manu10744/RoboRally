package src.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * This class has full control over the chat view. It is responsible for
 * providing the ability to connect to a server, chat with other clients and
 * to initialize, join and start a game. Moreover, the controller can open
 * the game wiki when needed.
 *
 * @author Manuel Neumayer
 */
public class ChatController {
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldServer;
    @FXML
    private TextArea chatOutput;
    @FXML
    private TextArea chatInput;
    @FXML
    private Button buttonWiki;
    @FXML
    private Button buttonInit;
    @FXML
    private Button buttonJoin;
    @FXML
    private Button buttonStart;

    /**
     * This method is responsible for connecting the client to the specified server.
     * It uses the {@link fieldServer} to get the IP and Port.
     */
    public void connect() {

    }

    /**
     * This method is responsible for sending ordinary messages to the server.
     * The server is going to process the messages based on whether it is
     * a private or ordinary message.
     * It uses the {@link @FXML chatInput} to get the message content.
     */
    public void sendMessage() {

    }

    /**
     * This method is responsible for sending private messages to the server.
     * The server is going to process the messages based on whether it is
     * a private or ordinary message.
     * It uses the {@link @FXML chatInput} to get the message content.
     */
    public void sendPrivateMessage() {

    }

    /** This method is responsible for initializing a game. It is triggered by
     * clicking on the {@link buttonInit} button.
     */
    public void init() {

    }

    /**
     * This method is responsible for joining a game. It is triggered by
     * clicking on the {@link buttonJoin} button.
     */
    public void join() {

    }

    /**
     * This method is responsible for starting a game. It is triggered by
     * clicking on the {@link buttonStart} button. <b>Note:</b> A game can
     * only be started when at least 2 players have joined.
     */
    public void start() {

    }
}
