package server.game.decks;

import server.game.Card;
import server.game.Deck;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;
import java.util.Collections;

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
     * This method shuffles the deck.
     */
    @Override
    public void shuffleDeck(ArrayList<Card> deckDiscard) {
        Collections.shuffle(deckDiscard);
    }

    /**
     * This method is needed, if the deckDraw gets to small.
     */
    public void addDiscardToDraw(DeckDraw deckDraw) {
        deckDraw.getDeck().addAll(deckDiscard);
        deckDiscard.clear();
    }

    /**
     * This method returns the deck.
     */
    @Override
    public ArrayList<Card> getDeck() {
        return deckDiscard;
    }
}
