import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import modelserver.Server;
import java.io.*;
import java.util.logging.Logger;


/**
 * This is the main class of RoboRally. <br>
 * It will start all the other necessary classes.
 *
 * @author Vincent Tafferner
 * @author Manuel Neumayer
 * @author Ivan Dovecar
 */
public class Main extends Application {

    Stage primaryStage;
    private static final Logger logger = Logger.getLogger( Server.class.getName() );

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Run the server
        Server server = new Server();
        try {
            server.start(primaryStage);
        } catch (Exception e) {
            logger.info("Positive, switching to client mode and activating GUI...");
        }

        // Load the GUI
        try {
            FXMLLoader loader = new FXMLLoader();
            // Load view for primaryStage
            loader.setLocation(Main.class.getResource("view/Stage.fxml"));
            AnchorPane stageView;
            stageView = loader.load();

            // Set scene
            Scene scene = new Scene(stageView);
            primaryStage.setScene(scene);
            // Show primaryStage
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}