package viewmodel;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


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

    @FXML
    public void buttonClicked(ActionEvent event)throws IOException {

        Stage rootStage;
        Parent root;

        if (event.getSource() == buttonCards) {
            rootStage = (Stage) buttonCards.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/view/CardsWiki.fxml"));
            Scene scene = new Scene(root);
            rootStage.setScene(scene);
            rootStage.show();
        }
        if (event.getSource() == buttonRobots) {
            rootStage = (Stage) buttonRobots.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/view/RobotsWiki.fxml"));
            Scene scene = new Scene(root);
            rootStage.setScene(scene);
            rootStage.show();
        }
        if (event.getSource() == buttonRules) {
            rootStage = (Stage) buttonRules.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/view/WikiRules.fxml"));
            Scene scene = new Scene(root);
            rootStage.setScene(scene);
            rootStage.show();
        }
    }

    public void initialize(URL url, ResourceBundle rb){

    }

    public static void main(String[] args) {
        launch(args);
    }
}