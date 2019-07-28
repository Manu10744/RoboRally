package viewmodels;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import server.game.Card;
import server.game.Player;
import server.game.Robot;
import utils.Parameter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This class has full control over the opponentMat views.
 * It is responsible for showing the playerHand in the register of each player,
 * the update playerHand of each player and the icons of the players.
 *
 * @author Jessica Gerlach
 * @author Ivan Dovecar
 * @author Mia
 */

public class OpponentMatController implements IController {

    @FXML
    private GridPane opponentMat;
    @FXML
    private HBox player1Register;
    @FXML
    private HBox player2Register;
    @FXML
    private HBox player3Register;
    @FXML
    private HBox player4Register;
    @FXML
    private HBox player5Register;

    @FXML
    private HBox player1Updates;
    @FXML
    private HBox player2Updates;
    @FXML
    private HBox player3Updates;
    @FXML
    private HBox player4Updates;
    @FXML
    private HBox player5Updates;

    @FXML
    private ImageView player1Icon;
    @FXML
    private ImageView player1Reg1;
    @FXML
    private ImageView player1Reg2;
    @FXML
    private ImageView player1Reg3;
    @FXML
    private ImageView player1Reg4;
    @FXML
    private ImageView player1Reg5;

    @FXML
    private ImageView player1TempUpdates1;
    @FXML
    private ImageView player1TempUpdates2;
    @FXML
    private ImageView player1TempUpdates3;
    @FXML
    private ImageView player1PermUpdates1;
    @FXML
    private ImageView player1PermUpdates2;
    @FXML
    private ImageView player1PermUpdates3;

    @FXML
    private ImageView player2Icon;
    @FXML
    private ImageView player2Reg1;
    @FXML
    private ImageView player2Reg2;
    @FXML
    private ImageView player2Reg3;
    @FXML
    private ImageView player2Reg4;
    @FXML
    private ImageView player2Reg5;

    @FXML
    private ImageView player2TempUpdates1;
    @FXML
    private ImageView player2TempUpdates2;
    @FXML
    private ImageView player2TempUpdates3;
    @FXML
    private ImageView player2PermUpdates1;
    @FXML
    private ImageView player2PermUpdates2;
    @FXML
    private ImageView player2PermUpdates3;

    @FXML
    private ImageView player3Icon;
    @FXML
    private ImageView player3Reg1;
    @FXML
    private ImageView player3Reg2;
    @FXML
    private ImageView player3Reg3;
    @FXML
    private ImageView player3Reg4;
    @FXML
    private ImageView player3Reg5;

    @FXML
    private ImageView player3TempUpdates1;
    @FXML
    private ImageView player3TempUpdates2;
    @FXML
    private ImageView player3TempUpdates3;
    @FXML
    private ImageView player3PermUpdates1;
    @FXML
    private ImageView player3PermUpdates2;
    @FXML
    private ImageView player3PermUpdates3;

    @FXML
    private ImageView player4Icon;
    @FXML
    private ImageView player4Reg1;
    @FXML
    private ImageView player4Reg2;
    @FXML
    private ImageView player4Reg3;
    @FXML
    private ImageView player4Reg4;
    @FXML
    private ImageView player4Reg5;

    @FXML
    private ImageView player4TempUpdates1;
    @FXML
    private ImageView player4TempUpdates2;
    @FXML
    private ImageView player4TempUpdates3;
    @FXML
    private ImageView player4PermUpdates1;
    @FXML
    private ImageView player4PermUpdates2;
    @FXML
    private ImageView player4PermUpdates3;

    @FXML
    private ImageView player5Icon;
    @FXML
    private ImageView player5Reg1;
    @FXML
    private ImageView player5Reg2;
    @FXML
    private ImageView player5Reg3;
    @FXML
    private ImageView player5Reg4;
    @FXML
    private ImageView player5Reg5;

    @FXML
    private ImageView player5TempUpdates1;
    @FXML
    private ImageView player5TempUpdates2;
    @FXML
    private ImageView player5TempUpdates3;
    @FXML
    private ImageView player5PermUpdates1;
    @FXML
    private ImageView player5PermUpdates2;
    @FXML
    private ImageView player5PermUpdates3;

    private ArrayList<HBox> opponentPlayerRegisters = new ArrayList<>();

    private HashMap<Integer, HBox> registerMap = new HashMap<>();


