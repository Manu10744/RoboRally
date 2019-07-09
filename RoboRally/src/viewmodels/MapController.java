package viewmodels;

import client.Client;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import server.game.Tiles.*;
import utils.Parameter;
import utils.json.protocol.GameStartedBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
                        if (!tileArray.contains(new Empty())) {
                            tileArray.add(0, new Empty());
                        }

                        Group imageGroup = new Group();
                        // For each tile in the array, get the image and display it in the corresponding field
                        for (Tile tile : tileArray) {
                            Image image = tile.getTileImage();
                            ImageView imageView = new ImageView();
                            imageView.setImage(image);

                            // Necessary for making map fields responsive
                            imageView.fitWidthProperty().bind(mapPane.widthProperty().divide(Parameter.DIZZY_HIGHWAY_WIDTH));
                            imageView.fitHeightProperty().bind(mapPane.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
                            imageView.setPreserveRatio(true);

                            imageGroup.getChildren().add(imageView);
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
                allowSetStart = true;

            }
        });
    }

    public ImageView getImageByFigure(int figure) {
        if (figure == 1) {
            Image HammerBot = new Image("/resources/images/robots/HammerBot.PNG");
            ImageView HammerBotView = new ImageView(HammerBot);
            return HammerBotView;
        } else if (figure == 2) {
            Image HulkX90 = new Image("/resources/images/robots/HulkX90.PNG");
            ImageView HulkX90View = new ImageView(HulkX90);
            return HulkX90View;
        } else if (figure == 3) {
            Image SmashBot = new Image("/resources/images/robots/SmashBot.PNG");
            ImageView SmashBotView = new ImageView(SmashBot);
            return SmashBotView;
        } else if (figure == 4) {
            Image Twonky = new Image("/resources/images/robots/Twonky.PNG");
            ImageView TwonkyView = new ImageView(Twonky);
            return TwonkyView;
        } else if (figure == 5) {
            Image Spinbot = new Image("/resources/images/robots/Spinbot.PNG");
            ImageView SpinbotView = new ImageView(Spinbot);
            return SpinbotView;
        } else if (figure == 6) {
            Image ZoomBot = new Image("/resources/images/robots/ZoomBot.PNG");
            ImageView ZoomBotView = new ImageView(ZoomBot);
            return ZoomBotView;
        }
        return null;
    }

    public void setStartingPoint(int figure) {
        if (allowSetStart) {
            Platform.runLater(new Runnable() {
                @Override

                public void run() {
                    //the startinPoints in order according to y-position
                    Group startPoint1 = fieldMap.get("0-3");
                    Group startPoint2 = fieldMap.get("0-6");
                    Group startPoint3 = fieldMap.get("1-1");
                    Group startPoint4 = fieldMap.get("1-4");
                    Group startPoint5 = fieldMap.get("1-5");
                    Group startPoint6 = fieldMap.get("1-8");

                    ArrayList<Group> startPoints = new ArrayList<>();
                    startPoints.add(startPoint1);
                    startPoints.add(startPoint2);
                    startPoints.add(startPoint3);
                    startPoints.add(startPoint4);
                    startPoints.add(startPoint5);
                    startPoints.add(startPoint6);

                    for (Group startpoint : startPoints) {
                        startpoint.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                System.out.println("Clicked on StartPoint " + startpoint.getId());
                                // startpoint.getChildren().add(!!!); //add Figure
                                ImageView robot = getImageByFigure(figure);
                                robot.fitWidthProperty().bind(mapPane.widthProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
                                robot.fitHeightProperty().bind(mapPane.heightProperty().divide(Parameter.DIZZY_HIGHWAY_HEIGHT));
                                robot.preserveRatioProperty().set(true);

                                robot.rotateProperty().setValue(90);

                                startpoint.getChildren().add(robot);
                                allowSetStart = false;
                            }

                        });
                    }
                }

            });

        }
    }

    public ImageView getImageByFigureNumber (int figure){

        ImageView robotImageView = new ImageView();

        switch (figure) {
            case 1: {
                Image avatar = new Image("/resources/images/robots/choose-robot-hammerbot.png");
                robotImageView.setImage(avatar);
                break;
            }
            case 2: {
                Image avatar = new Image("/resources/images/robots/choose-robot-hulkX90.png");
                robotImageView.setImage(avatar);
                break;
            }
            case 3: {
                Image avatar = new Image("/resources/images/robots/choose-robot-smashbot.png");
                robotImageView.setImage(avatar);
                break;
            }
            case 4: {
                Image avatar = new Image("/resources/images/robots/choose-robot-twonky.png");
                robotImageView.setImage(avatar);
                break;
            }
            case 5: {
                Image avatar = new Image("/resources/images/robots/choose-robot-hammerbot.png");
                robotImageView.setImage(avatar);
                break;
            }
            case 6: {
                Image avatar = new Image("/resources/images/robots/choose-robot-zoombot.png");
                robotImageView.setImage(avatar);
                break;
            }

        }
        return robotImageView;
    }



    /**
     * This method controls robotlaser in activation phase
     */
    public void robotLaser() {

    }
    public boolean isAllowSetStart() {
        return allowSetStart;
    }

    public void setAllowStart(Boolean allowStart){
        this.allowSetStart = allowStart;
    }

}
