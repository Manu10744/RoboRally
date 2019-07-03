package server.game.decks;

import server.game.Card;
import server.game.DamageCards.*;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckSpam.
 *
 * @author Vincent Tafferner
 */
public class DeckSpam {

    /**
     * This method initializes the deck of Spam cards.
     */
    public static void initializeDeckSpam(){

        ArrayList<Card> deckSpam = new ArrayList<>();

        for (int i = 0; i < SPAM_CARDS_AMOUNT; i++) {
            deckSpam.add(new Spam());
        }

    }
}
