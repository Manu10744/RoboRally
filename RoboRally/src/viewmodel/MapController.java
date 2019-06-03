package viewmodel;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

/**
 * The class MapController notifies the Server where each robot is positioned and and on which tile(type) it rests
 */
public class MapController {

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



}