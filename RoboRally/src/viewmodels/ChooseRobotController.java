package viewmodels;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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

    @FXML
    Label headline;
    @FXML
    Label smashBotLabel;
    @FXML
    Label hammerBotLabel;
    @FXML
    Label hulkX90Label;
    @FXML
    Label twonkyLabel;
    @FXML
    Label spinBotLabel;
    @FXML
    Label zoomBotLabel;

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

        // Loading font each time because loading fonts in CSS is buggy in JavaFX
        // Font can't be loaded if theres a space in path
        headline.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 40));
        smashBotLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 20));
        hammerBotLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 20));
        hulkX90Label.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 20));
        twonkyLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 20));
        spinBotLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 20));
        zoomBotLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 20));
        chooseRobot.getStylesheets().add("/resources/css/main.css");
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
