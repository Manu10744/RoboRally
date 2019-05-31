package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.*;


/**
 * This is the main class of RoboRally. <br>
 * It will start all the other necessary classes.
 *
 * @author Vincent Tafferner
 */
public class Main extends Application {

    Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Chat.fxml"));
            AnchorPane chat = (AnchorPane) loader.load();

            Scene scene = new Scene(chat);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}