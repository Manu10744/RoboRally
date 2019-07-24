package viewmodels;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
    private Button buttonCourses;
    @FXML
    private Button buttonCard;
    @FXML
    private Button buttonRobot;
    @FXML
    private Button buttonRule;
    @FXML
    private Button buttonCourse;
    @FXML
    private ImageView imageCards;
    @FXML
    private ImageView imageRobots;
    @FXML
    private ImageView imageRules;
    @FXML
    private Label headingWiki;
    @FXML
    private Label subheadingWiki;
    @FXML
    private Label textWiki;
    @FXML
    private Label robopedia;

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

    @FXML
    private void initialize() throws IOException {
        if (buttonRules != null && buttonCards != null && buttonRobots != null) {
            // Set RoboRally font as soon as elements are initialized
            buttonRules.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 20));
            buttonCards.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 20));
            buttonRobots.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 20));
            buttonCourses.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 20));

            robopedia.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 80));

        }
        if (buttonRule != null && buttonCard != null && buttonRobot != null) {
            // Set RoboRally font as soon as elements are initialized
            buttonRule.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"),14));
            buttonCard.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 14));
            buttonRobot.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 14));
            buttonCourse.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 14));

        }

        if (headingWiki != null && subheadingWiki != null && textWiki != null) {
            // Set RoboRally font as soon as elements are initialized
            headingWiki.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 30));
            subheadingWiki.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/Roborally.ttf"), 20));
            textWiki.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/css/RoborallyRegular.ttf"), 15));
        }
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method handles button clicks inside the wiki stage to switch tabs by changing the
     * corresponding scenes.
     *
     * @param event
     * @throws IOException
     */

    @FXML
    public void buttonClicked(ActionEvent event) throws IOException {
        Parent root;

        if (event.getSource() == buttonCards) {
            this.rootStage = (Stage) buttonCards.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/views/WikiCards.fxml"));
            Scene scene = new Scene(root);
            this.rootStage.setScene(scene);
            this.rootStage.show();
        }
        if (event.getSource() == buttonRobots) {
            this.rootStage = (Stage) buttonRobots.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/views/WikiRobots.fxml"));
            Scene scene = new Scene(root);
            this.rootStage.setScene(scene);
            this.rootStage.show();
        }
        if (event.getSource() == buttonRules) {
            this.rootStage = (Stage) buttonRules.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/views/WikiRules.fxml"));
            Scene scene = new Scene(root);
            this.rootStage.setScene(scene);
            this.rootStage.show();
        }
        if (event.getSource() == buttonCourses) {
            this.rootStage = (Stage) buttonCourses.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/views/WikiCourses.fxml"));
            Scene scene = new Scene(root);
            this.rootStage.setScene(scene);
            this.rootStage.show();
        }
        if (event.getSource() == buttonCard) {
            this.rootStage = (Stage) buttonCard.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/views/WikiCards.fxml"));
            Scene scene = new Scene(root);
            this.rootStage.setScene(scene);
            this.rootStage.show();
        }
        if (event.getSource() == buttonRobot) {
            this.rootStage = (Stage) buttonRobot.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/views/WikiRobots.fxml"));
            Scene scene = new Scene(root);
            this.rootStage.setScene(scene);
            this.rootStage.show();
        }
        if (event.getSource() == buttonRule) {
            this.rootStage = (Stage) buttonRule.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/views/WikiRules.fxml"));
            Scene scene = new Scene(root);
            this.rootStage.setScene(scene);
            this.rootStage.show();
        }
        if (event.getSource() == buttonCourse) {
            this.rootStage = (Stage) buttonCourse.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/views/WikiCourses.fxml"));
            Scene scene = new Scene(root);
            this.rootStage.setScene(scene);
            this.rootStage.show();
        }
    }
}
