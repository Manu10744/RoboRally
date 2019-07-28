package server.game;

import javafx.scene.image.Image;
import server.game.decks.DeckDiscard;
import server.game.decks.DeckDraw;
import server.game.decks.DeckHand;
import server.game.decks.DeckRegister;

import java.io.Serializable;

import static utils.Parameter.HAND_CARDS_AMOUNT;
import static utils.Parameter.ORIENTATION_RIGHT;

/**
 * Each player will have most of the important attributes handled in an array.
 *
 * @author Ivan Dovecar
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 * @author Mia Brandtner
 */
public class Player implements Serializable {

    private int selectedCards;
    private String name;
    private int energy;
    private int playerID;
    private int figure;
    private int checkPointCounter = 0;
    private boolean isReady;
    private Robot playerRobot;
    private String color;

    private DeckDraw deckDraw;
    private DeckDiscard deckDiscard;
    private DeckHand deckHand;
    private DeckRegister deckRegister;

    private int currentRound;

    public Player() {
        this.name = "Findus";
        this.energy = 5;

        // Create decks, initialize draw deck
        this.deckDraw = new DeckDraw();
        deckDraw.initializeDeck();
        deckDraw.shuffleDeck();

        this.deckDiscard = new DeckDiscard();
        deckDiscard.initializeDeck();
        this.deckHand = new DeckHand();
        deckHand.initializeDeck();
        this.deckRegister = new DeckRegister();
        deckRegister.initializeDeck();
        this.selectedCards = 0;

    }

    public int getFigure() {
        return figure;
    }

    public void setFigure(int figure) {
        this.figure = figure;
    }

    /**
     * This method initialises the robot of a player with name and image as well as its setting the color o the player according to the
     * robot figure
     */
    public void initRobotByFigure(int figure) {

        Image robotImage;
        String name;
        if (figure == 1) {
            robotImage = new Image("/resources/images/robots/HammerBot.PNG");
            name = "HammerBot";
            this.color = "purple";
        } else if (figure == 2) {
            robotImage = new Image("/resources/images/robots/HulkX90.PNG");
            name = "HulkX90";
            this.color = "red";
        } else if (figure == 3) {
            robotImage = new Image("/resources/images/robots/SmashBot.PNG");
            name = "SmashBot";
            this.color = "yellow";
        } else if (figure == 4) {
            robotImage = new Image("/resources/images/robots/Spinbot.PNG");
            name = "SpinBot";
            this.color = "blue";
        } else if (figure == 5) {
            robotImage = new Image("/resources/images/robots/Twonky.PNG");
            name = "Twonky";
            this.color = "orange";
        } else { // figure == 6
            robotImage = new Image("/resources/images/robots/ZoomBot.PNG");
            name = "ZoomBot";
            this.color = "green";
        }

        // Set robot for this player
        this.playerRobot = new Robot(name, robotImage, ORIENTATION_RIGHT, 0, 0);
    }

    /**
     * This method is used for drawing cards from the deck. <br>
     * It draws Cards until the maximum amount of allowed hand cards is reached.
     */
    public void drawHandCards(DeckHand deckHand, DeckDraw deckDraw, DeckDiscard deckDiscard) {
        for (int i = 0; i < HAND_CARDS_AMOUNT; i++) {

            // Each time this checks if there are still enough Cards left in the deckDraw.
            if (deckDraw.getDeck().isEmpty()) {
                deckDiscard.addDiscardToDraw(deckDraw);
                deckDraw.shuffleDeck();
            }

            // The first Card of deckDraw is added to the deckHand.
            deckHand.getDeck().add(deckDraw.getTopCard());
            deckDraw.removeTopCard(deckDraw.getDeck());
        }
    }

    /**
     * This method clears the deckRegister after they have been played.
     */
    public void clearRegister(DeckRegister deckRegister) {
        deckRegister.getDeck().clear();
    }

    /**
     * This method is needed, if the deckDraw gets to small.
     */
    public void addDiscardToDraw(DeckDraw deckDraw, DeckDiscard deckDiscard) {
        deckDraw.getDeck().addAll(deckDiscard.getDeck());   // addAll() puts the elements of the list it is given into
        deckDiscard.getDeck().clear();                      // the list on which it is called.
    }

