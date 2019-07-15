package server.game.decks;

import server.game.Card;
import server.game.DamageCards.Virus;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckVirus.
 *
 * @author Vincent Tafferner
 */
public class DeckVirus {

    ArrayList<Card> deckVirus;

    /**
     * This method initializes the deck of Virus cards.
     */
    public void initializeDeckVirus() {
        this.deckVirus = new ArrayList<>();

        for (int i = 0; i < VIRUS_CARDS_AMOUNT; i++) {
            deckVirus.add(new Virus());
        }
    }

    public ArrayList<Card> getDeckVirus() {
        return deckVirus;
    }
}
