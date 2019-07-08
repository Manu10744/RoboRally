package viewmodels;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;


/**
 * This class has full control over the playermat views.
 * It is responsible for showing the player´s own icon,
 * the upgrade cards and tbe register of the player.
 *
 * @author Jessica Gerlach
 * @author Verena Sadtler
 * @author Mia Brandtner
 */

public class PlayerMatController implements  IController{

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
    HBox cards;

    @FXML
    GridPane popupCards;

    // only for testing --> usually the playermat isn't called wire the chatController, so for now we need a static
    // reference for the card popup that is initialized in the chatcontroller at the moment.
    static Stage stage;

    public static void setStage(Stage origStage){
        stage = origStage;
    }

    @FXML
    void onDragDetectedPopUp() {
        for (Node card: cards.getChildren()) {
            card.setOnDragDetected(new EventHandler<MouseEvent>(){
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
                            ((ImageView) register).setImage(db.getImage());

                            System.out.println("Test image? " + ((ImageView) register).getImage());
                            success = true;
                        }
                        if (register1.getImage() != null && register2.getImage() != null && register3.getImage() != null && register4.getImage() != null && register5.getImage() != null ) {
                           stage.close();
                            register1.setDisable(true);
                            register2.setDisable(true);
                            register3.setDisable(true);
                            register4.setDisable(true);
                            register5.setDisable(true);
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
        for (Node register: playerRegister.getChildren()) {
            register.setOnDragDetected(new EventHandler<MouseEvent>(){
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
    public IController setPrimaryController(StageController stageController) {
        return this;
    }
}
