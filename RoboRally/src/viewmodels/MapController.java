package viewmodels;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
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
 * The map fits the grid and is responsive, furthermore it is zoomable (click the map for requesting focus ->
 * zoom in by pressing "+" / zoom out by pressing "-") and scrollable by Mouse (scroll wheel for y-pos and SHIFT +
 * scroll wheel for x-position) or by keyboard (left by "A" / right by "D" / up by "W" / down by "S").
 *
 * @author Ivan Dovecar
 * @author Mia
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


        map.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent mouseEvent)
            {
                map.requestFocus();
            }
        });

        map.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (!event.isInertia()) {
                    map.setTranslateX(map.getTranslateX() + event.getDeltaX());
                    map.setTranslateY(map.getTranslateY() + event.getDeltaY());
                }
                event.consume();
            }
        });

        map.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent e) -> {
            logger.info("Pressed Key: " + e);
            if (e.getCode() == KeyCode.PLUS) {
                map.setScaleX(map.getScaleX() * 1.1);
                map.setScaleY(map.getScaleY() * 1.1);
                map.requestFocus();
            }
            else if(e.getCode() == KeyCode.MINUS ){
                map.setScaleX(map.getScaleX() / 1.1);
                map.setScaleY(map.getScaleY() / 1.1);
                map.requestFocus();
            }
            else if(e.getCode() == KeyCode.A){
                map.setTranslateX(map.getTranslateX() - 10);
                map.requestFocus();
            }
            else if(e.getCode() == KeyCode.D){
                map.setTranslateX(map.getTranslateX() + 10);
                map.requestFocus();
            }
            else if(e.getCode() == KeyCode.W){
                map.setTranslateY(map.getTranslateY() - 10);
                map.requestFocus();
            }
            else if(e.getCode() == KeyCode.S){
                map.setTranslateY(map.getTranslateY() + 10);
                map.requestFocus();
            }
        });


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