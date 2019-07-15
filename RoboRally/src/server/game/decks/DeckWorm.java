package server.game.decks;

import server.game.Card;
import server.game.DamageCards.Worm;

import java.util.ArrayList;

import static utils.Parameter.WORM_CARDS_AMOUNT;

/**
 * This class implements the deckWorm.
 *
 * @author Vincent Tafferner
 */
public class DeckWorm {

    private ArrayList<Card> deckWorm;

    /**
     * This method initializes the deck of Worm cards.
     */
    public void initializeDeckWorm() {
        this.deckWorm = new ArrayList<>();

        for (int i = 0; i < WORM_CARDS_AMOUNT; i++) {
            deckWorm.add(new Worm());
        }
    }

    public ArrayList<Card> getDeckWorm() {
        return deckWorm;
    }
}
