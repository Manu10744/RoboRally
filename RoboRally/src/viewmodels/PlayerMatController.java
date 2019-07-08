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
    private ImageView emptyIcon20;
    @FXML
    private ImageView emptyIcon21;

    @FXML
    HBox cards;
    @FXML
    GridPane popupCards;

    Stage rootStage;

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

    public ImageView getEmptyIcon04() {
        return emptyIcon04;
    }

    public ImageView getEmptyIcon05() {
        return emptyIcon05;
    }

    public ImageView getEmptyIcon06() {
        return emptyIcon06;
    }

    public ImageView getEmptyIcon07() {
        return emptyIcon07;
    }

    public ImageView getPermaUpdate1() {
        return permaUpdate1;
    }

    public ImageView getPermaUpdate2() {
        return permaUpdate2;
    }

    public ImageView getPermaUpdate3() {
        return permaUpdate3;
    }

    public ImageView getEmptyIcon00() {
        return emptyIcon00;
    }

    public ImageView getTempUpdate1() {
        return tempUpdate1;
    }

    public ImageView getTempUpdate2() {
        return tempUpdate2;
    }

    public ImageView getTempUpdate3() {
        return tempUpdate3;
    }

    public ImageView getRegister1() {
        return register1;
    }

    public ImageView getRegister2() {
        return register2;
    }

    public ImageView getRegister3() {
        return register3;
    }

    public ImageView getRegister4() {
        return register4;
    }

    public ImageView getRegister5() {
        return register5;
    }

    public ImageView getEmptyIcon20() {
        return emptyIcon20;
    }

    public ImageView getEmptyIcon21() {
        return emptyIcon21;
    }



    // only for testing --> usually the playermat isn't called wire the chatController, so for now we need a static
    // reference for the card popup that is initialized in the chatcontroller at the moment.
    static Stage stage;

    public static void setStage(Stage origStage){
        stage = origStage;
        stage.setAlwaysOnTop(true);
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



    private StageController stageController;
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
        this.stageController = stageController;
        return this;
    }
}
