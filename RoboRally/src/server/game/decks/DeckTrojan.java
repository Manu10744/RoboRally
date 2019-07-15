package server.game.decks;

import server.game.Card;
import server.game.DamageCards.*;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckTrojan.
 *
 * @author Vincent Tafferner
 */
public class DeckTrojan {

    ArrayList<Card> deckTrojan;

    /**
     * This method initializes the deck of Trojan cards.
     */
    public void initializeDeckTrojan(){
        this.deckTrojan = new ArrayList<>();

        for (int i = 0; i < TROJAN_CARDS_AMOUNT; i++) {
            deckTrojan.add(new Trojan());
        }
    }

    public ArrayList<Card> getDeckTrojan() {
        return deckTrojan;
    }
}
