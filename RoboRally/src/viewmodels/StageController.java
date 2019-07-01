package viewmodels;

import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import static viewmodels.ChatController.*;

import java.io.IOException;

public class StageController {

/**
 * This controller class is responsible for the board and the chat.
 * GridPane 'stage' adapts automatically to the users' screen setup.
 * Stage GUI and its elements are fully responsive.
 * It loads furthermore the initial start screen and the select robot screen.
 *
 * @author Ivan Dovecar
 */

    @FXML
    GridPane stage;
    @FXML
    GridPane startScreen;
    @FXML
    GridPane chooseRobot;
    @FXML
    GridPane opponentMat;
    @FXML
    GridPane map;
    @FXML
    GridPane playerMat;
    @FXML
    GridPane chat;
    @FXML
    ImageView hammerBot;
    @FXML
    ImageView hulkX90;
    @FXML
    ImageView smashBot;
    @FXML
    ImageView twonky;
    @FXML
    ImageView spinBot;
    @FXML
    ImageView zoomBot;

   public void mouseClicked() {
       /*
       hammerBot.setOnMousePressed(event ->{
           figure.setValue(1);
           chooseRobot.setVisible(false);
       });
       hulkX90.setOnMousePressed(event ->{
           figure.setValue(2);
           chooseRobot.setVisible(false);
       });
       smashBot.setOnMousePressed(event ->{
           figure.setValue(3);
           chooseRobot.setVisible(false);
       });
       twonky.setOnMousePressed(event ->{
           figure.setValue(4);
           chooseRobot.setVisible(false);
       });
       spinBot.setOnMousePressed(event ->{
           figure.setValue(5);
           chooseRobot.setVisible(false);
       });
       zoomBot.setOnMousePressed(event ->{
           figure.setValue(6);
           chooseRobot.setVisible(false);
       });
       */
   }

}
