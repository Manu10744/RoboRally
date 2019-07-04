package viewmodels;

import client.Client;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import server.game.Tiles.*;
import utils.Parameter;
import utils.json.protocol.GameStartedBody;
import java.util.ArrayList;
import java.util.logging.Logger;



/**
 * The class MapController notifies the Server where each robot is positioned and and on which tile(type) it rests
 * The mapPane fits the grid and is responsive, furthermore it is zoomable (click the mapPane for requesting focus ->
 * zoom in by pressing "+" / zoom out by pressing "-") and scrollable by Mouse (scroll wheel for y-pos and SHIFT +
 * scroll wheel for x-position) or by keyboard (left by "A" / right by "D" / up by "W" / down by "S").
 *
 * @author Ivan Dovecar
 * @author Mia
 * @author Manu
 */
public class MapController implements IController {

    @FXML
    private GridPane mapPane;
    private static final Logger logger = Logger.getLogger(viewmodels.MapController.class.getName());

    @Override
    public IController setPrimaryController(StageController stageController) {
        return this;
    }

    /**
     * This method fills the GridPane that's responsible for displaying the map. It is triggered inside of the
     * {@link utils.json.MessageDistributer#handleGameStarted(Client, Client.ClientReaderTask, GameStartedBody)} method.
     * @param gameStartedBody MessageBody of the 'GameStarted' protocol message containing the map information.
     */
    public void fillGridPaneWithMap(GameStartedBody gameStartedBody) {
        System.out.println("MAPPANE CHILDREN START: " + mapPane.getChildren().size());

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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

                mapPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
                    logger.info("Pressed Key: " + e);
                    if (e.getCode() == KeyCode.PLUS) {
                        mapPane.setScaleX(mapPane.getScaleX() * 1.05);
                        mapPane.setScaleY(mapPane.getScaleY() * 1.05);
                        mapPane.requestFocus();
                    } else if (e.getCode() == KeyCode.MINUS) {
                        mapPane.setScaleX(mapPane.getScaleX() / 1.05);
                        mapPane.setScaleY(mapPane.getScaleY() / 1.05);
                        mapPane.requestFocus();
                    } else if (e.getCode() == KeyCode.A) {
                        mapPane.setTranslateX(mapPane.getTranslateX() - 5);
                        mapPane.requestFocus();
                    } else if (e.getCode() == KeyCode.D) {
                        mapPane.setTranslateX(mapPane.getTranslateX() + 5);
                        mapPane.requestFocus();
                    } else if (e.getCode() == KeyCode.W) {
                        mapPane.setTranslateY(mapPane.getTranslateY() - 5);
                        mapPane.requestFocus();
                    } else if (e.getCode() == KeyCode.S) {
                        mapPane.setTranslateY(mapPane.getTranslateY() + 5);
                        mapPane.requestFocus();
                    }
                });

                ArrayList<ArrayList<ArrayList<Tile>>> map = gameStartedBody.getXArray();

                int i = 0;
                for (int xPos = Parameter.DIZZY_HIGHWAY_WIDTH - 1; xPos >= 0; xPos--) {
                    for (int yPos = Parameter.DIZZY_HIGHWAY_HEIGHT - 1; yPos >= 0 ; yPos--) {
                        ArrayList<Tile> tileArray = map.get(xPos).get(yPos);

                        // Add a normal tile to each non-empty field to prevent whitespace
                        if (!tileArray.contains(new Empty())) {
                            tileArray.add(0, new Empty());
                        }

                        // For each tile in the array, get the image and display it in the corresponding field
                        for (Tile tile : tileArray) {
                            Image image = tile.getTileImage();
                            ImageView imageView = new ImageView();
                            imageView.setImage(image);

                            imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(Parameter.DIZZY_HIGHWAY_WIDTH));
                            imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
                            imageView.setPreserveRatio(true);

                            // Set new Y-position to avoid the map getting displayed inverted (!)
                            int newYPos = Parameter.DIZZY_HIGHWAY_HEIGHT - (yPos + 1);
                            mapPane.setConstraints(imageView, xPos, newYPos);
                            mapPane.getChildren().add(i, imageView);
                            i++;
                        }
                    }
                }
            }
        });
    }


    /**
     * This method controls robotlaser in activation phase
     */
    public void robotLaser() {

    }
}
