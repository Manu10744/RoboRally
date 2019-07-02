package viewmodels;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import client.Client;
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
import java.net.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.fxml.FXML;



/**
 * This class has full control over the chat views. It is responsible for providing the ability to connect to a server,
 * chat with other clients and to signal ready status to the server which starts a game when every client is ready.
 * Moreover, the controller can open the game wiki when needed.
 *
 * @author Ivan Dovecar
 */
public class ChatController implements Initializable, IController {

    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldFigure;
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

    private String tempString;
    private String serverIP;

    private Client client;
    private int serverPort;

    private Stage primaryStage;

    private StringProperty serverAddress;
    private StringProperty name;
    public IntegerProperty figure;

    private BooleanProperty serverSettingFinished;
    private BooleanProperty figureSettingFinished;
    private BooleanProperty nameSettingFinished;
    private BooleanProperty playerSettingFinished;
    private BooleanProperty isReadyProperty;

    private StringProperty message;
    private StringProperty clientChatOutput;
    private static final Logger logger = Logger.getLogger( ChatController.class.getName() );

    /**
     * Initialize supervises all chat elements for action, checks user input on syntax failures and controls the
     * elements' visibility.
     *
     * @author Ivan Dovecar
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        serverAddress = new SimpleStringProperty();
        serverSettingFinished = new SimpleBooleanProperty(false);
        figure = new SimpleIntegerProperty();
        figureSettingFinished = new SimpleBooleanProperty(false);
        name = new SimpleStringProperty();
        nameSettingFinished = new SimpleBooleanProperty(false);
        isReadyProperty = new SimpleBooleanProperty(false);
        message = new SimpleStringProperty();
        clientChatOutput = new SimpleStringProperty();


        //SERVERINPUT: Tooltip is shown if ip and port information are not entered proper
        Tooltip fieldServerTooltip = new Tooltip("xxx.xxx.xxx.xxx:xxxx");
        fieldServerTooltip.setAutoHide(true);

        //NAMEINPUT: Tooltip is shown name is not entered proper (ATM no use because there are no restrictions)
        Tooltip fieldNameTooltip = new Tooltip("ADD TEXT IF NECESSARY.");
        fieldNameTooltip.setAutoHide(true);

        //SERVERINPUT: addListener waits for IP and port
        serverAddress.addListener(((observableValue, oldValue, newValue) -> {
            logger.info("serverAddress addlistener is creating and closing a test-socket to check if IP and port are valid (leads to first server INFO: Client connected from: IP");
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
            client = new Client(serverIP, serverPort);
            client.connectClient();
        }));

        figure.addListener((observableValue, oldValue, newValue) -> {
            figure.setValue(newValue);
            figureSettingFinished.set(true);
        });

        //NAMEINPUT: addListener waits for name
        name.addListener((observableValue, oldValue, newValue) -> {
            clientChatOutput.bind(client.getChatHistoryProperty());
            //After name input player values (name, figure) are handed over to client
            client.sendPlayerValues(name.get(), figure.getValue().intValue());
            nameSettingFinished.set(true);
        });


        //MESSAGEINPUT: addListener waits for Message
        message.addListener((observableValue, oldValue, newValue) -> {

            String[] partsOfMessage = newValue.split("\\s+");
            String firstPart = partsOfMessage[0];

            if (partsOfMessage.length == 0) {
                return;
            } else if (firstPart.charAt(0) == '@' && firstPart.length() > 1) {
                    int addressedPlayerID = Integer.parseInt(firstPart.substring(1));
                    client.sendPrivateMessage(message.get().substring(firstPart.length()), addressedPlayerID);
                    logger.info("Private message values: " + message.get().substring(firstPart.length()) +
                            ", addressed playerID: " + addressedPlayerID);
            } else client.sendMessage(message.get());
        });

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
                    nameProperty().setValue(fieldName.getText());
                }
        });

        // FIGUREINPUT: Set Enter-Event
        fieldFigure.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                figure.set(Integer.parseInt(fieldFigure.getText()));
            }
        });

        // Inform server about ready status changes
        getReadyProperty().addListener((observableValue, oldvalue, newValue) -> {
            // If property changes, inform the server
            client.sendReadyStatus(newValue);
            logger.info("NEW READY STATUS: " + newValue);
        });

        // Change ready status by clicking the 'Ready' button
        buttonReady.setOnMouseClicked(event -> {
            boolean readyStatus = getReadyProperty().get();
            // Toggle ready status
            getReadyProperty().set(!readyStatus);

            // Button changes color according to ready status
            if (buttonReady.getStyle().equals("-fx-base: #00FF00;")) {
                buttonReady.setStyle("-fx-base: #FF0000;");
            } else buttonReady.setStyle("-fx-base: #00FF00;");
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

        //Enable fieldFigure after server setting is finished
        serverSettingFinishedProperty().addListener(((observableValue, oldValue, newValue) -> {
            fieldServer.disableProperty().set(true);
            fieldFigure.disableProperty().set(false);
            fieldFigure.requestFocus();
            //TODO enable visibility choose robot / disable visibility start
        }));

        //Enable fieldName after figure setting is finished
        figureSettingFinishedProperty().addListener(((observableValue, oldValue, newValue) -> {
            fieldFigure.disableProperty().set(true);
            fieldName.disableProperty().set(false);
            fieldName.requestFocus();
        }));

        //Enable chatInput and buttons after name setting is finished
        nameSettingFinishedProperty().addListener(((observableValue, oldValue, newValue) -> {
            fieldName.disableProperty().set(true);
            chatInput.disableProperty().set(false);
            chatInput.requestFocus();
            chatInput.selectEnd();
            chatOutput.textProperty().bind(clientChatOutputProperty());
            buttonReady.disableProperty().set(false);
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

    private BooleanProperty figureSettingFinishedProperty() {
        return figureSettingFinished;
    }

    private BooleanProperty nameSettingFinishedProperty() {
        return nameSettingFinished;
    }

    private BooleanProperty getReadyProperty() {
        return isReadyProperty;
    }

    private StringProperty messageProperty() {
        return message;
    }

    private StringProperty clientChatOutputProperty() {
        return clientChatOutput;
    }

    /**
     * Check if IP String is a valid IP Address and contains IP and Port
     *
     * @author Ivan Dovecar
     */
    private boolean checkIPString(String IP) {
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
     *
     * @param owner
     * @param control
     * @param tooltip
     * @author Ivan Dovecar
     */
    private void showTooltip(Stage owner, Control control, Tooltip tooltip) {
        Point2D p = control.localToScene(0.0, 0.0);
        tooltip.show(owner,
                p.getX(),
                p.getY());
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


    /**
     * Formats the chat message, removing all \n at the end of a message.
     *
     * @param chatAreaText plain chatAreaText
     * @return formatted String
     * @author Ivan Dovecar
     */
    private String formatChatMessage(String chatAreaText) {
        String result = chatAreaText;
        //Delete all wordwraps at the end of a message
        final int escape_char_length = 1;
        if (result.length() > escape_char_length) {

            while (result.substring(result.length() - escape_char_length).equals("\n")) {

                result = result.substring(0, result.length() - escape_char_length);
                if (result.length() - escape_char_length < 0) {
                    break;
                }
            }
        }
        return result;
    }

    @FXML
    void openWiki(ActionEvent event) throws IOException{
        Stage rootStage;
        Parent root1;

        if (event.getSource() == buttonWiki) {

            root1 = FXMLLoader.load(getClass().getResource("/views/Wiki.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.setTitle("WikiRoboRally");
            stage.show();

        }

    }

    @Override
    public IController setPrimaryController(StageController stageController) {
        return this;
    }
}