package server.game;

import server.game.DamageCards.*;
import server.game.ProgrammingCards.*;
import server.game.decks.*;

import java.util.ArrayList;
import java.util.Collections;

import static utils.Parameter.*;

/**
 * This class is responsible for handling the Decks in the game. <br>
 * For this purpose it has all the methods that transform the decks. <br>
 * The decks are created in their own classes.
 *
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 */
public abstract class Deck {

    private Card topCard;

    /**
     * This method initialises decks.
     */
    public abstract void initializeDeck();

    /**
     * This method shuffles the deck.
     */
    public abstract void shuffleDeck();

    /**
     * This method knows the top card of any deck.
     */
    public Card getTopCard() {
        topCard = this.getDeck().get(0);
        return topCard;
    }

    /**
     * This method removes the Top Card of a deck.
     */
    public void removeTopCard(ArrayList<Card> Deck) {
        Deck.remove(0);
    }

    /**
     * This method is overwritten by every deck. <br>
     * It returns the deck.
     */
    public abstract ArrayList<Card> getDeck();

}
