package server.game.decks;

import server.game.Card;
import server.game.DamageCards.*;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckWorm.
 *
 * @author Vincent Tafferner
 */
public class DeckWorm {

    /**
     * This method initializes the deck of Worm cards.
     */
    public static void initializeDeckWorm(){

        ArrayList<Card> deckWorm = new ArrayList<>();

        for (int i = 0; i < WORM_CARDS_AMOUNT; i++) {
            deckWorm.add(new Worm());
        }

    }
}
