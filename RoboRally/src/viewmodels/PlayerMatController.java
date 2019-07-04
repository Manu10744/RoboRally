package viewmodels;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class has full control over the playermat views.
 * It is responsible for showing the player´s own icon,
 * the upgrade cards and tbe register of the player.
 *
 * @author Jessica Gerlach
 */

public class PlayerMatController implements  IController{

    @FXML
    private HBox playerIcons;
    @FXML
    private HBox playerDrawDiscardCards;
    @FXML
    private HBox playerUpdates;
    @FXML
    private HBox playerRegister;

    @FXML
    private ImageView ownRobotIcon;
    @FXML
    private ImageView emptyIcon;
    @FXML
    private ImageView clockIcon;
    @FXML
    private ImageView emptyIcon2;
    @FXML
    private ImageView emptyIcon3;
    @FXML
    private ImageView emptyIcon4;
    @FXML
    private ImageView emptyIcon5;
    @FXML
    private ImageView emptyIcon6;

    @FXML
    private ImageView emptyIcon0;
    @FXML
    private ImageView emptyIcon01;
    @FXML
    private ImageView emptyIcon02;
    @FXML
    private ImageView emptyIcon03;
    @FXML
    private ImageView emptyIcon04;
    @FXML
    private ImageView emptyIcon05;
    @FXML
    private ImageView emptyIcon06;
    @FXML
    private ImageView emptyIcon07;

    @FXML
    private ImageView permaUpdate1;
    @FXML
    private ImageView permaUpdate2;
    @FXML
    private ImageView permaUpdate3;
    @FXML
    private ImageView emptyIcon00;
    @FXML
    private ImageView tempUpdate1;
    @FXML
    private ImageView tempUpdate2;
    @FXML
    private ImageView tempUpdate3;

    @FXML
    private ImageView register1;
    @FXML
    private ImageView register2;
    @FXML
    private ImageView register3;
    @FXML
    private ImageView register4;
    @FXML
    private ImageView register5;
    @FXML
    private ImageView emptyIcon20;
    @FXML
    private ImageView emptyIcon21;


    Stage rootStage;
    @FXML
    GridPane popupCards;

    public void start(Stage stage) {
        this.rootStage = stage;

        initStage();
    }

    private void initStage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WikiController.class.getResource("/views/Wiki.fxml"));
            popupCards= loader.load();
            Scene scene = new Scene(popupCards);
            rootStage.setScene(scene);

            rootStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public IController setPrimaryController(StageController stageController) {
        return this;
    }
}
