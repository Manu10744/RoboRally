package server.game.decks;

import server.game.Card;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckDraw.
 *
 * @author Vincent Tafferner
 */
public class DeckDraw {

    /**
     * This method initializes the deck of the programming cards.
     */
    public void initializeDeckDraw() {

        ArrayList<Card> deckDraw = new ArrayList<>();

        // Add MoveI cards to the deck. The default value is 5.
        for (int i = 0; i < MOVEI_CARDS_AMOUNT; i++) {
            deckDraw.add(new MoveI());
        }

        // Add MoveII cards to the deck. The default value is 3.
        for (int i = 0; i < MOVEII_CARDS_AMOUNT; i++) {
            deckDraw.add(new MoveII());
        }

        // Add TurnRight cards to the deck. The default value is 3.
        for (int i = 0; i < TURNRIGHT_CARDS_AMOUNT; i++) {
            deckDraw.add(new TurnRight());
        }

        // Add TurnLeft cards to the deck. The default value is 3.
        for (int i = 0; i < TURNLEFT_CARDS_AMOUNT; i++) {
            deckDraw.add(new TurnLeft());
        }

        // Add Again cards to the deck. The default value is 2.
        for (int i = 0; i < AGAIN_CARDS_AMOUNT; i++) {
            deckDraw.add(new Again());
        }

        // Add UTurn cards to the deck. The default value is 1.
        for (int i = 0; i < UTURN_CARDS_AMOUNT; i++) {
            deckDraw.add(new UTurn());
        }

        // Add BackUp cards to the deck. The default value is 1.
        for (int i = 0; i < BACKUP_CARDS_AMOUNT; i++) {
            deckDraw.add(new BackUp());
        }

        // Add PowerUp cards to the deck. The default value is 1.
        for (int i = 0; i < POWERUP_CARDS_AMOUNT; i++) {
            deckDraw.add(new PowerUp());
        }

        // Add MoveIII cards to the deck. The default value is 1.
        for (int i = 0; i < MOVEIII_CARDS_AMOUNT; i++) {
            deckDraw.add(new MoveIII());
        }
    }

}
