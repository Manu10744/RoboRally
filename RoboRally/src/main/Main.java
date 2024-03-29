package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import server.Server;
import java.io.*;
import java.util.logging.Logger;
import utils.Parameter;
import viewmodels.StageController;


/**
 * This is the main class of RoboRally. <br>
 * It will start all the other necessary classes.
 *
 * @author Vincent Tafferner
 * @author Manuel Neumayer
 * @author Ivan Dovecar
 */
public class Main extends Application {
    private static final Logger logger = Logger.getLogger( Server.class.getName() );

    @Override
    public void start(Stage primaryStage) {

        /* Run server in an own Thread, by setting server thread to daemon and clients not, it terminates its thread
           after last client (GUI) thread is gone (closing GUI) */
        Thread server = new Thread(new ServerStarterTask());
        server.setDaemon(true);
        server.start();

        // Load GUI in an own Thread
        // TODO At the moment two GUIs start at the same time (developer mode) - change parameter to one or delete for loop when project is finished
        for(int i = 0; i < Parameter.GUIS_TO_START; i++) {
            Thread gui = new Thread(new GUIStarterTask());
            gui.start();
        }
    }

    /**
     * Class ServerStarterTask implements Runnable and thereby enables the server to start in an own thread.<br>
     *
     * @author Ivan Dovecar
     */
    public class ServerStarterTask implements Runnable {
        Stage primaryStage;

        @Override
        public void run() {
            Server server = new Server();
            try {
                server.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Class GUIStarterTask implements Runnable and thereby enables the GUI to start in an own thread.<br>
     * As an result multiple GUIs can be loaded simultaneously for developing and testing reasons.
     *
     * @author Ivan Dovecar
     */
    public class GUIStarterTask implements Runnable {

        @Override
        public void run () {

            Platform.runLater(() -> {
                try {
                    Stage primaryStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    // Load views for primaryStage
                    loader.setLocation(Main.class.getResource("/views/Stage.fxml"));

                    // Custom loader setting, set the StageController manually, not automatically by @FXML
                    StageController stageController = new StageController();
                    loader.setController(stageController);

                    GridPane stageView;

                    stageView = loader.load();

                    // Set scene
                    Scene scene = new Scene(stageView);

                    // Custom cursor
                    Image cursorImage = new Image("/resources/images/others/Cursor.png");
                    scene.setCursor(new ImageCursor(cursorImage));

                    // Set Stage boundaries to visible bounds of the main screen
                    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
                    primaryStage.setX(primaryScreenBounds.getMinX());
                    primaryStage.setY(primaryScreenBounds.getMinY());
                    primaryStage.setWidth(primaryScreenBounds.getWidth());
                    primaryStage.setHeight(primaryScreenBounds.getHeight());

                    // Set Stage icon and title
                    primaryStage.getIcons().add(new Image("/resources/images/others/robotIcon.png"));
                    primaryStage.setTitle("RoboRally");
                    primaryStage.setScene(scene);

                    // Show primaryStage
                    primaryStage.show();

                    // Kill thread on close request
                    primaryStage.setOnCloseRequest(event -> {
                        System.out.println("Executing System.exit(0) on close request " + primaryStage.getTitle());
                        System.exit(0);
                    });
                 } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}