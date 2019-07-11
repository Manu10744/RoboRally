package viewmodels;

import client.Client;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import server.game.Robot;
import server.game.Tiles.*;
import utils.Parameter;
import utils.json.protocol.GameStartedBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;


/**
 * The class MapController notifies the Server where each robot is positioned and and on which tile(type) it rests
 * The mapPane fits the grid and is responsive, furthermore it is zoomable (click the mapPane for requesting focus ->
 * zoom in by pressing "+" / zoom out by pressing "-") and scrollable by Mouse (scroll wheel for y-pos and SHIFT +
 * scroll wheel for x-position) or by keyboard (left by "A" / right by "D" / up by "W" / down by "S"). By Pressing "Z"
 * Map will instantly resize and scroll to default size and position.
 * <p>
 * Additionally, we save every group with imageViews of the pos x/y in a hashmap, so that each group content can be gotten via this hashmap instead of going through the arrays
 * with calling fieldMap.get("x-y")
 *
 * @author Ivan Dovecar
 * @author Mia
 * @author Manu
 */

public class MapController implements IController {

    private StageController stageController;
    private boolean allowSetStart;

    @FXML
    private GridPane mapPane;
    private Map<String, Group> fieldMap = new HashMap<String, Group>();
    private static final Logger logger = Logger.getLogger(viewmodels.MapController.class.getName());

    @Override
    public IController setPrimaryController(StageController stageController) {
        this.stageController = stageController;
        return this;
    }

