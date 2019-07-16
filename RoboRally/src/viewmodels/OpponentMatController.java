package viewmodels;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;

/**
 * This class has full control over the opponentMat views.
 * It is responsible for showing the playerHand in the register of each player,
 * the update playerHand of each player and the icons of the players.
 *
 * @author Jessica Gerlach
 * @author Ivan Dovecar
 */

public class OpponentMatController implements  IController{

    @FXML
    private GridPane opponentMat;
    @FXML
    private HBox player1Register;
    @FXML
    private HBox player2Register;
    @FXML
    private HBox player3Register;
    @FXML
    private HBox player4Register;
    @FXML
    private HBox player5Register;

    @FXML
    private HBox player1Updates;
    @FXML
    private HBox player2Updates;
    @FXML
    private HBox player3Updates;
    @FXML
    private HBox player4Updates;
    @FXML
    private HBox player5Updates;

    @FXML
    private ImageView player1Icon;
    @FXML
    private ImageView player1Reg1;
    @FXML
    private ImageView player1Reg2;
    @FXML
    private ImageView player1Reg3;
    @FXML
    private ImageView player1Reg4;
    @FXML
    private ImageView player1Reg5;

    @FXML
    private ImageView player1TempUpdates1;
    @FXML
    private ImageView player1TempUpdates2;
    @FXML
    private ImageView player1TempUpdates3;
    @FXML
    private ImageView player1PermUpdates1;
    @FXML
    private ImageView player1PermUpdates2;
    @FXML
    private ImageView player1PermUpdates3;

    @FXML
    private ImageView player2Icon;
    @FXML
    private ImageView player2Reg1;
    @FXML
    private ImageView player2Reg2;
    @FXML
    private ImageView player2Reg3;
    @FXML
    private ImageView player2Reg4;
    @FXML
    private ImageView player2Reg5;

    @FXML
    private ImageView player2TempUpdates1;
    @FXML
    private ImageView player2TempUpdates2;
    @FXML
    private ImageView player2TempUpdates3;
    @FXML
    private ImageView player2PermUpdates1;
    @FXML
    private ImageView player2PermUpdates2;
    @FXML
    private ImageView player2PermUpdates3;

    @FXML
    private ImageView player3Icon;
    @FXML
    private ImageView player3Reg1;
    @FXML
    private ImageView player3Reg2;
    @FXML
    private ImageView player3Reg3;
    @FXML
    private ImageView player3Reg4;
    @FXML
    private ImageView player3Reg5;

    @FXML
    private ImageView player3TempUpdates1;
    @FXML
    private ImageView player3TempUpdates2;
    @FXML
    private ImageView player3TempUpdates3;
    @FXML
    private ImageView player3PermUpdates1;
    @FXML
    private ImageView player3PermUpdates2;
    @FXML
    private ImageView player3PermUpdates3;

    @FXML
    private ImageView player4Icon;
    @FXML
    private ImageView player4Reg1;
    @FXML
    private ImageView player4Reg2;
    @FXML
    private ImageView player4Reg3;
    @FXML
    private ImageView player4Reg4;
    @FXML
    private ImageView player4Reg5;

    @FXML
    private ImageView player4TempUpdates1;
    @FXML
    private ImageView player4TempUpdates2;
    @FXML
    private ImageView player4TempUpdates3;
    @FXML
    private ImageView player4PermUpdates1;
    @FXML
    private ImageView player4PermUpdates2;
    @FXML
    private ImageView player4PermUpdates3;

    @FXML
    private ImageView player5Icon;
    @FXML
    private ImageView player5Reg1;
    @FXML
    private ImageView player5Reg2;
    @FXML
    private ImageView player5Reg3;
    @FXML
    private ImageView player5Reg4;
    @FXML
    private ImageView player5Reg5;

    @FXML
    private ImageView player5TempUpdates1;
    @FXML
    private ImageView player5TempUpdates2;
    @FXML
    private ImageView player5TempUpdates3;
    @FXML
    private ImageView player5PermUpdates1;
    @FXML
    private ImageView player5PermUpdates2;
    @FXML
    private ImageView player5PermUpdates3;



    /* TODO
        ************************************************************************
        * Here is an example code to make the ImageViews fully responsive!!    *
        * Just adapt the necessary values:                                     *
        *   - image ID  -> here: player1Icon                                   *
        *   - HBox ID   -> here: player1Register                               *
        * The image views' height and width are bind to the HBox which         *
        * contains the image views and thereby to its' Values!! Enjoy, Ivan    *
        ************************************************************************
        public ImageView getPlayer1Icon() {
            player1Icon.setPreserveRatio(true);
            player1Icon.fitWidthProperty().bind(player1Register.widthProperty());
            player1Icon.fitHeightProperty().bind(player1Register.heightProperty());
            return player1Icon;
         }
     */

    @Override
    public IController setPrimaryController(StageController stageController) {
        return this;
    }


}
