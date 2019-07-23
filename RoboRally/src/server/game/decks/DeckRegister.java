package server.game.decks;

import server.game.Card;
import server.game.Deck;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;
import java.util.Collections;

import static utils.Parameter.*;

/**
 * This class implements the deckRegister.
 *
 * @author Vincent Tafferner
 */
public class DeckRegister extends Deck {

    private ArrayList<Card> deckRegister;

    /**
     * This method creates an empty deckRegister.
     */
    @Override
    public void initializeDeck(){
        this.deckRegister = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            deckRegister.add(i, null);
        }
    }

    /**
     * This method shuffles the deck.
     */
    public void shuffleDeck() {
        Collections.shuffle(this.getDeck());
    }

    /**
     * This method returns the deck.
     */
    @Override
    public ArrayList<Card> getDeck() {
        return deckRegister;
    }

}
