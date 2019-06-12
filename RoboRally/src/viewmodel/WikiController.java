package viewmodel;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.lang.Exception;


public class WikiController extends Application {
    private Stage rootStage;
    @FXML
    private AnchorPane wiki;
    @FXML
    private AnchorPane cardsWiki;
    @FXML
    private VBox vBox;
    @FXML
    private Button buttonCards;
    @FXML
    private Button buttonRobots;
    @FXML
    private Button buttonRules;
    @FXML
    private ImageView imageCards;
    @FXML
    private ImageView imageRobots;
    @FXML
    private ImageView imageRules;


    @Override
    public void start(Stage stage) {
        this.rootStage = stage;
        initStage();
    }

    private void initStage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WikiController.class.getResource("/view/Wiki.fxml"));
            wiki = loader.load();
            Scene scene = new Scene(wiki);
            rootStage.setScene(scene);
            rootStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void cardsClicked()  {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WikiController.class.getResource("/view/CardsWiki.fxml"));
            AnchorPane cardsWiki = loader.load();
            Scene scene = new Scene(cardsWiki);
            rootStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
