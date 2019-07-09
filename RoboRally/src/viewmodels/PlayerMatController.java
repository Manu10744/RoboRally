package viewmodels;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.game.Card;
import server.game.decks.DeckDraw;
import utils.Parameter;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;


/**
 * This class has full control over the playermat views.
 * It is responsible for showing the player´s own icon,
 * the upgrade cards and tbe register of the player.
 *
 * @author Jessica Gerlach
 * @author Verena Sadtler
 * @author Mia Brandtner
 */

public class PlayerMatController implements IController {

    @FXML
    private HBox playerIcons;
    @FXML
    private HBox playerDrawDiscardCards;
    @FXML
    private HBox playerUpdates;
    @FXML
    private HBox playerRegister;

    @FXML
    private ImageView ownRobotIcon;
    @FXML
    private ImageView emptyIcon;
    @FXML
    private ImageView clockIcon;
    @FXML
    private ImageView emptyIcon2;
    @FXML
    private ImageView emptyIcon3;
    @FXML
    private ImageView emptyIcon4;
    @FXML
    private ImageView emptyIcon5;
    @FXML
    private ImageView emptyIcon6;

    @FXML
    private ImageView emptyIcon0;
    @FXML
    private ImageView emptyIcon01;
    @FXML
    private ImageView emptyIcon02;
    @FXML
    private ImageView emptyIcon03;
    @FXML
    private ImageView emptyIcon04;
    @FXML
    private ImageView emptyIcon05;
    @FXML
    private ImageView emptyIcon06;
    @FXML
    private ImageView emptyIcon07;

    @FXML
    private ImageView permaUpdate1;
    @FXML
    private ImageView permaUpdate2;
    @FXML
    private ImageView permaUpdate3;
    @FXML
    private ImageView emptyIcon00;
    @FXML
    private ImageView tempUpdate1;
    @FXML
    private ImageView tempUpdate2;
    @FXML
    private ImageView tempUpdate3;

    @FXML
    private ImageView register1;
    @FXML
    private ImageView register2;
    @FXML
    private ImageView register3;
    @FXML
    private ImageView register4;
    @FXML
    private ImageView register5;
    @FXML
    private ImageView emptyIcon20;
    @FXML
    private ImageView emptyIcon21;

    @FXML
    HBox cards;

    @FXML
    GridPane popupCards;

    private StageController stageController;

    public HBox getPlayerIcons() {
        return playerIcons;
    }

    public HBox getPlayerDrawDiscardCards() {
        return playerDrawDiscardCards;
    }

    public HBox getPlayerUpdates() {
        return playerUpdates;
    }

    public HBox getPlayerRegister() {
        return playerRegister;
    }

    public ImageView getOwnRobotIcon() {
        return ownRobotIcon;
    }

    public ImageView getEmptyIcon() {
        return emptyIcon;
    }

    public ImageView getClockIcon() {
        return clockIcon;
    }

    public ImageView getEmptyIcon2() {
        return emptyIcon2;
    }

    public ImageView getEmptyIcon3() {
        return emptyIcon3;
    }

    public ImageView getEmptyIcon4() {
        return emptyIcon4;
    }

    public ImageView getEmptyIcon5() {
        return emptyIcon5;
    }

    public ImageView getEmptyIcon6() {
        return emptyIcon6;
    }

    public ImageView getEmptyIcon0() {
        return emptyIcon0;
    }

    public ImageView getEmptyIcon01() {
        return emptyIcon01;
    }

    public ImageView getEmptyIcon02() {
        return emptyIcon02;
    }

    public ImageView getEmptyIcon03() {
        return emptyIcon03;
    }
    private Stage rootStage;





    /**
     * This method is called when the protocol "YouCards" is called. YourCards gives the DrawDeck which is utilised here to fill the PopUp-Window.
     * This method fulfills two functions: first, all the hboxes are made responsive, second the drag and drop method is envocked
     *
     * @param deck: The cards one draws for choosing their programming
     */

