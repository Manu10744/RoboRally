package server.game.decks;

import server.game.Card;
import server.game.DamageCards.Worm;
import server.game.Deck;

import java.util.ArrayList;
import java.util.Collections;

import static utils.Parameter.WORM_CARDS_AMOUNT;

/**
 * This class implements the deckWorm.
 *
 * @author Vincent Tafferner
 */
public class DeckWorm extends Deck {

    private ArrayList<Card> deckWorm;

    /**
     * This method initializes the deck of Worm cards.
     */
    @Override
    public void initializeDeck() {
        this.deckWorm = new ArrayList<>();

        for (int i = 0; i < WORM_CARDS_AMOUNT; i++) {
            deckWorm.add(new Worm());
        }
    }

    /**
     * This method shuffles the deck.
     */
    @Override
    public void shuffleDeck(ArrayList<Card> deckWorm) {
        Collections.shuffle(deckWorm);
    }

    /**
     * This method returns the deck.
     */
    @Override
    public ArrayList<Card> getDeck() {
        return deckWorm;
    }
}
