package server.game;

import javafx.scene.image.Image;
import server.game.decks.DeckDiscard;
import server.game.decks.DeckDraw;
import server.game.decks.DeckHand;
import server.game.decks.DeckRegister;

import java.io.Serializable;
import java.util.ArrayList;

import static utils.Parameter.ORIENTATION_RIGHT;

/**
 * Each player will have most of the important attributes handled in an array.
 *
 * @author Ivan Dovecar
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 */
public class Player implements Serializable {

    private String name;
    private int energy;
    private int playerID;
    private int figure;
    private boolean isReady;
    private Robot playerRobot;

    private DeckDraw deckDraw;
    private DeckDiscard deckDiscard;
    private DeckHand deckHand;
    private DeckRegister deckRegister;

    public Player(){
        this.deckDraw = new DeckDraw();
        deckDraw.initializeDeckDraw();

        this.deckDiscard = new DeckDiscard();
        this.deckHand = new DeckHand();
        this.deckRegister = new DeckRegister();
    }


    public int getFigure() {
        return figure;
    }

    // Initialises Robot by processing figure property
    public void initRobotByFigure(int figure) {
        // Set figure for this player
        this.figure = figure;

        Image robotImage;
        if (figure == 1) {
            robotImage= new Image("/resources/images/robots/HammerBot.PNG");
        } else if (figure == 2) {
            robotImage = new Image("/resources/images/robots/HulkX90.PNG");
        } else if (figure == 3) {
            robotImage = new Image("/resources/images/robots/SmashBot.PNG");
        } else if (figure == 4) {
            robotImage = new Image("/resources/images/robots/Spinbot.PNG");
        } else if (figure == 5) {
            robotImage = new Image("/resources/images/robots/Twonky.PNG");
        } else { // figure == 6
            robotImage = new Image("/resources/images/robots/ZoomBot.PNG");
        }

        // Set robot for this player
        this.playerRobot = new Robot(robotImage, ORIENTATION_RIGHT, 0, 0);
    }

    public int getPlayerID(){
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public boolean isReady() { return isReady; }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Robot getPlayerRobot() {
        return playerRobot;
    }

    public void setPlayerRobot(Robot playerRobot) {
        this.playerRobot = playerRobot;
    }
}