    /**
     * This method is used to add the deckHand to the deckDiscard.
     */
    public void addHandToDiscard(DeckDiscard deckDiscard, DeckHand deckHand) {
        deckDiscard.getDeck().addAll(deckHand.getDeck());
        deckHand.getDeck().clear();
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    /**
     * This method fills the registers with Cards from the deckDraw, if the timer ended. <br>
     * It is necessary to ask for all indices specifically because its possible, that the register at index 0 is empty <br>
     * while the others are already filled. <br>
     * The try catch is necessary because its not possible to look if an index is null as with normal arrays. <br>
     * This is because get() throws an exception instead of null.
     */
    public void fillRegisters(DeckRegister deckRegister, DeckDraw deckDraw) {

        // If the index at 0 is empty the first Card of deckDraw will be put there
        try {
            deckRegister.getDeck().get(0);
        } catch (IndexOutOfBoundsException e) {

            // In the case that the deckDraw has no Cards left in it, it will get new Cards from the deckDiscard.
            if (deckDraw.getDeck().isEmpty()) {
                addDiscardToDraw(deckDraw, deckDiscard);
                deckDraw.shuffleDeck();
            }

            deckRegister.getDeck().add(0, deckDraw.getTopCard());
            deckDraw.removeTopCard(deckDraw.getDeck());
        }

        // If the index at 1 is empty the first Card of deckDraw will be put there
        try {
            deckRegister.getDeck().get(1);
        } catch (IndexOutOfBoundsException e) {

            // In the case that the deckDraw has no Cards left in it, it will get new Cards from the deckDiscard.
            if (deckDraw.getDeck().isEmpty()) {
                addDiscardToDraw(deckDraw, deckDiscard);
                deckDraw.shuffleDeck();
            }

            deckRegister.getDeck().add(1, deckDraw.getTopCard());
            deckDraw.removeTopCard(deckDraw.getDeck());
        }

        // If the index at 2 is empty the first Card of deckDraw will be put there
        try {
            deckRegister.getDeck().get(2);
        } catch (IndexOutOfBoundsException e) {

            // In the case that the deckDraw has no Cards left in it, it will get new Cards from the deckDiscard.
            if (deckDraw.getDeck().isEmpty()) {
                addDiscardToDraw(deckDraw, deckDiscard);
                deckDraw.shuffleDeck();
            }

            deckRegister.getDeck().add(2, deckDraw.getTopCard());
            deckDraw.removeTopCard(deckDraw.getDeck());
        }

        // If the index at 3 is empty the first Card of deckDraw will be put there
        try {
            deckRegister.getDeck().get(3);
        } catch (IndexOutOfBoundsException e) {

            // In the case that the deckDraw has no Cards left in it, it will get new Cards from the deckDiscard.
            if (deckDraw.getDeck().isEmpty()) {
                addDiscardToDraw(deckDraw, deckDiscard);
                deckDraw.shuffleDeck();
            }

            deckRegister.getDeck().add(3, deckDraw.getTopCard());
            deckDraw.removeTopCard(deckDraw.getDeck());
        }

        // If the index at 4 is empty the first Card of deckDraw will be put there
        try {
            deckRegister.getDeck().get(4);
        } catch (IndexOutOfBoundsException e) {

            // In the case that the deckDraw has no Cards left in it, it will get new Cards from the deckDiscard.
            if (deckDraw.getDeck().isEmpty()) {
                addDiscardToDraw(deckDraw, deckDiscard);
                deckDraw.shuffleDeck();
            }

            deckRegister.getDeck().add(4, deckDraw.getTopCard());
            deckDraw.removeTopCard(deckDraw.getDeck());
        }
    }


    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public boolean isReady() {
        return isReady;
    }

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

    public int getEnergy() {
        return energy;
    }

    public DeckDraw getDeckDraw() {
        return deckDraw;
    }

    public DeckDiscard getDeckDiscard() {
        return deckDiscard;
    }

    public DeckHand getDeckHand() {
        return deckHand;
    }

    public DeckRegister getDeckRegister() {
        return deckRegister;
    }

    public void setDeckDraw(DeckDraw deckDraw) {
        this.deckDraw = deckDraw;
    }

    public void setDeckDiscard(DeckDiscard deckDiscard) {
        this.deckDiscard = deckDiscard;
    }

    public void setDeckHand(DeckHand deckHand) {
        this.deckHand = deckHand;
    }

    public void addCardToRegister(Card card) {
        this.deckRegister.getDeck().add(card);
    }

    public int getSelectedCards() {
        return this.selectedCards;
    }

    public void setSelectedCards(int selectedCards) {
        this.selectedCards = selectedCards;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public String getColor() {
        return color;
    }

    public int getCheckPointCounter() { return checkPointCounter; }

    public void setCheckPointCounter(int counter) {
        this.checkPointCounter = counter;
    }

}
