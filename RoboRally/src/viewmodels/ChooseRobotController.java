package viewmodels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
    Label heading;
    @FXML
    VBox vBox1;



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
