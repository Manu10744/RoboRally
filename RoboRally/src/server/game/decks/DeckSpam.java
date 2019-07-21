package server.game.decks;

import server.game.Card;
import server.game.DamageCards.*;
import server.game.Deck;

import java.util.ArrayList;
import java.util.Collections;

import static utils.Parameter.*;

/**
 * This class implements the deckSpam.
 *
 * @author Vincent Tafferner
 */
public class DeckSpam extends Deck {

    private ArrayList<Card> deckSpam;

    /**
     * This method initializes the deck of Spam cards.
     */
    @Override
    public void initializeDeck() {
        this.deckSpam = new ArrayList<>();

        for (int i = 0; i < SPAM_CARDS_AMOUNT; i++) {
            deckSpam.add(new Spam());
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
        return deckSpam;
    }
}
