package viewmodels;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import utils.Parameter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller class is responsible for showing the startscreen
 *
 * @author Verena Sadtler
 */

public class StartScreenController implements Initializable {
    @FXML
    GridPane startScreen;
    @FXML
    ImageView imageView;
    @FXML
    ImageView imageView2;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageView.fitWidthProperty().bind(startScreen.widthProperty().divide(Parameter.START_SCREEN_RATIO_WIDTH));
        imageView.fitHeightProperty().bind(startScreen.heightProperty().divide(Parameter.START_SCREEN_RATIO_HEIGHT));

        imageView2.setPreserveRatio(true);
        imageView2.fitWidthProperty().bind(startScreen.widthProperty().divide(Parameter.START_SCREEN_RATIO_WIDTH));
        imageView2.fitHeightProperty().bind(startScreen.heightProperty().divide(Parameter.START_SCREEN_RATIO_HEIGHT));
    }
}
