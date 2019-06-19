package viewmodel;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import modelserver.game.Maps.DizzyHighway;
import modelserver.game.Maps.Map;
import modelserver.game.Maps.MapBody;
import modelserver.game.Tiles.Tile;
import utils.Parameter;

import java.util.ArrayList;

/**
 * The class MapController notifies the Server where each robot is positioned and and on which tile(type) it rests
 */
public class MapController extends Application {

    @FXML
    private Stage stage;

    @FXML
    private TilePane tilePane;
    @FXML
    private ImageView robotYellow;
    @FXML
    private ImageView robotOrange;
    @FXML
    private ImageView robotRed;
    @FXML
    private ImageView robotPurple;
    @FXML
    private ImageView robotBlue;
    @FXML
    private ImageView robotGreen;

    //Todo must be adapted for other maps than dizzy Highway -> input from a choose icon as String, then calling the Map(String mapName) constructor with it
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.tilePane = new TilePane();
        ArrayList<Image> tileImages = new ArrayList<>();

        //dimensions of the map dizzyHighway
        Map dizzyHighway = new Map(Parameter.DIZZY_HIGHWAY); //here the name would have to come via an imput
        MapBody dizzyHighWayMapBody = dizzyHighway.getGameMap();

        tilePane.setPrefColumns(Parameter.DIZZY_HIGHWAY_WIDTH);
        tilePane.setPrefRows(Parameter.DIZZY_HIGHWAY_WIDTH);
        ObservableList paneElements = tilePane.getChildren();

        //reading of images and then putting them within the tilePane
        for (int i = 0; i < Parameter.DIZZY_HIGHWAY_HEIGHT; i++) {
            for (int j = 0; j < Parameter.DIZZY_HIGHWAY_WIDTH; j++) {
                ArrayList<Tile> tiles = dizzyHighWayMapBody.getTileList();
                for (Tile tile: tiles){
                tileImages.add(Tile.getTileImage(tile));}

                for (Image image: tileImages) {
                   paneElements.add(new ImageView (image));
                }
            }
        }
        Scene tileScene = new Scene(tilePane);

        stage.setScene(tileScene);

        stage.show();


    }

    public Stage getStage(){
        return  this.stage;
    }

    public TilePane getTilePane() {
        return  this.tilePane;}

    public static void main(String args[]){launch(args);    }

    /**
     * This method controls robotlaser in activation phase
     */
    public void robotLaser() {

    }



}