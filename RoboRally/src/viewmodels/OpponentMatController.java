package viewmodels;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import server.game.Player;
import server.game.Robot;

import java.util.ArrayList;

/**
 * This class has full control over the opponentMat views.
 * It is responsible for showing the playerHand in the register of each player,
 * the update playerHand of each player and the icons of the players.
 *
 * @author Jessica Gerlach
 * @author Ivan Dovecar
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

    /**
     * This method shows that an opponent player with the player id playerID has laid a card (backside visible) into the register register
     *
     * @param register
     * @param playerID
     */
    public void updateOpponentRegister(int register, int playerID) {
        //Todo show cards laid down on opponent map
        ArrayList<HBox> hBoxes = new ArrayList<>();
        hBoxes.add(player1Register);
        hBoxes.add(player2Register);
        hBoxes.add(player3Register);
        hBoxes.add(player4Register);
        hBoxes.add(player5Register);


        for (HBox hbox : hBoxes) {

            hbox.getId();
        }
    }

    /**
     * This method gets a robot from an opposing player and the list with all otherPlayres
     * and sets for them their icon, it is not already taken
     *
     * @param otherPlayers
     * @param opponentPlayerRobot
     */
    public void initOtherPlayerIcon(ArrayList<Player> otherPlayers, Robot opponentPlayerRobot) {
        ArrayList<ImageView> opponentPlayerIcons = new ArrayList<>();
        opponentPlayerIcons.add(player1Icon);
        opponentPlayerIcons.add(player2Icon);
        opponentPlayerIcons.add((player3Icon));
        opponentPlayerIcons.add(player4Icon);
        opponentPlayerIcons.add(player5Icon);


        for (int i = 0; i < otherPlayers.size(); i++) {
            System.out.println("Hast du das icon geupdated");
            ImageView playerIcon = opponentPlayerIcons.get(i);
            playerIcon.setPreserveRatio(true);
            playerIcon.fitWidthProperty().bind(player1Register.widthProperty());
            playerIcon.fitHeightProperty().bind(player1Register.heightProperty());

            if (playerIcon.getImage() == null) {

                switch (opponentPlayerRobot.getName()) {
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

    public HBox getPlayer1Register() {
        return player1Register;
    }

    public void setPlayer1Register(HBox player1Register) {
        this.player1Register = player1Register;
    }

    public HBox getPlayer2Register() {
        return player2Register;
    }

    public void setPlayer2Register(HBox player2Register) {
        this.player2Register = player2Register;
    }

    public HBox getPlayer3Register() {
        return player3Register;
    }

    public void setPlayer3Register(HBox player3Register) {
        this.player3Register = player3Register;
    }

    public HBox getPlayer4Register() {
        return player4Register;
    }

    public void setPlayer4Register(HBox player4Register) {
        this.player4Register = player4Register;
    }

    public HBox getPlayer5Register() {
        return player5Register;
    }

    public void setPlayer5Register(HBox player5Register) {
        this.player5Register = player5Register;
    }

    public ImageView getPlayer1Icon() {
        return player1Icon;
    }

    public void setPlayer1Icon(ImageView player1Icon) {
        this.player1Icon = player1Icon;
    }

    public ImageView getPlayer1Reg1() {
        return player1Reg1;
    }

    public void setPlayer1Reg1(ImageView player1Reg1) {
        this.player1Reg1 = player1Reg1;
    }

    public ImageView getPlayer1Reg2() {
        return player1Reg2;
    }

    public void setPlayer1Reg2(ImageView player1Reg2) {
        this.player1Reg2 = player1Reg2;
    }

    public ImageView getPlayer1Reg3() {
        return player1Reg3;
    }

    public void setPlayer1Reg3(ImageView player1Reg3) {
        this.player1Reg3 = player1Reg3;
    }

    public ImageView getPlayer1Reg4() {
        return player1Reg4;
    }

    public void setPlayer1Reg4(ImageView player1Reg4) {
        this.player1Reg4 = player1Reg4;
    }

    public ImageView getPlayer1Reg5() {
        return player1Reg5;
    }

    public void setPlayer1Reg5(ImageView player1Reg5) {
        this.player1Reg5 = player1Reg5;
    }

    public ImageView getPlayer2Icon() {
        return player2Icon;
    }

    public void setPlayer2Icon(ImageView player2Icon) {
        this.player2Icon = player2Icon;
    }

    public ImageView getPlayer2Reg1() {
        return player2Reg1;
    }

    public void setPlayer2Reg1(ImageView player2Reg1) {
        this.player2Reg1 = player2Reg1;
    }

    public ImageView getPlayer2Reg2() {
        return player2Reg2;
    }

    public void setPlayer2Reg2(ImageView player2Reg2) {
        this.player2Reg2 = player2Reg2;
    }

    public ImageView getPlayer2Reg3() {
        return player2Reg3;
    }

    public void setPlayer2Reg3(ImageView player2Reg3) {
        this.player2Reg3 = player2Reg3;
    }

    public ImageView getPlayer2Reg4() {
        return player2Reg4;
    }

    public void setPlayer2Reg4(ImageView player2Reg4) {
        this.player2Reg4 = player2Reg4;
    }

    public ImageView getPlayer2Reg5() {
        return player2Reg5;
    }

    public void setPlayer2Reg5(ImageView player2Reg5) {
        this.player2Reg5 = player2Reg5;
    }

    public ImageView getPlayer3Icon() {
        return player3Icon;
    }

    public void setPlayer3Icon(ImageView player3Icon) {
        this.player3Icon = player3Icon;
    }

    public ImageView getPlayer3Reg1() {
        return player3Reg1;
    }

    public void setPlayer3Reg1(ImageView player3Reg1) {
        this.player3Reg1 = player3Reg1;
    }

    public ImageView getPlayer3Reg2() {
        return player3Reg2;
    }

    public void setPlayer3Reg2(ImageView player3Reg2) {
        this.player3Reg2 = player3Reg2;
    }

    public ImageView getPlayer3Reg3() {
        return player3Reg3;
    }

    public void setPlayer3Reg3(ImageView player3Reg3) {
        this.player3Reg3 = player3Reg3;
    }

    public ImageView getPlayer3Reg4() {
        return player3Reg4;
    }

    public void setPlayer3Reg4(ImageView player3Reg4) {
        this.player3Reg4 = player3Reg4;
    }

    public ImageView getPlayer3Reg5() {
        return player3Reg5;
    }

    public void setPlayer3Reg5(ImageView player3Reg5) {
        this.player3Reg5 = player3Reg5;
    }

    public ImageView getPlayer4Icon() {
        return player4Icon;
    }

    public void setPlayer4Icon(ImageView player4Icon) {
        this.player4Icon = player4Icon;
    }

    public ImageView getPlayer4Reg1() {
        return player4Reg1;
    }

    public void setPlayer4Reg1(ImageView player4Reg1) {
        this.player4Reg1 = player4Reg1;
    }

    public ImageView getPlayer4Reg2() {
        return player4Reg2;
    }

    public void setPlayer4Reg2(ImageView player4Reg2) {
        this.player4Reg2 = player4Reg2;
    }

    public ImageView getPlayer4Reg3() {
        return player4Reg3;
    }

    public void setPlayer4Reg3(ImageView player4Reg3) {
        this.player4Reg3 = player4Reg3;
    }

    public ImageView getPlayer4Reg4() {
        return player4Reg4;
    }

    public void setPlayer4Reg4(ImageView player4Reg4) {
        this.player4Reg4 = player4Reg4;
    }

    public ImageView getPlayer4Reg5() {
        return player4Reg5;
    }

    public void setPlayer4Reg5(ImageView player4Reg5) {
        this.player4Reg5 = player4Reg5;
    }

    public ImageView getPlayer5Icon() {
        return player5Icon;
    }

    public void setPlayer5Icon(ImageView player5Icon) {
        this.player5Icon = player5Icon;
    }

    public ImageView getPlayer5Reg1() {
        return player5Reg1;
    }

    public void setPlayer5Reg1(ImageView player5Reg1) {
        this.player5Reg1 = player5Reg1;
    }

    public ImageView getPlayer5Reg2() {
        return player5Reg2;
    }

    public void setPlayer5Reg2(ImageView player5Reg2) {
        this.player5Reg2 = player5Reg2;
    }

    public ImageView getPlayer5Reg3() {
        return player5Reg3;
    }

    public void setPlayer5Reg3(ImageView player5Reg3) {
        this.player5Reg3 = player5Reg3;
    }

    public ImageView getPlayer5Reg4() {
        return player5Reg4;
    }

    public void setPlayer5Reg4(ImageView player5Reg4) {
        this.player5Reg4 = player5Reg4;
    }

    public ImageView getPlayer5Reg5() {
        return player5Reg5;
    }

    public void setPlayer5Reg5(ImageView player5Reg5) {
        this.player5Reg5 = player5Reg5;
    }

    @Override
    public IController setPrimaryController(StageController stageController) {
        return this;
    }


}
