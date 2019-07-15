package server.game;

import server.game.DamageCards.*;
import server.game.ProgrammingCards.*;
import server.game.GamePhases.*;
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
    public void shuffleDeck(ArrayList<Card> Deck) {
        Collections.shuffle(Deck);
    }

    /**
     * This method knows the top card of any deck.
     */
    public Card getTopCard(ArrayList<Card> Deck) {
        topCard = Deck.get(0);
        return topCard;
    }

    /**
     * This method removes the Top Card of a deck.
     */
    public void removeTopCard(ArrayList<Card> Deck) {
        Deck.remove(0);
    }

    /**
     * This method clears a Deck. For example you could clear the deckDiscard after you've added it to the deckDraw.
     */
    public void clearDeck(ArrayList<Card> Deck) {
        Deck.clear();
    }

    /**
     * This method can check if a Deck has Cards left in it.
     */
    public boolean deckEmpty(ArrayList<Card> Deck){
        return Deck.isEmpty();
    }

    /**
     * This method is overwritten by every deck. <br>
     * It returns the deck.
     */
    public abstract ArrayList<Card> getDeck();




}