    /* TODO
        ************************************************************************
        * Here is an example code to make the ImageViews fully responsive!!    *
        * Just adapt the necessary values:                                     *
        *   - image ID  -> here: player1Icon                                   *
        *   - HBox ID   -> here: player1Register                               *
        * The image views' height and width are bind to the HBox which         *
        * contains the image views and thereby to its' Values!! Enjoy, Ivan    *
        ************************************************************************
        public ImageView getPlayer1Icon() {
            player1Icon.setPreserveRatio(true);
            player1Icon.fitWidthProperty().bind(player1Register.widthProperty());
            player1Icon.fitHeightProperty().bind(player1Register.heightProperty());
            return player1Icon;
         }
     */
    private static final Logger logger = Logger.getLogger(viewmodels.OpponentMatController.class.getName());


    /**
     * This method gets a robot from an opposing player and the list with all otherPlayres
     * and sets for them their icon, it is not already taken
     *
     * @param otherPlayers
     * @param opponentPlayer
     */
    public void initOtherPlayerIcon(ArrayList<Player> otherPlayers, Player opponentPlayer) {

        //eventually this part in initialise
        ArrayList<ImageView> opponentPlayerIcons = new ArrayList<>();
        opponentPlayerIcons.add(player1Icon);
        opponentPlayerIcons.add(player2Icon);
        opponentPlayerIcons.add((player3Icon));
        opponentPlayerIcons.add(player4Icon);
        opponentPlayerIcons.add(player5Icon);


        opponentPlayerRegisters.add(player1Register);
        opponentPlayerRegisters.add(player2Register);
        opponentPlayerRegisters.add(player3Register);
        opponentPlayerRegisters.add(player4Register);
        opponentPlayerRegisters.add(player5Register);


        // player1Register.getChildren().setAll(player1Reg1, player1Reg2, player1Reg3, player1Reg4, player1Reg5);

        for (int i = 0; i < otherPlayers.size(); i++) {
            ImageView playerIcon = opponentPlayerIcons.get(i);
            playerIcon.setPreserveRatio(true);
            playerIcon.fitWidthProperty().bind(player1Register.widthProperty());
            playerIcon.fitHeightProperty().bind(player1Register.heightProperty());

            if (playerIcon.getImage() == null) {
                {
                    registerMap.put(opponentPlayer.getPlayerID(), opponentPlayerRegisters.get(i));
                    logger.info("OPPONENTMATCONTROLLER: REGISTER " + opponentPlayerRegisters.get(i).getId() + " HAS BEEN ADDED TO REGISTERMAP FOR PLAYER WITH ID" + opponentPlayer.getPlayerID());

                    switch (opponentPlayer.getPlayerRobot().getName()) {
                        case "HammerBot": {
                            Image avatar = new Image("/resources/images/robots/choose-robot-hammerbot.png");
                            playerIcon.setImage(avatar);
                            break;
                        }
                        case "HulkX90": {
                            Image avatar = new Image("/resources/images/robots/choose-robot-hulkX90.png");
                            playerIcon.setImage(avatar);
                            break;
                        }
                        case "SmashBot": {
                            Image avatar = new Image("/resources/images/robots/choose-robot-smashbot.png");
                            playerIcon.setImage(avatar);
                            break;
                        }
                        case "SpinBot": {
                            Image avatar = new Image("/resources/images/robots/choose-robot-spinbot.png");
                            playerIcon.setImage(avatar);
                            break;
                        }
                        case "Twonky": {
                            Image avatar = new Image("/resources/images/robots/choose-robot-twonky.png");
                            playerIcon.setImage(avatar);
                            break;
                        }
                        case "ZoomBot": {
                            Image avatar = new Image("/resources/images/robots/choose-robot-zoombot.png");
                            playerIcon.setImage(avatar);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void updateOpponentRegister(ArrayList<Player> opponentPlayers, int registerID, Player opponentPlayer) {
        Image cardBackImage = new Image("/resources/images/cards/card-back50x50.png");
        int playerID = opponentPlayer.getPlayerID();
        //updating imageView

        //Making the imageView responsive
        for (Player player : opponentPlayers) {
            if (playerID == player.getPlayerID()) {
                HBox opponentPlayerRegister = registerMap.get(playerID);

                /*
                In the following we do not have to decrement the registerID even though it is an index,
                because there are six imageViews in register HBox: 1 for the icon, and 5 for the registers, so we start at 1 with register 1 (0 would be the icon)
                 */

                //Check if there is already a card in register - if so, delete
                if (((ImageView) opponentPlayerRegister.getChildren().get(registerID)).getImage() != null) {
                    ((ImageView) opponentPlayerRegister.getChildren().get(registerID)).setImage(null);
                    logger.info("(OPPONENTCONTROLLERMAP): OPPONENT PLAYER " + playerID + " RETURNED CARD FROM REGISTER " + opponentPlayerRegister.getId());

                } else {
                    ((ImageView) opponentPlayerRegister.getChildren().get(registerID)).setImage(cardBackImage);

                    //make imageViews responsive
                    ((ImageView) opponentPlayerRegister.getChildren().get(registerID)).setPreserveRatio(true);
                    ((ImageView) opponentPlayerRegister.getChildren().get(registerID)).fitWidthProperty().bind(player1Register.widthProperty());
                    ((ImageView) opponentPlayerRegister.getChildren().get(registerID)).fitHeightProperty().bind(player1Register.heightProperty());
                    logger.info("( OPPONENTMAPCONTROLLER ): OPPONENT PLAYER " + playerID + " SELECTED CARD INTO REGISTER " + opponentPlayerRegister.getId());
                }
            }
        }

    }

    public ArrayList<Integer> getEmptyRegisterNumbersFromOpponents(Player opponentPlayer) {
        ArrayList<Integer> emptyOpponentRegisters = new ArrayList();

        for (int register = Parameter.REGISTER_ONE; register <= Parameter.REGISTER_FIVE; register++) {
            if (((ImageView) registerMap.get(opponentPlayer.getPlayerID()).getChildren().get(register)).getImage() == null) {
                emptyOpponentRegisters.add(register);

                logger.info("OPPONENTPLAYERMAT: NEW EMPTY REGISTER " + register + " HAS BEEN ADDED TO EMPTY REGISTERS " + emptyOpponentRegisters);
            }
        }

        return emptyOpponentRegisters;
    }

    /**
     * This method shows the current register during the active phase. It is used in handleCurrentCards
     * * @param opponentPlayer
     *
     * @param card
     * @param activeRegister
     */
    public void showCurrentOpponentRegisters(Player opponentPlayer, Card card, int activeRegister) {
        Image cardImage;
        String color = opponentPlayer.getColor();
        String cardName = card.getCardName();

        int playerID = opponentPlayer.getPlayerID();
        System.out.println("Register of other player is " + registerMap.get(playerID));

        //The right image and color for card to be displayed are chosen
        switch (cardName) {
            case "Again": {
                cardImage = new Image("/resources/images/cards/again-" + color + "50x50.png");
                //image of current register is shown in right color
                ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).setImage(cardImage);
                break;
            }
            case "BackUp": {
                cardImage = new Image("/resources/images/cards/moveback-" + color + "50x50.png");
                //image of current register is shown in right color
                ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).setImage(cardImage);
                break;
            }
            case "MoveI": {
                cardImage = new Image("/resources/images/cards/move1-" + color + "50x50.png");
                //image of current register is shown in right color
                ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).setImage(cardImage);
                break;
            }
            case "MoveII": {
                cardImage = new Image("/resources/images/cards/move2-" + color + "50x50.png");
                //image of current register is shown in right color
                ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).setImage(cardImage);
                break;
            }
            case "MoveIII": {
                cardImage = new Image("/resources/images/cards/move3-" + color + "50x50.png");
                //image of current register is shown in right color
                ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).setImage(cardImage);
                break;
            }
            case "PowerUp": {
                cardImage = new Image("/resources/images/cards/powerup-" + color + "50x50.png");
                //image of current register is shown in right color
                ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).setImage(cardImage);
                break;
            }
            case "TurnLeft": {
                cardImage = new Image("/resources/images/cards/leftturn-" + color + "50x50.png");
                //image of current register is shown in right color
                ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).setImage(cardImage);
                break;
            }
            case "TurnRight": {
                cardImage = new Image("/resources/images/cards/rightturn-" + color + "50x50.png");
                //image of current register is shown in right color
                ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).setImage(cardImage);
                break;
            }
            case "UTurn": {
                cardImage = new Image("/resources/images/cards/uturn-" + color + "50x50.png");
                //image of current register is shown in right color
                ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).setImage(cardImage);
                break;
            }
        }
        //making current cards responsive
        ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).setPreserveRatio(true);
        ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).fitWidthProperty().bind(player1Register.widthProperty());
        ((ImageView) registerMap.get(playerID).getChildren().get(activeRegister)).fitHeightProperty().bind(player1Register.heightProperty());

    }

    /**
     * This method empties the register of all opponent players. It is used by handle currentcard
     *
     * @param opponentPlayers
     */
    public void emptyRegisters(ArrayList<Player> opponentPlayers) {
        //when the last round has been played, registers are emptoed
        for (Player opponentPlayer : opponentPlayers) {
            int i = 1;
            while (i <= Parameter.REGISTER_FIVE) {
                ((ImageView) registerMap.get(opponentPlayer.getPlayerID()).getChildren().get(i)).setImage(null);
                i++;
            }
        }
    }

    @Override
    public IController setPrimaryController(StageController stageController) {
        return this;
    }


}
