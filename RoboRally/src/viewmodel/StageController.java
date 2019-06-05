package viewmodel;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The Class StageController contains all different views (
 * @link ChatController,
 * @link MapController,
 * @link OpponentMapController
 * @link PlayerMatController
 *
 * */
public class StageController {

    //Todo Javadoc

    @FXML
    HBox chatBoardBox;

    @FXML
    AnchorPane board;
    @FXML
    VBox board;
    @FXML
    AnchorPane opponentMat;
    @FXML
    AnchorPane map;
    @FXML
    AnchorPane playerMat;

    @FXML
    AnchorPane chat;


}
