package viewmodels;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This controller class is responsible for the wiki.
 * @author Verena Sadtler
 * @author Jessica Gerlach
 */

public class WikiController extends Application {

    private Stage rootStage;
    @FXML
    private GridPane wiki;
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


    /**
     * This method starts the wiki
     *
     * @param stage
     */

    @Override
    public void start(Stage stage) {
        this.rootStage = stage;
        initStage();
    }

    /**
     * This method inits the stage of the wiki
     */

    private void initStage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WikiController.class.getResource("/views/Wiki.fxml"));
            wiki = loader.load();
            Scene scene = new Scene(wiki);

            rootStage.setScene(scene);
            rootStage.showingProperty().addListener((observable, oldValue, showing) -> {
                if (showing) {
                    rootStage.setMinWidth(rootStage.getWidth());
                    rootStage.setMinHeight(rootStage.getHeight());
                }
            });
            rootStage.show();
            //rootStage.getStylesheets().addAll(getClass().getResource("/css/app.css").toExternalForm())
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method handles button klicks inside the wiki stage to get from one to another scene
     *
     * @param event
     * @throws IOException
     */

    @FXML
    public void buttonClicked(ActionEvent event) throws IOException {

        Stage rootStage;
        Parent root;

        if (event.getSource() == buttonCards) {
            rootStage = (Stage) buttonCards.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/views/WikiCards.fxml"));
            Scene scene = new Scene(root);
            rootStage.setScene(scene);
            rootStage.show();
        }
        if (event.getSource() == buttonRobots) {
            rootStage = (Stage) buttonRobots.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/views/WikiRobots.fxml"));
            Scene scene = new Scene(root);
            rootStage.setScene(scene);
            rootStage.show();
        }
        if (event.getSource() == buttonRules) {
            rootStage = (Stage) buttonRules.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/views/WikiRules.fxml"));
            Scene scene = new Scene(root);
            rootStage.setScene(scene);
            rootStage.show();
        }
    }

}