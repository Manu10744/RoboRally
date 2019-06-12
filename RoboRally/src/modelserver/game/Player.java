package modelserver.game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Each player will have most of the important attributes handled in an array.
 *
 * @author Ivan Dovecar
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 */
public class Player implements Serializable {

    private String name;
    private int playerID;
    private Card[] cardHand;
    private Card[] cardRegister;
    private ArrayList<Card> deckDiscard;
    private ArrayList<Card> deckDraw;


    /**
     * Player constructor
     */
    public Player(String name, int playerID) {
        this.name = name;
        this.playerID = playerID;
    }

    /**
     * Get the name of a player
     * @return name The name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Get the age of a player
     * @return age The age of a player
     */
    public int getPlayerID() {
        return this.playerID;
    }

    /**
     * Getter for the array the hand cards of a player are stored in
     * @return cardHand The cardHand of a player
     */
    public Card[] getCardHand() {
        return cardHand;
    }

    /**
     * Getter for the array the cards which are in the register of a player are stored in
     * @return cardRegister The cardRegister of a player
     */
    public Card[] getCardRegister() {
        return cardRegister;
    }

    /**
     * Getter for the array list the cards of the discard deck are stored in
     * @return deckDiscard The deckDiscard of a player
     */
    public ArrayList<Card> getDeckDiscard() {
        return deckDiscard;
    }

    /**
     * Getter for the array list the cards of the draw deck are stored in
     * @return deckDraw The deckDraw of a player
     */
    public ArrayList<Card> getDeckDraw() {
        return deckDraw;
    }
}