package viewmodels;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import server.game.Tiles.*;
import utils.Parameter;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * The class MapController notifies the Server where each robot is positioned and and on which tile(type) it rests
 * The mapPane fits the grid and is responsive, furthermore it is zoomable (click the mapPane for requesting focus ->
 * zoom in by pressing "+" / zoom out by pressing "-") and scrollable by Mouse (scroll wheel for y-pos and SHIFT +
 * scroll wheel for x-position) or by keyboard (left by "A" / right by "D" / up by "W" / down by "S").
 *
 * @author Ivan Dovecar
 * @author Mia
 */
public class MapController implements Initializable {

    @FXML
    private GridPane mapPane;

    private static final Logger logger = Logger.getLogger(viewmodels.MapController.class.getName());

    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("mapPane started, GridPane initialised");

        logger.info("Alignment for mapPane filing and width set");
        mapPane.setGridLinesVisible(true);
        mapPane.autosize();


        mapPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                mapPane.requestFocus();
            }
        });

        mapPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (!event.isInertia()) {
                    mapPane.setTranslateX(mapPane.getTranslateX() + event.getDeltaX());
                    mapPane.setTranslateY(mapPane.getTranslateY() + event.getDeltaY());
                }
                event.consume();
            }
        });

        mapPane.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent e) -> {
            logger.info("Pressed Key: " + e);
            if (e.getCode() == KeyCode.PLUS) {
                mapPane.setScaleX(mapPane.getScaleX() * 1.1);
                mapPane.setScaleY(mapPane.getScaleY() * 1.1);
                mapPane.requestFocus();
            } else if (e.getCode() == KeyCode.MINUS) {
                mapPane.setScaleX(mapPane.getScaleX() / 1.1);
                mapPane.setScaleY(mapPane.getScaleY() / 1.1);
                mapPane.requestFocus();
            } else if (e.getCode() == KeyCode.A) {
                mapPane.setTranslateX(mapPane.getTranslateX() - 10);
                mapPane.requestFocus();
            } else if (e.getCode() == KeyCode.D) {
                mapPane.setTranslateX(mapPane.getTranslateX() + 10);
                mapPane.requestFocus();
            } else if (e.getCode() == KeyCode.W) {
                mapPane.setTranslateY(mapPane.getTranslateY() - 10);
                mapPane.requestFocus();
            } else if (e.getCode() == KeyCode.S) {
                mapPane.setTranslateY(mapPane.getTranslateY() + 10);
                mapPane.requestFocus();
            }
        });

        int col = 0;
        int row = 0;


        Image image;
        ImageView imageView;

        //130 (height * width are in dizzyHighway, 0 - 129 places in array have to be filled

        for (int i = 0; i < Parameter.DIZZY_HIGHWAY_HEIGHT * Parameter.DIZZY_HIGHWAY_WIDTH; i++) {
          //normal tile imageviews are produced
            image = (new Empty()).getTileImage();
            imageView = new ImageView(image);

            // responsiveness of the images to the gridpane is introduced
            imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(Parameter.DIZZY_HIGHWAY_WIDTH));
            imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
            imageView.setPreserveRatio(true);

            //ImageView is added to an array
            ArrayList<ImageView> imageViews = new ArrayList<>();
            imageViews.add(imageView);

            //Finally, the array with one normal tile is added to the gridpane
            mapPane.setConstraints(imageView, row, col);
            mapPane.getChildren().addAll(i, imageViews);
            col++;

            if (col >= Parameter.DIZZY_HIGHWAY_HEIGHT) {
                row++;
                col = 0;
            }
        }
    }

    /** This
     * @param tiles
     */
    public void fillMapWithImageViews(ArrayList<ArrayList<Tile>> tiles) {
        int col = 0;
        int row = 0;


        Image image;
        ImageView imageView;

        //130 (height * width are in dizzyHighway, 0 - 129 places in array have to be filled

        for (int i = 0; i < Parameter.DIZZY_HIGHWAY_HEIGHT * Parameter.DIZZY_HIGHWAY_WIDTH; i++) {

            for (ArrayList<Tile> tileArray : tiles) {
                ArrayList<ImageView> imageViewArray = new ArrayList<>();
            /*
            The Maptiles are now translated into images with the method getImageFromTile (see for its implementation Tile.class) which are in a next substep translated to imageviews as images alone
            are no nodes and can thus not be displayed in javafx
            */
                for (Tile tile : tileArray) {
                    image = tile.getTileImage();
                    imageView = new ImageView(image);

                    // Here the width and height are made responsive through them being set in relation to the width and height of the map (divide width snd height and setting the outcome as their respective ratio to the whole gidpane //
//                    imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(Parameter.DIZZY_HIGHWAY_WIDTH));
//                    imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
 //                   imageView.setPreserveRatio(true);

                    if (imageView.getImage().equals((new Empty()).getTileImage())){
                        row++;
                        //add nothing as there is already a normal tile there
                    }else {
                    /*
                    The position of the imageview-arrays in the gridpane is set as x and y coordinates, then the imageview-arrays are added to the gridpane like they would be to an arraylist
                     */
                        mapPane.setConstraints(imageView, col, row);
                        //here we do not add an arraylist as we already initialised the gridpane with them, so we add to the ones existing imageViews
                        mapPane.getChildren().add(i, imageView);
                        row++;

                        System.out.println(mapPane.getChildren().get(i) + " LENGTH: " + mapPane.getChildren().size() + " ROW: " + row + " COL: " + col);
                        if (col >= Parameter.DIZZY_HIGHWAY_WIDTH) {
                            col++;
                            row = 0;
                        }
                    }
                }
            }
        }
    }


    public GridPane getMapPane() {
        return mapPane;
    }

    /**
     * This method controls robotlaser in activation phase
     */
    public void robotLaser() {

    }
}