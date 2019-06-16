package viewmodel;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import modelclient.Client;
import utils.Parameter;
import javafx.fxml.Initializable;
import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Pattern;
import javafx.fxml.FXML;


/**
 * This class has full control over the chat view. It is responsible for providing the ability to connect to a server,
 * chat with other clients and to signal ready status to the server which starts a game when every client is ready.
 * Moreover, the controller can open the game wiki when needed.
 *
 * @author Ivan Dovecar
 */
public class ChatController implements Initializable {

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
    private Button buttonReady;

    private Client client;
    private String serverIP;
    private int serverPort;

    private Stage primaryStage;
    private String tempString;

    private StringProperty name;
    private StringProperty serverAddress;
    private BooleanProperty serverSettingFinished;
    private BooleanProperty nameSettingFinished;
    private boolean wikiWindowIsOpen;
    private boolean joinPopupIsOpen;
    private StringProperty message;
    private StringProperty clientChatOutput;

    /**
     * Initialize supervises all chat elements for action, checks user input on syntax failures and controls the
     * elements' visibility.
     *
     * @author Ivan Dovecar
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        serverAddress = new SimpleStringProperty();
        serverSettingFinished = new SimpleBooleanProperty(false);
        name = new SimpleStringProperty();
        message = new SimpleStringProperty();
        nameSettingFinished = new SimpleBooleanProperty(false);
        clientChatOutput = new SimpleStringProperty();


        //SERVERINPUT: Tooltip is shown if ip and port information are not entered proper
        Tooltip fieldServerTooltip = new Tooltip("xxx.xxx.xxx.xxx:xxxx");
        fieldServerTooltip.setAutoHide(true);

        //NAMEINPUT: Tooltip is shown name is not entered proper
        Tooltip fieldNameTooltip = new Tooltip("Space is not allowed in names. Maximum namesize is " + utils.Parameter.MAX_NAMESIZE + ".");
        fieldNameTooltip.setAutoHide(true);

        //SERVERINPUT: addListener waits for IP and port
        serverAddress.addListener(((observableValue, oldValue, newValue) -> {
            serverIP = serverAddress.get().split("\\:")[0];
            serverPort = Integer.parseInt(serverAddress.get().split("\\:")[1]);
            try {
                Socket checkConnectionSocket = new Socket(serverIP, serverPort);
                checkConnectionSocket.close();
                serverSettingFinished.set(true);
            } catch (UnknownHostException | ConnectException e) {
                showInvalidServerAddressAlert();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        //NAMEINPUT: addListener waits for name
        name.addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(utils.Parameter.INVALID_CLIENTNAME)) {
                client = new Client(name.get(), serverIP, serverPort);
                if (client.connect()) { //Connection successfull
                    nameSettingFinished.set(true);

                    clientChatOutput.bind(client.chatHistoryProperty()); // hier wird der Text durchgegeben
                } else {    //Connection not successfull due to name conflict
                    showInvalidNameAlert();
                    name.setValue(Parameter.INVALID_CLIENTNAME);
                }
            }
        });

        /* Needs to be adapated as clients are appearantly no longer adressed by
        //MESSAGEINPUT: addListener waits for Message
        message.addListener((observableValue, oldValue, newValue) -> {
            String[] partsOfMessage = newValue.split("\\s+");
            if (partsOfMessage.length == 0) {
                return;
            }
            String firstPart = partsOfMessage[0];
            if (newValue.equals("ready")) {
                client.ready();
            } else if (newValue.equals("bye")) {
                client.sayBye();
            } else if (firstPart.charAt(0) == '@') {
                if (firstPart.length() > 1) {
                    int addressedClient = Integer.parseInt(firstPart.substring(1));
                    for (String otherClient : client.activeClientsProperty()) {
                        if (addressedClient.equals(otherClient)) {
                            client.sendPrivateMessage(message.get().substring(firstPart.length()), addressedClient);
                            break;
                        }
                    }
                }
            } else {
                client.sendMessage(message.get());
            }
        });

         */

