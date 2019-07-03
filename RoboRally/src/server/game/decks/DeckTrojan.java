package server.game.decks;

import server.game.Card;
import server.game.DamageCards.*;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckTrojan.
 *
 * @author Vincent Tafferner
 */
public class DeckTrojan {

    /**
     * This method initializes the deck of Trojan cards.
     */
    public static void initializeDeckTrojan(){

        ArrayList<Card> deckTrojan = new ArrayList<>();

        for (int i = 0; i < TROJAN_CARDS_AMOUNT; i++) {
            deckTrojan.add(new Trojan());
        }

    }
}
