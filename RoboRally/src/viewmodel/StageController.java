package viewmodel;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class StageController {

/**
 * This controller class is responsible for the board and the chat.
 * GridPane 'stage' adapts automatically to the users' screen setup.
 * Stage GUI and its elements are fully responsive.
 * It loads furthermore the initial start screen and the select robot screen.
 *
 * @author Ivan Dovecar
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
}
