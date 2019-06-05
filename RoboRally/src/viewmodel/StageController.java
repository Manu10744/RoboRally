package viewmodel;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

/**
 * This class has full control over the primary Stage, which contains all GUI
 * parts (Chat, Map, Player mat, Opponent mat).
 *
 * {@link stage} Wrapper of all GUI Elements.<br>
 * {@link chatBoardBox} Box containing the Chat and Board (Board contains the map, player
 * mat and opponent mat).<br>
 * {@link board} Box containing the map, player mat and opponent mat.<br>
 * {@link opponentMat} The opponent mat section.<br>
 * {@link playerMat} The player mat section.<br>
 * {@link map} The map section.<br>
 * {@link tilePane} The actual map containing the tiles.<br>
 * {@link chat} The chat section.<br>
 *
 * @author Mia
 * @author Manuel Neumayer
 */
public class StageController {
    @FXML
    private AnchorPane stage;
    @FXML
    private HBox chatBoardBox;
    @FXML
    private VBox board;
    @FXML
    private AnchorPane opponentMat;
    @FXML
    private AnchorPane playerMat;
    @FXML
    private AnchorPane map;
    @FXML
    private TilePane tilePane;
    @FXML
    private AnchorPane chat;
}
