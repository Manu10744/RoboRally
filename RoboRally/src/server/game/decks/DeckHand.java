package server.game.decks;

import server.game.Card;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckHand.
 *
 * @author Vincent Tafferner
 */
public class DeckHand {

    private ArrayList<Card> deckHand;

    /**
     * This method creates an empty deckHand.
     */
    public void initializeDeckHand() {
        this.deckHand = new ArrayList<>();
    }

    public ArrayList<Card> getDeckHand() {
        return deckHand;
    }
}
