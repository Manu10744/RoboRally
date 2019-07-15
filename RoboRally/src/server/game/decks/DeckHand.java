package server.game.decks;

import server.game.Card;
import server.game.Deck;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckHand.
 *
 * @author Vincent Tafferner
 */
public class DeckHand extends Deck {

    private ArrayList<Card> deckHand;

    /**
     * This method creates an empty deckHand.
     */
    @Override
    public void initializeDeck() {
        this.deckHand = new ArrayList<>();
    }

    /**
     * This method returns the deck.
     */
    @Override
    public ArrayList<Card> getDeck() {
        return deckHand;
    }
}
