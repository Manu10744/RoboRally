package server.game.decks;

import server.game.Card;
import server.game.DamageCards.*;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckVirus.
 *
 * @author Vincent Tafferner
 */
public class DeckVirus {

    /**
     * This method initializes the deck of Virus cards.
     */
    public static void initializeDeckVirus(){

        ArrayList<Card> deckVirus = new ArrayList<>();

        for (int i = 0; i < VIRUS_CARDS_AMOUNT; i++) {
            deckVirus.add(new Virus());
        }

    }
}
