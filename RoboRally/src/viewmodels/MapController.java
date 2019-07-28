package viewmodels;

import client.Client;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import server.game.Robot;
import server.game.Tiles.*;
import utils.Parameter;
import utils.json.MessageDistributer;
import utils.json.protocol.GameStartedBody;

import java.util.*;
import java.util.logging.Logger;

import static utils.Parameter.*;


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
 * @author Mia Brandtner
 * @author Manu Neumayer
 * @author Verena Sadtler
 */

public class MapController implements IController {
    @FXML
    private GridPane mapPane;

    private boolean allowSetStart;
    public int mapChangeCounter;
    public int mapWidth;
    public int mapHeight;

    private Tile antenna;

    private Map<String, Group> fieldMap = new HashMap<String, Group>();
    private Map<String, Wall> wallMap = new HashMap<>();
    private Map<String, Pit> pitMap = new HashMap<>();
    private Map<String, Gear> gearMap = new HashMap<>();
    private Map<String, Laser> laserMap = new HashMap<>();
    private Map<String, PushPanel> pushPanelMap = new HashMap<>();
    private Map<String, RestartPoint> rebootMap = new HashMap<>();
    private Map<String, CheckPoint> checkPointMap = new HashMap<>();
    private Map<String, EnergySpace> energySpaceMap = new HashMap<>();
    private Map<String, Robot> robotMap = new HashMap<>();
    private Map<String, Antenna> antennaMap = new HashMap<>();

    private ArrayList<ArrayList<ArrayList<Tile>>> map;
    private ArrayList<Group> startPointList;
    private StageController stageController;

    private static final Logger logger = Logger.getLogger(viewmodels.MapController.class.getName());
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Override
    public IController setPrimaryController(StageController stageController) {
        this.stageController = stageController;
        return this;
    }