        //SERVERINPUT: Set Enter-Event
        fieldServer.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (checkIPString(fieldServer.getText())) {
                    serverAddressProperty().set(fieldServer.getText());
                    fieldServerTooltip.hide();
                } else {
                    showTooltip(primaryStage, fieldServer, fieldServerTooltip);
                }
            }
        });

        //NAMEINPUT: Set Enter-Event
        fieldName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                //Don't allow space in name, safety check:
                if (fieldName.getText().contains(" ") || fieldName.getText().equals("") || fieldName.getText().contains("\t")) {
                    showTooltip(primaryStage, fieldName, fieldNameTooltip);
                } else {
                    nameProperty().setValue(fieldName.getText());
                    fieldName.disableProperty().set(true);
                    fieldNameTooltip.hide();
                }
            }
        });

        //CHATINPUT: Set Key-Events
        chatInput.setOnKeyPressed(event -> {
            //Shift+Enter -> Wordwrap
            if (event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
                String wholeChat = chatInput.getText();
                int caretPosition = chatInput.getCaretPosition();
                chatInput.setText(wholeChat.substring(0, caretPosition));
                chatInput.appendText("\n");
                chatInput.appendText(wholeChat.substring(caretPosition));
                chatInput.positionCaret(caretPosition + 1);
            }
            //Enter -> Submit
            else if (event.getCode() == KeyCode.ENTER) {
                //Send message to server
                String chatAreaText = chatInput.getText();
                if (chatAreaText.equals(tempString)) {
                    chatAreaText += "\t";
                }
                messageProperty().setValue(formatChatMessage(chatAreaText));
                tempString = chatAreaText;
                //Reset chatArea:
                chatInput.setText("");
                chatInput.selectEnd();
            }
        });

        //Enable fieldName after server setting is finished
        serverSettingFinishedProperty().addListener(((observableValue, oldValue, newValue) -> {
            fieldServer.disableProperty().set(true);
            fieldName.disableProperty().set(false);
            fieldName.requestFocus();
        }));

        //Enable chatInput and buttons after name setting is finished
        nameSettingFinishedProperty().addListener(((observableValue, oldValue, newValue) -> {
            chatInput.disableProperty().set(false);
            chatInput.requestFocus();
            chatInput.selectEnd();
            chatOutput.textProperty().bind(clientChatOutputProperty());
            buttonReady.disableProperty().set(false);
        }));

        //NAMEINPUT: Avoid spaces in names and limit maximum namesize
        fieldName.textProperty().addListener(((observableValue, s, t1) -> {
            String currentText = fieldName.getText();
            if (currentText.length() > 0 &&
                    (currentText.charAt(currentText.length() - 1) == ' ' || currentText.length() > utils.Parameter.MAX_NAMESIZE)) {
                fieldName.setText(currentText.substring(0, currentText.length() - 1)); //delete last character
                fieldName.positionCaret(fieldName.getText().length()); //reposition caret
                showTooltip(primaryStage, fieldName, fieldNameTooltip); //show field (name) Tooltip declared above
            }
        }));

        //Autoscroll in chat output:
        clientChatOutputProperty().addListener((observable -> {
            chatOutput.setScrollTop(Double.MAX_VALUE);
        }));
    }

    private StringProperty nameProperty() {
        return name;
    }

    private StringProperty serverAddressProperty() {
        return serverAddress;
    }

    private BooleanProperty serverSettingFinishedProperty() {
        return serverSettingFinished;
    }

    private BooleanProperty nameSettingFinishedProperty() {
        return nameSettingFinished;
    }

    private StringProperty messageProperty() {
        return message;
    }

    private StringProperty clientChatOutputProperty() {
        return clientChatOutput;
    }

    //TODO This Property will control a.o. status traffic light in GUI
    private BooleanProperty gameReadyProperty() { return  client.gameReadyProperty(); }

    //TODO Check if needed
    @FXML
    private void setButtonReady(){
        messageProperty().setValue(formatChatMessage("ready"));
    }

    /**
     * Check if IP String is a valid IP Address and contains IP and Port
     *
     * @author Ivan Dovecar
     */
    private boolean checkIPString (String IP){
        if (!IP.contains(":")) {
            return false;
        } else {

            String[] partsOfIPandPort = IP.split("\\:"); // IP is divided in IP [0] and Port [1]

            String ipString = partsOfIPandPort[0];
            String portString = partsOfIPandPort[1];

            String[] partsOfIP = ipString.split("\\."); // IP is divided by ".", ideally there are now four parts


            if (partsOfIP.length != 4 || portString.length() > 4 || portString.length() == 0) { //is not an ip if there are not four parts and the port is bigger than 4 digits
                return false;
            } else {
                boolean isPort = Pattern.matches("[0-9]+", portString); //Test: Port consisits solely of numbers
                boolean isIP = false;

                for (String ipPart : partsOfIP) {
                    if ((ipPart.length() <= 3) && Pattern.matches("[0-9]+", ipPart)) { // IP adress pieces has max 3 digits
                        isIP = true;
                    } else {
                        return false;
                    }
                }
                return (isIP && isPort);
            }
        }
    }

    /**
     * Shows a Tooltip under any specified FX Control
     * @param owner
     * @param control
     * @param tooltip
     *
     * @author Ivan Dovecar
     */
    private void showTooltip (Stage owner, Control control, Tooltip tooltip) {
        Point2D p = control.localToScene(0.0, 0.0);
        tooltip.show(owner,
                p.getX(),
                p.getY() );
    }

    /**
     * Shows an alert and informs about unknown Server or invalid Name.
     *
     * @author Ivan Dovecar
     */

    // TODO Check if still necessary, after tooltip works.

    private static void showInvalidServerAddressAlert() {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unknown Server");
        alert.setHeaderText("ERROR");
        alert.setContentText("Server with this address is unreachable");
        alert.showAndWait();
    }

    private static void showInvalidNameAlert() {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Name");
        alert.setHeaderText("ERROR");
        alert.setContentText("Name already used");
        alert.showAndWait();
    }

    /**
     * Formats the chat message, removing all \n at the end of a message.
     * @param chatAreaText plain chatAreaText
     * @return formatted String
     *
     * @author Ivan Dovecar
     */
    private String formatChatMessage(String chatAreaText) {
        String result = chatAreaText;
        //Delete all wordwraps at the end of a message
        final int escape_char_length = 1;
        if(result.length() > escape_char_length) {

            while (result.substring(result.length() - escape_char_length).equals("\n")) {

                result = result.substring(0, result.length() - escape_char_length);
                if(result.length() - escape_char_length < 0) {
                    break;
                }
            }
        }
        return result;
    }
}