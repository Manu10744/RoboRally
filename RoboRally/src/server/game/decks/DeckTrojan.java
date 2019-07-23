package server.game.decks;

import server.game.Card;
import server.game.DamageCards.*;
import server.game.Deck;

import java.util.ArrayList;
import java.util.Collections;

import static utils.Parameter.*;

/**
 * This class implements the deckTrojan.
 *
 * @author Vincent Tafferner
 */
public class DeckTrojan extends Deck {

    private ArrayList<Card> deckTrojan;

    /**
     * This method initializes the deck of Trojan cards.
     */
    @Override
    public void initializeDeck(){
        this.deckTrojan = new ArrayList<>();

        for (int i = 0; i < TROJAN_CARDS_AMOUNT; i++) {
            deckTrojan.add(new Trojan());
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
        return deckTrojan;
    }
}
