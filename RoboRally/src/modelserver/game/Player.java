package src.modelserver.game;

import java.util.ArrayList;
import src.modelserver.game.Card;

/**
 * Each player will have most of the important attributes handled in an array.
 *
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 */
public class Player {

    private String name;
    private int age;
    private Card[] cardHand;
    private Card[] cardRegister;
    private ArrayList<Card> deckDiscard;
    private ArrayList<Card> deckDraw;


    /**
     * Get the name of a player
     * @return name The name of the player
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the age of a player
     * @return age The age of a player
     */
    public int getAge() {
        return age;
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
