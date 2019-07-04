package viewmodels;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import utils.Parameter;

import javax.swing.event.ChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseRobotController implements Initializable,IController{

    @FXML
    GridPane chooseRobot;
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
    ImageView chooseRobotBackground;

    private StageController stageController;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        smashBot.fitWidthProperty().bind(chooseRobot.widthProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_WIDTH));
        smashBot.fitHeightProperty().bind(chooseRobot.heightProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_HEIGHT));
        hammerBot.fitWidthProperty().bind(chooseRobot.widthProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_WIDTH));
        hammerBot.fitHeightProperty().bind(chooseRobot.heightProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_HEIGHT));
        hulkX90.fitWidthProperty().bind(chooseRobot.widthProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_WIDTH));
        hulkX90.fitHeightProperty().bind(chooseRobot.heightProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_HEIGHT));
        twonky.fitWidthProperty().bind(chooseRobot.widthProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_WIDTH));
        twonky.fitHeightProperty().bind(chooseRobot.heightProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_HEIGHT));
        spinBot.fitWidthProperty().bind(chooseRobot.widthProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_WIDTH));
        spinBot.fitHeightProperty().bind(chooseRobot.heightProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_HEIGHT));
        zoomBot.fitWidthProperty().bind(chooseRobot.widthProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_WIDTH));
        zoomBot.fitHeightProperty().bind(chooseRobot.heightProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_HEIGHT));
        chooseRobotBackground.fitWidthProperty().bind(chooseRobot.widthProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_WIDTH_BGR));
        chooseRobotBackground.fitHeightProperty().bind(chooseRobot.heightProperty().divide(Parameter.CHOOSE_ROBOT_RATIO_HEIGHT_BGR));
    }


    public void mouseClicked() {
        System.out.println("CHATCONTROLLER:" + this.stageController);
        ChatController chatController = (ChatController) this.stageController.getControllerMap().get("Chat");
       hammerBot.setOnMousePressed(event ->{
           chatController.figure.setValue(1);
           chooseRobot.setVisible(false);
       });
       hulkX90.setOnMousePressed(event ->{
           chatController.figure.setValue(2);
           chooseRobot.setVisible(false);
       });
       smashBot.setOnMousePressed(event ->{
           chatController.figure.setValue(3);
           chooseRobot.setVisible(false);
       });
       twonky.setOnMousePressed(event ->{
           chatController.figure.setValue(4);
           chooseRobot.setVisible(false);
       });
       spinBot.setOnMousePressed(event ->{
           chatController.figure.setValue(5);
           chooseRobot.setVisible(false);
       });
       zoomBot.setOnMousePressed(event ->{
           chatController.figure.setValue(6);
           chooseRobot.setVisible(false);
       });
    }

    @Override
    public IController setPrimaryController(StageController stageController) {
        this.stageController = stageController;
        return this;
    }
}