    @FXML
    public void initialize() {
        // This Listener makes a callback after the map has been completely loaded
        mapPane.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {
                MapController mapController = (MapController) stageController.getControllerMap().get("Map");
                mapChangeCounter++;
                logger.info("MAP CHANGE COUNTER: " + mapChangeCounter);

                // Map size = Height * Width + 1(Group containing all the fields)
                int mapSizeAfterLoad = (mapController.map.size() * mapController.map.get(0).size()) + 1;

                if (mapChangeCounter == mapSizeAfterLoad) {
                    // Map is completely loaded
                    logger.info(ANSI_GREEN + "( MAPCONTROLLER ): MAP LOADING FINISHED!" + ANSI_RESET);

                    mapController.initEventsOnStartpoints();
                    logger.info("Initialized MouseClick Events on all StartPoints");
                    
                    // Popup of 9 playerHand to choose from
                   // ((PlayerMatController) stageController.getControllerMap().get("PlayerMat")).initializeCards(null); //handleYourCards
                }
            }
        });
    }



    /**
     * This method fills the GridPane that's responsible for displaying the map. It is triggered inside of the
     *
     * @param gameStartedBody MessageBody of the 'GameStarted' protocol message containing the map information.
     */
    public void fillGridPaneWithMap(GameStartedBody gameStartedBody) {

       startPointList = new ArrayList<>();

        // Set map so the Listener on the map can determine the size of map after it has been completely loaded
        this.map = gameStartedBody.getXArray();


        // Scrolling, zooming and filling of Map
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mapPane.setGridLinesVisible(true);
                mapPane.autosize();

                //Fade in effect for map
                FadeTransition ftMap = new FadeTransition(Duration.millis(4000), mapPane);
                ftMap.setFromValue(0.0);
                ftMap.setToValue(1.0);
                ftMap.setCycleCount(0);
                ftMap.play();

                //Scroll and zoom map
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

                mapPane.setOnZoom(new EventHandler<ZoomEvent>() {
                    @Override public void handle(ZoomEvent event) {
                        mapPane.setScaleX(mapPane.getScaleX() * event.getZoomFactor());
                        mapPane.setScaleY(mapPane.getScaleY() * event.getZoomFactor());
                        event.consume();
                    }
                });

                mapPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
                    logger.info("Pressed Key: " + e + mapPane.getTranslateY());
                    if (e.getCode() == KeyCode.PLUS) {
                        mapPane.setScaleX(mapPane.getScaleX() * Parameter.ZOOM_FACTOR);
                        mapPane.setScaleY(mapPane.getScaleY() * Parameter.ZOOM_FACTOR);
                        mapPane.requestFocus();
                    } else if (e.getCode() == KeyCode.MINUS) {
                        mapPane.setScaleX(mapPane.getScaleX() / Parameter.ZOOM_FACTOR);
                        mapPane.setScaleY(mapPane.getScaleY() / Parameter.ZOOM_FACTOR);
                        mapPane.requestFocus();
                    } else if (e.getCode() == KeyCode.A) {
                        mapPane.setTranslateX(mapPane.getTranslateX() - Parameter.SCROLL_FACTOR);
                        mapPane.requestFocus();
                    } else if (e.getCode() == KeyCode.D) {
                        mapPane.setTranslateX(mapPane.getTranslateX() + Parameter.SCROLL_FACTOR);
                        mapPane.requestFocus();
                    } else if (e.getCode() == KeyCode.W) {
                        mapPane.setTranslateY(mapPane.getTranslateY() - Parameter.SCROLL_FACTOR);
                        mapPane.requestFocus();
                    } else if (e.getCode() == KeyCode.S) {
                        mapPane.setTranslateY(mapPane.getTranslateY() + Parameter.SCROLL_FACTOR);
                        mapPane.requestFocus();
                    } else if (e.getCode() == KeyCode.Z) {
                        mapPane.setScaleX(Parameter.ZOOM_DEFAULT);
                        mapPane.setScaleY(Parameter.ZOOM_DEFAULT);
                        mapPane.setTranslateX(Parameter.SCROLL_DEFAULT);
                        mapPane.setTranslateY(Parameter.SCROLL_DEFAULT);
                        mapPane.requestFocus();
                    }
                });

                logger.info(ANSI_GREEN + "( MAPCONTROLLER ): MAP LOADING WAS STARTED..." + ANSI_RESET);

                ArrayList<ArrayList<ArrayList<Tile>>> map = gameStartedBody.getXArray();

                mapWidth = map.size();
                mapHeight = map.get(0).size();

                for (int xPos = mapWidth - 1; xPos >= 0; xPos--) {
                    for (int yPos = mapHeight - 1; yPos >= 0; yPos--) {
                        // Each field on the map is represented by a single Array of Tiles
                        ArrayList<Tile> tileArray = map.get(xPos).get(yPos);

                        // Only check for Empty if current field is no empty field
                        if (!tileArray.contains(null)) {
                            // Add a normal tile to each non-empty field to prevent whitespace
                            if (!containsInstance(tileArray, Empty.class)) {
                                tileArray.add(0, new Empty());
                            }
                        }

                        // Only check for Laser and wall if current field is no empty field
                        if (!tileArray.contains(null)) {
                            if (containsInstance(tileArray, Wall.class) && containsInstance(tileArray, Laser.class)) {
                                Tile wall = tileArray.get(1);
                                Tile laser = tileArray.get(2);

                                // Swap laser and wall so wall image is put over laser image
                                tileArray.set(2, wall);
                                tileArray.set(1, laser);
                            }
                        }

                        Group imageGroup = new Group();
                        // For each tile in the array, get the image and display it in the corresponding field
                        for (Tile tile : tileArray) {
                            if (tile == null) {
                                ImageView imageView = new ImageView();

                                imageView.setPreserveRatio(true);
                                imageGroup.getChildren().add(imageView);
                            } else {
                                Image image = tile.getTileImage();
                                ImageView imageView = new ImageView();
                                imageView.setImage(image);

                                if (tile instanceof Wall) {
                                    String ID = xPos + "-" + yPos;
                                    Wall wall = (Wall) tile;
                                    wallMap.put(ID, wall);
                                }

                                if (tile instanceof Pit) {
                                    String ID = xPos + "-" + yPos;
                                    Pit pit = (Pit) tile;
                                    pitMap.put(ID, pit);
                                }

                                if (tile instanceof Gear) {
                                    String ID = xPos + "-" + yPos;
                                    Gear gear = (Gear) tile;
                                    gearMap.put(ID, gear);
                                }

                                if (tile instanceof Laser) {
                                    String ID = xPos + "-" + yPos;
                                    Laser laser = (Laser) tile;
                                    laserMap.put(ID, laser);
                                }

                                if (tile instanceof PushPanel) {
                                    String ID = xPos + "-" + yPos;
                                    PushPanel pushPanel = (PushPanel) tile;
                                    pushPanelMap.put(ID, pushPanel);
                                }

                                if (tile instanceof RestartPoint) {
                                    String ID = xPos + "-" + yPos;
                                    RestartPoint restartPoint = (RestartPoint) tile;
                                    rebootMap.put(ID, restartPoint);
                                }

                                if (tile instanceof CheckPoint) {
                                    String ID = xPos + "-" + yPos;
                                    CheckPoint checkPoint = (CheckPoint) tile;
                                    checkPointMap.put(ID, checkPoint);
                                }

                                if (tile instanceof EnergySpace) {
                                    String ID = xPos + "-" + yPos;
                                    EnergySpace energySpace = (EnergySpace) tile;
                                    energySpaceMap.put(ID, energySpace);
                                }

                                if (tile instanceof StartPoint){
                                    String ID = xPos + "-" + yPos;
                                    imageGroup.setId(ID);
                                    imageGroup.setStyle("-fx-cursor: hand;");
                                    startPointList.add(imageGroup);
                                }

                                if (tile instanceof Antenna) {
                                    setAntenna(tile);

                                    //Antenna is added with position to antennaMap
                                    String ID = xPos + "-" + yPos;
                                    Antenna antenna = new Antenna();
                                    antennaMap.put(ID, antenna);

                                    logger.info(ANSI_GREEN + "ANTENNA IN Client antennaMap HAS BEEN SET! COORDINATES: " + "( " + xPos + " | " + yPos + " )" + ANSI_RESET);
                                }

                                // Necessary for making map fields responsive
                                imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(mapWidth));
                                imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(mapHeight));
                                imageView.setPreserveRatio(true);

                                imageGroup.getChildren().add(imageView);}

                        }

                        // Groups are added to HashMap
                        String groupID = xPos + "-" + yPos;
                        fieldMap.put(groupID, imageGroup);

                        // Set new Y-position to avoid the map getting displayed inverted (!)
                        int newYPos = mapHeight - (yPos + 1);
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

    /**
     * This method initializes the click events for all starting points on the map, so they can be clicked.
     */
    public void initEventsOnStartpoints() {

        for (Group startpoint : startPointList) {
            logger.info(ANSI_GREEN + "( MAPCONTROLLER ): DETECTED STARTPOINT ON " + startpoint.getId() + "." + ANSI_RESET);
            startpoint.setOnMouseClicked(new EventHandler<MouseEvent>() {
                // We need the map controller here because Event Handlers create an anonymous inner class (!)
                MapController mapController = (MapController) stageController.getControllerMap().get("Map");

                @Override
                public void handle(MouseEvent mouseEvent) {
                    String id = startpoint.getId();

                    ChatController chatController = (ChatController) stageController.getControllerMap().get("Chat");

                    if (mapController.isAllowedToSetStart()) {
                        chatController.getClient().sendStartingPoint(id);
                    }
                }
            });
        }
    }


    /**
     * This method sets the robot onto the chosen startingpoint.
     * @param playerRobot The players chosen robot
     * @param startingPoint The startingpoint the player has chosen to set his robot on.
     */
    public void setStartingPoint(Robot playerRobot, String startingPoint) {
        String antennaOrientation = this.antenna.getOrientations().get(0);

        ImageView imageView = new ImageView(playerRobot.getRobotImage());
        imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(mapWidth));
        imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(mapHeight));
        imageView.setPreserveRatio(true);

        // Set robot orientation equal to antenna orientation
        if (antennaOrientation.equals("up")) {
            imageView.rotateProperty().setValue(0);
        } else if (antennaOrientation.equals("right")) {
            imageView.rotateProperty().setValue(90);
        } else if (antennaOrientation.equals("down")) {
            imageView.rotateProperty().setValue(180);
        } else if (antennaOrientation.equals("left")) {
            imageView.rotateProperty().setValue(270);
        }

        //Robot imageView is added to map
        fieldMap.get(startingPoint).getChildren().add(imageView);
        //Robot is saved in robotMap
        robotMap.put(startingPoint, playerRobot);

        logger.info(ANSI_GREEN + "( MAPCONTROLLER ): New player Robot " + playerRobot.getName() + " was added to robotMap at position " + startingPoint + ANSI_RESET);


    }

    /**
     * This method turns the robot according to turn card either left or right
     * @param robotPosition
     * @param turnDirection
     */
    public void turnRobot (String robotPosition, String  turnDirection){
        //Here we get the robot imageView
        ImageView robotImageView = (ImageView) fieldMap.get(robotPosition).getChildren().get(fieldMap.get(robotPosition).getChildren().size()-1);
        double currentOrientation = robotImageView.rotateProperty().getValue();

        //Here we turn it either to right or left side
        if(turnDirection.equals(ORIENTATION_LEFT)) {
            robotImageView.rotateProperty().setValue(currentOrientation - 90);
        }else{
            robotImageView.rotateProperty().setValue(currentOrientation + 90);
        }
    }

    /**
     * This method removes the robot from its old position and sets it onto the new position
     * @param oldPosition The old Position of the robot
     * @param newPosition The new Position of the robot
     */
    public void moveRobot(String oldPosition, String newPosition ){
        ImageView robotImageView = (ImageView) fieldMap.get(oldPosition).getChildren().get(fieldMap.get(oldPosition).getChildren().size()-1);
        fieldMap.get(oldPosition).getChildren().remove(fieldMap.get(oldPosition).getChildren().size()-1);
        fieldMap.get(newPosition).getChildren().add(robotImageView);
    }

    /**
     * This method controls robotlaser in activation phase
     */
    public void robotLaser() {

    }

    public ArrayList<ArrayList<ArrayList<Tile>>> getMap() { return map; }

    public boolean isAllowedToSetStart() {
        return allowSetStart;
    }

    public void setAllowedToSetStart(Boolean allowStart) {
        this.allowSetStart = allowStart;
    }

    public ArrayList<Group> getStartpointList() {
        return startPointList;
    }

    public Tile getAntenna() {
        return antenna;
    }

    public void setAntenna(Tile antenna) {
        this.antenna = antenna;
    }

    public Map<String, Group> getFieldMap() {
        return fieldMap;
    }

    public Map<String, Wall> getWallMap() {
        return wallMap;
    }

    public Map<String, Pit> getPitMap() {
        return pitMap;
    }

    public Map<String, Gear> getGearMap() {
        return gearMap;
    }

    public Map<String, Laser> getLaserMap() {
        return laserMap;
    }

    public Map<String, PushPanel> getPushPanelMap() {
        return pushPanelMap;
    }

    public Map<String, RestartPoint> getRebootMap() {
        return rebootMap;
    }

    public Map<String, CheckPoint> getCheckPointMap() {
        return checkPointMap;
    }

    public Map<String, EnergySpace> getEnergySpaceMap() {
        return energySpaceMap;
    }

    public Map<String, Robot> getRobotMap() {
        return robotMap;
    }

    public Map<String, Antenna> getAntennaMap() {
        return antennaMap;
    }

    public void setRobotMap(Map<String, Robot> robotMap) {
        this.robotMap = robotMap;
    }
}

