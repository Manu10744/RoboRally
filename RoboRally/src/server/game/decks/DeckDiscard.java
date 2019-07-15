package server.game.decks;

import server.game.Card;
import server.game.Deck;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckDiscard.
 *
 * @author Vincent Tafferner
 */
public class DeckDiscard extends Deck {

    private ArrayList<Card> deckDiscard;

    /**
     * This method creates an empty deckDiscard.
     */
    @Override
    public void initializeDeck() {
        this.deckDiscard = new ArrayList<>();
    }

    /**
     * This method returns the deck.
     */
    @Override
    public ArrayList<Card> getDeck() {
        return deckDiscard;
    }
}
