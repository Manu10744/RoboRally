package viewmodels;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import utils.Parameter;

import java.net.URL;
import java.util.ResourceBundle;

public class ScoreboardController implements Initializable {

    @FXML
    GridPane scoreboard;
    @FXML
    ImageView background;
    @FXML
    ImageView siegertreppe;
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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_RATIO_WIDTH));
        background.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_RATIO_HEIGHT));
        siegertreppe.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_SIEGERTREPPE_RATIO_WIDTH));
        siegertreppe.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_SIEGERTREPPE_RATIO_HEIGHT));

        smashBot.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_WIDTH));
        smashBot.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_HEIGHT));
        hammerBot.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_WIDTH));
        hammerBot.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_HEIGHT));
        hulkX90.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_WIDTH));
        hulkX90.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_HEIGHT));
        twonky.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_WIDTH));
        twonky.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_HEIGHT));
        spinBot.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_WIDTH));
        spinBot.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_HEIGHT));
        zoomBot.fitWidthProperty().bind(scoreboard.widthProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_WIDTH));
        zoomBot.fitHeightProperty().bind(scoreboard.heightProperty().divide(Parameter.SCOREBOARD_ROBOT_RATIO_HEIGHT));
    }

}
