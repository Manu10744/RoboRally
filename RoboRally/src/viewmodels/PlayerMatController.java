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
 * @author Ivan Dovecar
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


    // Player Icons
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

    // Player Draw Discard Cards
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

    // Player Updates
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

    // Player Register
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

    // Following FXMLs are related to PopupCards which represent the players' hand
    @FXML
    GridPane popupCards;
    @FXML
    HBox cards;
    @FXML
    ImageView dragImage1;
    @FXML
    ImageView dragImage2;
    @FXML
    ImageView dragImage3;
    @FXML
    ImageView dragImage4;
    @FXML
    ImageView dragImage5;
    @FXML
    ImageView dragImage6;
    @FXML
    ImageView dragImage7;
    @FXML
    ImageView dragImage8;
    @FXML
    ImageView dragImage9;

    private Stage rootStage;
    private StageController stageController;

    BooleanProperty allRegistersSet;

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
                    root.getStylesheets().add("/resources/css/main.css");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *  Drag and drop procedure
     *
     *  onDragDetected
     *  The drag-and-drop gesture can only be started by calling the startDragAndDrop method inside the handler of the
     *  DRAG_DETECTED event on a gesture source.
     *
     *  onDragOver
     *  After the drag-and-drop gesture is started, any node or scene that the mouse is dragged over is a potential
     *  target to drop the data. You specify which object accepts the data by implementing the DRAG_OVER event handler.
     *
     *  onDragDropped
     *  When the mouse button is released on the gesture target, which accepted previous DRAG_OVER events with a transfer
     *  mode supported by the gesture source, then the DRAG_DROPPED event is sent to the gesture target.
     *
     *  ondDragDone
     *  After the drag-and-drop gesture is finished, the DRAG_DONE event is sent to the gesture source to inform the
     *  source about how the gesture finished.
     *
     *  @author Jessie
     *  @author Verena
     *  @author Mia
     *  @author Ivan
     */


    @FXML
    void onDragDetectedPopUp() {
        for (Node card : cards.getChildren()) {
            card.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(((ImageView) card).getImage());
                    db.setContent(content);
                    event.consume();
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
                    Dragboard db = register.startDragAndDrop(TransferMode.MOVE);
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
                        dragEvent.acceptTransferModes(TransferMode.MOVE);
                    }
                    dragEvent.consume();
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
                        dragEvent.acceptTransferModes(TransferMode.MOVE);
                    }
                    dragEvent.consume();
                }
            });
        }
    }



    @FXML
    void onDragDroppedPopUp() {
        for (Node card : cards.getChildren()) {
            card.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    Dragboard db = dragEvent.getDragboard();
                    boolean success = false;
                    if (db.hasImage()) {
                        if (((ImageView) card).getImage() == null) {
                            ((ImageView) card).setImage(db.getImage());
                            success = true;
                        }
                    }
                    dragEvent.setDropCompleted(success);
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
                            ((ImageView) register).setImage(db.getImage());
                            ((ImageView) register).setPreserveRatio(true);
                            ((ImageView) register).fitWidthProperty().bind(playerRegister.widthProperty().divide(Parameter.CARDS_WIDTH));
                            ((ImageView) register).fitHeightProperty().bind(playerRegister.heightProperty());
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

                            //Boolean Property for Server so that server knows when a player has finished their programming
                            allRegistersSet = new SimpleBooleanProperty();
                            allRegistersSet.setValue(true);
                        }
                    }
                    dragEvent.setDropCompleted(success);
                    dragEvent.consume();
                }
            });
        }
        System.out.println("INITIALIZING DRAGDROP EVENTS ON PLAYERREGISTERS");
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

    // Player Icons
    public ImageView getOwnRobotIcon() {
        ownRobotIcon.setPreserveRatio(true);
        ownRobotIcon.fitWidthProperty().bind(playerIcons.widthProperty());
        ownRobotIcon.fitHeightProperty().bind(playerIcons.heightProperty());
        return ownRobotIcon;
    }

    public ImageView getEmptyIcon() {
        emptyIcon.setPreserveRatio(true);
        emptyIcon.fitWidthProperty().bind(playerIcons.widthProperty());
        emptyIcon.fitHeightProperty().bind(playerIcons.heightProperty());
        return emptyIcon;
    }

    public ImageView getClockIcon() {
        clockIcon.setPreserveRatio(true);
        clockIcon.fitWidthProperty().bind(playerIcons.widthProperty());
        clockIcon.fitHeightProperty().bind(playerIcons.heightProperty());
        return clockIcon;
    }

    public ImageView getEmptyIcon2() {
        emptyIcon2.setPreserveRatio(true);
        emptyIcon2.fitWidthProperty().bind(playerIcons.widthProperty());
        emptyIcon2.fitHeightProperty().bind(playerIcons.heightProperty());
        return emptyIcon2;
    }

    public ImageView getEmptyIcon3() {
        emptyIcon3.setPreserveRatio(true);
        emptyIcon3.fitWidthProperty().bind(playerIcons.widthProperty());
        emptyIcon3.fitHeightProperty().bind(playerIcons.heightProperty());
        return emptyIcon3;
    }

    public ImageView getEmptyIcon4() {
        emptyIcon4.setPreserveRatio(true);
        emptyIcon4.fitWidthProperty().bind(playerIcons.widthProperty());
        emptyIcon4.fitHeightProperty().bind(playerIcons.heightProperty());
        return emptyIcon4;
    }

    public ImageView getEmptyIcon5() {
        emptyIcon5.setPreserveRatio(true);
        emptyIcon5.fitWidthProperty().bind(playerIcons.widthProperty());
        emptyIcon5.fitHeightProperty().bind(playerIcons.heightProperty());
        return emptyIcon5;
    }

    public ImageView getEmptyIcon6() {
        emptyIcon6.setPreserveRatio(true);
        emptyIcon6.fitWidthProperty().bind(playerIcons.widthProperty());
        emptyIcon6.fitHeightProperty().bind(playerIcons.heightProperty());
        return emptyIcon6;
    }


    // Player Draw Discard Cards

    public ImageView getEmptyIcon0() {
        emptyIcon0.setPreserveRatio(true);
        emptyIcon0.fitWidthProperty().bind(playerDrawDiscardCards.widthProperty());
        emptyIcon0.fitHeightProperty().bind(playerDrawDiscardCards.heightProperty());
        return emptyIcon0;
    }

    public ImageView getEmptyIcon01() {
        emptyIcon01.setPreserveRatio(true);
        emptyIcon01.fitWidthProperty().bind(playerDrawDiscardCards.widthProperty());
        emptyIcon01.fitHeightProperty().bind(playerDrawDiscardCards.heightProperty());
        return emptyIcon01;
    }

    public ImageView getEmptyIcon02() {
        emptyIcon02.setPreserveRatio(true);
        emptyIcon02.fitWidthProperty().bind(playerDrawDiscardCards.widthProperty());
        emptyIcon02.fitHeightProperty().bind(playerDrawDiscardCards.heightProperty());
        return emptyIcon02;
    }

    public ImageView getEmptyIcon03() {
        emptyIcon03.setPreserveRatio(true);
        emptyIcon03.fitWidthProperty().bind(playerDrawDiscardCards.widthProperty());
        emptyIcon03.fitHeightProperty().bind(playerDrawDiscardCards.heightProperty());
        return emptyIcon03;
    }


    public ImageView getEmptyIcon04() {
        emptyIcon04.setPreserveRatio(true);
        emptyIcon04.fitWidthProperty().bind(playerDrawDiscardCards.widthProperty());
        emptyIcon04.fitHeightProperty().bind(playerDrawDiscardCards.heightProperty());
        return emptyIcon04;
    }


    public ImageView getEmptyIcon05() {
        emptyIcon05.setPreserveRatio(true);
        emptyIcon05.fitWidthProperty().bind(playerDrawDiscardCards.widthProperty());
        emptyIcon05.fitHeightProperty().bind(playerDrawDiscardCards.heightProperty());
        return emptyIcon05;
    }


    public ImageView getEmptyIcon06() {
        emptyIcon06.setPreserveRatio(true);
        emptyIcon06.fitWidthProperty().bind(playerDrawDiscardCards.widthProperty());
        emptyIcon06.fitHeightProperty().bind(playerDrawDiscardCards.heightProperty());
        return emptyIcon06;
    }


    public ImageView getEmptyIcon07() {
        emptyIcon07.setPreserveRatio(true);
        emptyIcon07.fitWidthProperty().bind(playerDrawDiscardCards.widthProperty());
        emptyIcon07.fitHeightProperty().bind(playerDrawDiscardCards.heightProperty());
        return emptyIcon07;
    }


    // Player Updates

    public ImageView getPermaUpdate1() {
        permaUpdate1.setPreserveRatio(true);
        permaUpdate1.fitWidthProperty().bind(playerUpdates.widthProperty());
        permaUpdate1.fitHeightProperty().bind(playerUpdates.heightProperty());
        return permaUpdate1;
    }

    public ImageView getPermaUpdate2() {
        permaUpdate2.setPreserveRatio(true);
        permaUpdate2.fitWidthProperty().bind(playerUpdates.widthProperty());
        permaUpdate2.fitHeightProperty().bind(playerUpdates.heightProperty());
        return permaUpdate2;
    }

    public ImageView getPermaUpdate3() {
        permaUpdate3.setPreserveRatio(true);
        permaUpdate3.fitWidthProperty().bind(playerUpdates.widthProperty());
        permaUpdate3.fitHeightProperty().bind(playerUpdates.heightProperty());
        return permaUpdate3;
    }

    public ImageView getEmptyIcon00() {
        emptyIcon00.setPreserveRatio(true);
        emptyIcon00.fitWidthProperty().bind(playerUpdates.widthProperty());
        emptyIcon00.fitHeightProperty().bind(playerUpdates.heightProperty());
        return emptyIcon00;
    }

    public ImageView getTempUpdate1() {
        tempUpdate1.setPreserveRatio(true);
        tempUpdate1.fitWidthProperty().bind(playerUpdates.widthProperty());
        tempUpdate1.fitHeightProperty().bind(playerUpdates.heightProperty());
        return tempUpdate1;
    }

    public ImageView getTempUpdate2() {
        tempUpdate2.setPreserveRatio(true);
        tempUpdate2.fitWidthProperty().bind(playerUpdates.widthProperty());
        tempUpdate2.fitHeightProperty().bind(playerUpdates.heightProperty());
        return tempUpdate2;
    }

    public ImageView getTempUpdate3() {
        tempUpdate3.setPreserveRatio(true);
        tempUpdate3.fitWidthProperty().bind(playerUpdates.widthProperty());
        tempUpdate3.fitHeightProperty().bind(playerUpdates.heightProperty());
        return tempUpdate3;
    }


    @Override
    public IController setPrimaryController (StageController stageController){
        this.stageController = stageController;
        return this;
    }
}