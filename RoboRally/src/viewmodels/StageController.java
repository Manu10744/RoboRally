package viewmodels;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import utils.json.MessageDistributer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StageController implements Initializable, IController {

    /**
     * This controller class is responsible for the board and the chat.
     * GridPane 'stage' adapts automatically to the users' screen setup.
     * Stage GUI and its elements are fully responsive.
     * It loads furthermore the initial start screen and the select robot screen.
     *
     * @author Ivan Dovecar
     * @author Manu
     * @author Mia
     */

    @FXML
    GridPane stage;
    @FXML
    GridPane startScreen;
    @FXML
    GridPane chooseRobot;
    @FXML
    GridPane opponentMat;
    @FXML
    GridPane map;
    @FXML
    GridPane playerMat;
    @FXML
    GridPane chat;
    @FXML
    ImageView hammerBot;
    @FXML
    ImageView hulkX90;
    @FXML
    ImageView smashBot;
    @FXML
    ImageView twonky;
    @FXML
    ImageView spinBot;
    @FXML
    ImageView zoomBot;
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

    private Map<String, IController> controllerMap = new HashMap<>();

    /**
     * If a new Controller is initialized, this method adds it then to the HasMap from which is given to the
     * {@link MessageDistributer} so that the controllers can be referenced statically there.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(mapController != null){
            controllerMap.put("Map", mapController);
        }
        if(chatController != null){
            controllerMap.put("Chat", chatController);
        }
        if(opponentMatController != null){
            controllerMap.put("OpponentMap", opponentMatController);
        }
        if(playerMatController != null){
            controllerMap.put("PlayertMat", playerMatController);
        }
        //Sents the HasMap to the MessageDistributer
        if (playerMatController != null && mapController != null && chatController != null && opponentMatController != null){
            MessageDistributer.setControllerMap(controllerMap);
        }
    }

    public void mouseClicked() {
       /*
       hammerBot.setOnMousePressed(event ->{
           figure.setValue(1);
           chooseRobot.setVisible(false);
       });
       hulkX90.setOnMousePressed(event ->{
           figure.setValue(2);
           chooseRobot.setVisible(false);
       });
       smashBot.setOnMousePressed(event ->{
           figure.setValue(3);
           chooseRobot.setVisible(false);
       });
       twonky.setOnMousePressed(event ->{
           figure.setValue(4);
           chooseRobot.setVisible(false);
       });
       spinBot.setOnMousePressed(event ->{
           figure.setValue(5);
           chooseRobot.setVisible(false);
       });
       zoomBot.setOnMousePressed(event ->{
           figure.setValue(6);
           chooseRobot.setVisible(false);
       });
       */
    }

    @Override
    public IController setPrimaryController(StageController stageController) {
        return this;
    }

}
