package viewmodels;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import server.game.Tiles.Antenna;
import server.game.Tiles.*;
import utils.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * The class MapController notifies the Server where each robot is positioned and and on which tile(type) it rests
 */
public class MapController implements Initializable {

    @FXML
    private GridPane map;

    private static final Logger logger = Logger.getLogger(viewmodels.MapController.class.getName());

    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("map started, GridPane initialised");

        logger.info("Alignment for map filing and width set");
        map.setGridLinesVisible(true);
        map.autosize();

        ArrayList<Image> tileImages = new ArrayList<>();
        logger.info("ArrayList for all the images within the dizzyHighway map initialised");

        ArrayList<ImageView> imageViews = new ArrayList<>();
        logger.info("ArrayList for ImageView containing images from map initialised");

        logger.info("Test: One tile created and as image saved");
        Image image = Tile.getTileImageView(new Pit());

        int col = 0;
        int row = 0;
        ImageView imageView;
        for (int i = 0; i < Parameter.DIZZY_HIGHWAY_HEIGHT * Parameter.DIZZY_HIGHWAY_WIDTH; i++) {
            //130 tiles are in dizzyHighway, 0 - 129 places in array have to be filled
            imageView = new ImageView(image);
            imageView.fitWidthProperty().bind(map.widthProperty().divide(Parameter.DIZZY_HIGHWAY_WIDTH));
            imageView.fitHeightProperty().bind(map.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
            imageView.setPreserveRatio(true);

            map.setConstraints(imageView, col, row);
            col++;
            map.getChildren().add(i, imageView);
            System.out.println(map.getChildren().get(i) + " LENGTH: " + map.getChildren().size() + " ROW: " + row + " COL: " + col);
            if (col >= Parameter.DIZZY_HIGHWAY_WIDTH) {
                map.addRow(row);
                row++;
                col = 0;
            }
        }
    }

    /**
     * This method controls robotlaser in activation phase
     */
    public void robotLaser() {

    }
}