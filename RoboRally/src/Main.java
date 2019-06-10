import javafx.application.Application;
import javafx.application.Platform;
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


    private static final Logger logger = Logger.getLogger( Server.class.getName() );

    @Override
    public void start(Stage primaryStage) {


            // Run server in an own Thread
            Thread server = new Thread(new ServerStarterTask());
            server.start();

            // Load GUI in an own Thread
            // TODO At the moment two GUIs start at the same time (developer mode) - change i to one or delete for loop when project is finished
            for(int i = 0; i < 2; i++) {
                Thread gui = new Thread(new GUIStarterTask());
                gui.start();
            }

    }

    /**
     * This class implements Runnable and thereby enables the server to start in an own thread.<br>
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

    public class GUIStarterTask implements Runnable {

        @Override
        public void run () {

            Platform.runLater(() -> {
            try {
                Stage primaryStage = new Stage();
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
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}