package server.game.decks;

import server.game.Card;
import server.game.DamageCards.Virus;
import server.game.Deck;

import java.util.ArrayList;
import java.util.Collections;

import static utils.Parameter.*;

/**
 * This class implements the deckVirus.
 *
 * @author Vincent Tafferner
 */
public class DeckVirus extends Deck {

    private ArrayList<Card> deckVirus;

    /**
     * This method initializes the deck of Virus cards.
     */
    @Override
    public void initializeDeck() {
        this.deckVirus = new ArrayList<>();

        for (int i = 0; i < VIRUS_CARDS_AMOUNT; i++) {
            deckVirus.add(new Virus());
        }
    }

    /**
     * This method shuffles the deck.
     */
    @Override
    public void shuffleDeck() {
        Collections.shuffle(this.getDeck());
    }

    /**
     * This method returns the deck.
     */
    @Override
    public ArrayList<Card> getDeck() {
        return deckVirus;
    }
}
