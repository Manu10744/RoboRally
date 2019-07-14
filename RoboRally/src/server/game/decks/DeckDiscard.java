package server.game.decks;

import server.game.Card;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckDiscard.
 *
 * @author Vincent Tafferner
 */
public class DeckDiscard {

    private ArrayList<Card> deckDiscard;

    /**
     * This method creates an empty deckDiscard.
     */
    public void initializeDeckDiscard() {
        this.deckDiscard = new ArrayList<>();
    }

    public ArrayList<Card> getDeckDiscard() {
        return deckDiscard;
    }
}
