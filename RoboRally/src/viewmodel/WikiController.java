package viewmodel;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class WikiController extends Application {

    private Stage stage;
    private AnchorPane wiki;
    @FXML
    private VBox vBox;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        initStage();

    }
    private void initStage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WikiController.class.getResource("/view/Wiki.fxml"));
            wiki = loader.load();
            Scene scene = new Scene(wiki);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }



    public static void main(String[] args) {
        launch(args);
    }
}
