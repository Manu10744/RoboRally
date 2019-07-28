package viewmodels;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import utils.Parameter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is responsible for showing the sccoreboard when a player has won the game.
 *
 * @author Verena Sadtler
 * @author Jessica Gerlach
 */

public class ScoreboardController implements Initializable, IController {

    @FXML
    GridPane scoreboard;
    @FXML
    ImageView background;
    @FXML
    ImageView siegertreppe;
    @FXML
    ImageView firstPlace;
    @FXML
    ImageView secondPlace;
    @FXML
    ImageView thirdPlace;
    @FXML
    ImageView fourthPlace;
    @FXML
    ImageView fifthPlace;
    @FXML
    ImageView sixthPlace;
    @FXML
    Label headlineScore;
    @FXML
    Label fourthPlaceLabel;
    @FXML
    Label fifthPlaceLabel;
    @FXML
    Label sixthPlaceLabel;


    private StageController stageController;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_RATIO_WIDTH));
        background.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_RATIO_HEIGHT));
        siegertreppe.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_SIEGERTREPPE_RATIO_WIDTH));
        siegertreppe.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_SIEGERTREPPE_RATIO_HEIGHT));

        thirdPlace.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_WIDTH));
        thirdPlace.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_HEIGHT));
        firstPlace.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_WIDTH));
        firstPlace.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_HEIGHT));
        secondPlace.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_WIDTH));
        secondPlace.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_HEIGHT));

        fourthPlace.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_LOSER_RATIO_WIDTH));
        fourthPlace.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_LOSER_RATIO_HEIGHT));
        fifthPlace.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_LOSER_RATIO_WIDTH));
        fifthPlace.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_LOSER_RATIO_HEIGHT));
        sixthPlace.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_LOSER_RATIO_WIDTH));
        sixthPlace.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_LOSER_RATIO_HEIGHT));

        // Loading font each time because loading fonts in CSS is buggy in JavaFX
        // Font can't be loaded if theres a space in path
        headlineScore.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 40));
        fourthPlaceLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 30));
        fifthPlaceLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 30));
        sixthPlaceLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 30));
    }

    public ImageView getFirstPlace() {
        return firstPlace;
    }

    public ImageView getSecondPlace() {
        return secondPlace;
    }

    public ImageView getThirdPlace() {
        return thirdPlace;
    }

    public ImageView getFourthPlace() {
        return fourthPlace;
    }

    public ImageView getFifthPlace() {
        return fifthPlace;
    }

    public ImageView getSixthPlace() {
        return sixthPlace;
    }

    public Image getRobotImageforScore(int figure) {
        Image avatar;
        if (figure == 1) {
            avatar = new Image("/resources/images/robots/choose-robot-hammerbot.png");
            return avatar;
        } else if (figure == 2) {
            avatar = new Image("/resources/images/robots/choose-robot-hulkX90.png");
            return avatar;
        } else if (figure == 3) {
            avatar = new Image("/resources/images/robots/choose-robot-smashbot.png");
            return avatar;
        } else if (figure == 4) {
            avatar = new Image("/resources/images/robots/choose-robot-spinbot.png");
            return avatar;
        } else if (figure == 5) {
            avatar = new Image("/resources/images/robots/choose-robot-twonky.png");
            return avatar;
        } else {
            avatar = new Image("/resources/images/robots/choose-robot-zoombot.png");
            return avatar;
        }
    }

    @Override
    public IController setPrimaryController(StageController stageController) {
        this.stageController = stageController;
        return this;
    }
}
