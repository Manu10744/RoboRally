package viewmodels;


import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import utils.Parameter;
import utils.json.MessageDistributer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This controller class is responsible for the board and the chat.
 * GridPane 'stage' adapts automatically to the users' screen setup.
 * Stage GUI and its elements are fully responsive.
 * It loads furthermore the initial start screen and the select robot screen.
 *
 * @author Ivan Dovecar
 * @author Manu Neumayer
 * @author Mia Brandtner
 */
public class StageController implements Initializable, IController {

    @FXML
    private GridPane stage;
    @FXML
    private GridPane startScreen;
    @FXML
    private GridPane chooseRobot;
    @FXML
    private GridPane opponentMat;
    @FXML
    private GridPane map;
    @FXML
    private GridPane playerMat;
    @FXML
    private GridPane chat;
    @FXML
    private GridPane scoreboard;
    @FXML
    private ImageView lobbyBackground;

    @FXML
    private ChatController chatController;
    @FXML
    private MapController mapController;
    @FXML
    private OpponentMatController opponentMatController;
    @FXML
    private PlayerMatController playerMatController;
    @FXML
    private WikiController wikiController;
    @FXML
    private ChooseRobotController chooseRobotController;
    @FXML
    private ScoreboardController scoreboardController;

    // Hashmap for the controller references
    private Map<String, IController> controllerMap = new HashMap<>();
    /**
     * If a new Controller is initialized, this method adds it then to the HasMap from which is given to the
     * {@link MessageDistributer} so that the controllers can be referenced statically there.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lobbyBackground.fitWidthProperty().bind(chooseRobot.widthProperty().divide(Parameter.LOBBY_RATIO_HEIGHT_BGR));
        lobbyBackground.fitHeightProperty().bind(chooseRobot.heightProperty().divide(Parameter.LOBBY_RATIO_HEIGHT_BGR));

        if (chooseRobotController != null) {
            controllerMap.put("ChooseRobot", chooseRobotController.setPrimaryController(this));
        }
        if (mapController != null) {
            controllerMap.put("Map", mapController.setPrimaryController(this));
        }
        if (chatController != null) {
            controllerMap.put("Chat", chatController.setPrimaryController(this));
        }
        if (opponentMatController != null){
            controllerMap.put("OpponentMat", opponentMatController);
        }
        if (playerMatController != null){
            controllerMap.put("PlayerMat", playerMatController.setPrimaryController(this));
        }
        if (scoreboardController != null) {
            controllerMap.put("ScoreBoard", scoreboardController.setPrimaryController(this));
        }

        // Sends the HasMap to the MessageDistributer after adding all controllers
        if (playerMatController != null && mapController != null && chatController != null && opponentMatController != null) {
            MessageDistributer messageDistributer = new MessageDistributer();
            messageDistributer.setControllerMap(controllerMap);
        }
    }

    public Map<String, IController> getControllerMap() {
        return controllerMap;
    }

    public GridPane getStartScreen() {
        return startScreen;
    }

    public GridPane getChooseRobot() {
        return chooseRobot;
    }

    public GridPane getOpponentMat() {
        return opponentMat;
    }

    public GridPane getMap() {
        return map;
    }

    public GridPane getPlayerMat() {
        return playerMat;
    }

    public GridPane getChat() {
        return chat;
    }

    public GridPane getStage() { return stage; }

    public GridPane getScoreBoard() { return scoreboard; }

    public ImageView getLobbyBackground() { return lobbyBackground; }

    @Override
    public IController setPrimaryController(StageController stageController) {
        return this;
    }

}