    public void openPopupCards(ArrayList<Card> deck) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                rootStage = new Stage();
                Parent root;

                    try {
                        root = FXMLLoader.load(getClass().getResource("/views/PopupCards.fxml"));
                        rootStage.setScene(new Scene(root));
                        rootStage.setAlwaysOnTop(true);
                        rootStage.initStyle(StageStyle.TRANSPARENT);
                        rootStage.setX(stageController.getPlayerMat().getLayoutX() + playerUpdates.getLayoutX() - register1.getFitWidth());
                        rootStage.setY(stageController.getPlayerMat().getLayoutY() + playerUpdates.getLayoutY());

                        rootStage.show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

        });

    }

    @FXML
    void onDragDetectedPopUp() {
        for (Node card : cards.getChildren()) {
            card.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Dragboard db = card.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                        content.putImage(((ImageView) card).getImage());
                        db.setContent(content);
                        event.consume();
                    }
            });
        }
    }

    @FXML
    void onDragOverRegister() {
        for (Node register : playerRegister.getChildren()) {
            register.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    if (dragEvent.getGestureSource() != register && dragEvent.getDragboard().hasImage()) {
                        dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    dragEvent.consume();

                }
            });
        }

    }

    @FXML
    void onDragDroppedRegister() {
        for (Node register : playerRegister.getChildren()) {
            register.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    Dragboard db = dragEvent.getDragboard();
                    boolean success = false;
                    if (db.hasImage()) {
                        if (((ImageView) register).getImage() == null) {
                            //Todo images are cut off when dragged to register
                            ((ImageView) register).setImage(db.getImage());
                            ((ImageView) register).setPreserveRatio(true);
                            ((ImageView) register).fitWidthProperty().bind(playerRegister.widthProperty().divide(Parameter.CARDS_WIDTH));
                            success = true;
                        }

                        if (register1.getImage() != null && register2.getImage() != null && register3.getImage() != null && register4.getImage() != null && register5.getImage() != null){
                            register1.setDisable(true);
                            register2.setDisable(true);
                            register3.setDisable(true);
                            register4.setDisable(true);
                            register5.setDisable(true);

                            //closes popup
                             rootStage.close();
                        }
                    }
                    dragEvent.setDropCompleted(success);
                    dragEvent.consume();
                }
            });


        }

    }

    @FXML
    void onDragDonePopUp() {
        for (Node card : cards.getChildren()) {
            card.setOnDragDone(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    if (dragEvent.getTransferMode() == TransferMode.MOVE) {
                            ((ImageView) card).setImage(null);
                    }
                    dragEvent.consume();
                }
            });
        }
    }

    @FXML
    void onDragDetectedRegister() {
        for (Node register : playerRegister.getChildren()) {
            register.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Dragboard db = register.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(((ImageView) register).getImage());
                    db.setContent(content);
                    event.consume();
                }
            });
        }
    }

    @FXML
    void onDragOverPopUp() {
        for (Node card : cards.getChildren()) {
            card.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    if (dragEvent.getGestureSource() != card && dragEvent.getDragboard().hasImage()) {
                        dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    dragEvent.consume();
                }
            });
        }
    }

    @FXML
    void onDragDroppedPopUp() {
        for (Node card : cards.getChildren()) {
            cards.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    Dragboard db = dragEvent.getDragboard();
                    boolean success = false;
                    if (db.hasImage()) {
                        ((ImageView) card).setImage(db.getImage());
                        success = true;
                    }
                    dragEvent.setDropCompleted(success);
                    dragEvent.consume();
                }
            });
        }
    }

    @FXML
    void onDragDoneRegister() {
        for (Node register : playerRegister.getChildren()) {
            register.setOnDragDone(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    if (dragEvent.getTransferMode() == TransferMode.MOVE) {
                            ((ImageView) register).setImage(null);

                    }
                    dragEvent.consume();
                }
            });
        }
    }


    @Override
        public IController setPrimaryController (StageController stageController){
            this.stageController = stageController;
            return this;
        }
    }