    /**
     * This method fills the GridPane that's responsible for displaying the map. It is triggered inside of the
     * {@link utils.json.MessageDistributer#handleGameStarted(Client, Client.ClientReaderTask, GameStartedBody)} method.
     *
     * @param gameStartedBody MessageBody of the 'GameStarted' protocol message containing the map information.
     */
    public void fillGridPaneWithMap(GameStartedBody gameStartedBody) {

        System.out.println("MAPPANE CHILDREN START: " + mapPane.getChildren().size());

        // Popup for performing actions on robot
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Robot Actions");
                alert.setContentText("Make your robot perform actions!");
                alert.setResizable(true);

                ButtonType moveI = new ButtonType("MoveI");
                ButtonType moveII = new ButtonType("MoveII");
                ButtonType moveIII = new ButtonType("MoveII");
                ButtonType turnRight = new ButtonType("TurnRight");
                ButtonType turnLeft = new ButtonType("TurnLeft");
                ButtonType backup = new ButtonType("Backup");
                ButtonType uTurn = new ButtonType("UTurn");
                ButtonType again = new ButtonType("Again");

                alert.getButtonTypes().addAll(moveI, moveII, moveIII, turnRight, turnLeft, backup, uTurn, again);

                while (true) {
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == moveI) { /* Function moveI() */ }
                    if (result.get() == moveII) { /* Function moveII() */ }
                    if (result.get() == moveIII) { /* Function moveIII() */ }
                    if (result.get() == turnRight) { /* Function turnRight() */ }
                    if (result.get() == turnLeft) { /* Function turnLeft() */ }
                    if (result.get() == backup) { /* Function backUp() */ }
                    if (result.get() == uTurn) { /* Function uTurn() */ }
                    if (result.get() == again) { /* Function again() */ }
                }
            }
        });

        // Scrolling, zooming and filling of Map
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
                    logger.info("Pressed Key: " + e + mapPane.getTranslateY());
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
                    } else if (e.getCode() == KeyCode.Z) {
                        mapPane.setScaleX(1);
                        mapPane.setScaleY(1);
                        mapPane.setTranslateX(0.0);
                        mapPane.setTranslateY(0.0);
                        mapPane.requestFocus();
                    }
                });

                ArrayList<ArrayList<ArrayList<Tile>>> map = gameStartedBody.getXArray();

                for (int xPos = Parameter.DIZZY_HIGHWAY_WIDTH - 1; xPos >= 0; xPos--) {
                    for (int yPos = Parameter.DIZZY_HIGHWAY_HEIGHT - 1; yPos >= 0; yPos--) {
                        // Each field on the map is represented by a single Array of Tiles
                        ArrayList<Tile> tileArray = map.get(xPos).get(yPos);

                        // Add a normal tile to each non-empty field to prevent whitespace
                        if (!containsInstance(tileArray, Empty.class)) {
                            System.out.println("Doesnt contain empty, so i will add one!");
                            tileArray.add(0, new Empty());
                        }

                        Group imageGroup = new Group();
                        // For each tile in the array, get the image and display it in the corresponding field
                        for (Tile tile : tileArray) {
                            // If the field contains a Laser and Wall, only display a picture for wall which already contains the laser,
                            // Correct image is provided by considering orientation of wall
                                if (containsInstance(tileArray, Laser.class) && containsInstance(tileArray, Wall.class)) {
                                    if (tile instanceof Wall) {
                                        String wallOrientation = tile.getOrientations().get(0);
                                        if (wallOrientation.equals(Parameter.ORIENTATION_LEFT)) {
                                            Image image = new Image("resources/images/mapelements/laser-onebeam-start-left.png");
                                            ImageView imageView = new ImageView(image);

                                            imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(Parameter.DIZZY_HIGHWAY_WIDTH));
                                            imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
                                            imageView.setPreserveRatio(true);

                                            imageGroup.getChildren().add(imageView);
                                        } else if (wallOrientation.equals(Parameter.ORIENTATION_RIGHT)) {
                                            Image image = new Image("resources/images/mapelements/laser-onebeam-start-right.png");
                                            ImageView imageView = new ImageView(image);

                                            imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(Parameter.DIZZY_HIGHWAY_WIDTH));
                                            imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
                                            imageView.setPreserveRatio(true);

                                            imageGroup.getChildren().add(imageView);
                                        } else if (wallOrientation.equals(Parameter.ORIENTATION_DOWN)) {
                                            Image image = new Image("resources/images/mapelements/laser-onebeam-start-bottom.png");
                                            ImageView imageView = new ImageView(image);

                                            imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(Parameter.DIZZY_HIGHWAY_WIDTH));
                                            imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
                                            imageView.setPreserveRatio(true);

                                            imageGroup.getChildren().add(imageView);
                                        } else if (wallOrientation.equals(Parameter.ORIENTATION_UP)) {
                                            Image image = new Image("resources/images/mapelements/laser-onebeam-start-top.png");
                                            ImageView imageView = new ImageView(image);

                                            imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(Parameter.DIZZY_HIGHWAY_WIDTH));
                                            imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
                                            imageView.setPreserveRatio(true);

                                            imageGroup.getChildren().add(imageView);
                                        }
                                    }
                                    // If tile is Laser, don't provide an image
                                    if (tile instanceof Laser) break;
                                    if (tile instanceof Empty) {
                                        Image image = new Image("resources/images/mapelements/normal1.png");
                                        ImageView imageView = new ImageView(image);

                                        imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(Parameter.DIZZY_HIGHWAY_WIDTH));
                                        imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
                                        imageView.setPreserveRatio(true);

                                        imageGroup.getChildren().add(imageView);
                                    }
                            } else {
                                Image image = tile.getTileImage();
                                ImageView imageView = new ImageView();
                                imageView.setImage(image);

                                // TODO: For each Tile of type StartPoint set an ID dynamically consisting out of the coordinates!

                                // Necessary for making map fields responsive
                                imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(Parameter.DIZZY_HIGHWAY_WIDTH));
                                imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
                                imageView.setPreserveRatio(true);

                                imageGroup.getChildren().add(imageView);
                            }
                        }
                        // Groups are added to HashMap
                        String groupID = xPos + "-" + yPos;
                        fieldMap.put(groupID, imageGroup);

                        // Set new Y-position to avoid the map getting displayed inverted (!)
                        int newYPos = Parameter.DIZZY_HIGHWAY_HEIGHT - (yPos + 1);
                        mapPane.setConstraints(imageGroup, xPos, newYPos);
                        mapPane.getChildren().add(imageGroup);
                    }
                }
            }
        });
        this.allowSetStart = true;
    }

    /**
     * This method checks if an ArrayList<Tile> contains an element which is an instance of a given class.
     * @param listToCheck The ArrayList that gets iterated and checked.
     * @param target The class of the desired object that is searched inside the ArrayList
     * @return <code>true</code> if that ArrayList contains an object of the given class
     */
    public boolean containsInstance(ArrayList<Tile> listToCheck, Class<?> target) {
        for (Tile listItem : listToCheck) {
            if (listItem.getClass().equals(target)) {
                return true;
            }
        }
        return false;
    }

    public void sendStartPoint() {
        // The startinPoints in order according to y-position
        Group startPoint1 = fieldMap.get("0-3");
        startPoint1.setId("0-3");
        Group startPoint2 = fieldMap.get("0-6");
        startPoint2.setId("0-6");
        Group startPoint3 = fieldMap.get("1-1");
        startPoint3.setId("1-1");
        Group startPoint4 = fieldMap.get("1-4");
        startPoint4.setId("1-4");
        Group startPoint5 = fieldMap.get("1-5");
        startPoint5.setId("1-5");
        Group startPoint6 = fieldMap.get("1-8");
        startPoint6.setId("1-8");

        ArrayList<Group> startPoints = new ArrayList<>();
        startPoints.add(startPoint1);
        startPoints.add(startPoint2);
        startPoints.add(startPoint3);
        startPoints.add(startPoint4);
        startPoints.add(startPoint5);
        startPoints.add(startPoint6);

        for (Group startpoint : startPoints) {
            startpoint.setOnMouseClicked(new EventHandler<MouseEvent>() {
                // We need the map controller here because Event Handlers create an anonymous inner class (!)
                MapController mapController = (MapController) stageController.getControllerMap().get("Map");

                @Override
                public void handle(MouseEvent mouseEvent) {
                    String id = startpoint.getId();

                    ChatController chatController = (ChatController) stageController.getControllerMap().get("Chat");
                    chatController.getClient().sendStartingPoint(id);
                }
            });
        }
    }


    public void setStartingPoint(Robot playerRobot, String startingPoint) {
        fieldMap.get(startingPoint).getChildren().add(new ImageView(playerRobot.getRobotImage()));
    }

    /**
     * This method controls robotlaser in activation phase
     */
    public void robotLaser() {

    }

    public boolean isAllowedToSetStart() {
        return allowSetStart;
    }

    public void setAllowedToSetStart(Boolean allowStart) {
        this.allowSetStart = allowStart;
    }

}
