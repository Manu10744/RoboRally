package server.game.decks;

import server.game.Card;
import server.game.ProgrammingCards.*;

import java.util.ArrayList;

import static utils.Parameter.*;

/**
 * This class implements the deckRegister.
 *
 * @author Vincent Tafferner
 */
public class DeckRegister {

    private ArrayList<Card> deckRegister;

    /**
     * This method creates an empty deckRegister.
     */
    public void initializeDeckRegister(){
        this.deckRegister = new ArrayList<>();
    }

    public ArrayList<Card> getDeckRegister() {
        return deckRegister;
    }
}
