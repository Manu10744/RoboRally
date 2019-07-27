package viewmodels;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
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

        // Fade in effect for Logo and Background
        Timeline timeline = new Timeline();

        KeyValue transparentBackground = new KeyValue(imageView.opacityProperty(), 0.0);
        KeyValue opaqueBackground = new KeyValue(imageView.opacityProperty(), 1.0);

        KeyValue transparentLogo = new KeyValue(imageView2.opacityProperty(), 0.0);
        KeyValue opaqueLogo = new KeyValue(imageView2.opacityProperty(), 1.0);

        KeyFrame startFadeInBackground = new KeyFrame(Duration.ZERO, transparentBackground);
        KeyFrame endFadeInBackground = new KeyFrame(Duration.seconds(5), opaqueBackground);

        KeyFrame startFadeInLogo = new KeyFrame(Duration.seconds(2), transparentLogo);
        KeyFrame endFadeInLogo = new KeyFrame(Duration.seconds(6), opaqueLogo);

        timeline.getKeyFrames().addAll(startFadeInBackground, endFadeInBackground, startFadeInLogo, endFadeInLogo);
        timeline.setCycleCount(0);
        timeline.play();

        imageView.fitWidthProperty().bind(startScreen.widthProperty().divide(Parameter.START_SCREEN_RATIO_WIDTH));
        imageView.fitHeightProperty().bind(startScreen.heightProperty().divide(Parameter.START_SCREEN_RATIO_HEIGHT));

        imageView2.setPreserveRatio(true);
        imageView2.fitWidthProperty().bind(startScreen.widthProperty().divide(Parameter.START_SCREEN_RATIO_WIDTH));
        imageView2.fitHeightProperty().bind(startScreen.heightProperty().divide(Parameter.START_SCREEN_RATIO_HEIGHT));
    }
}
