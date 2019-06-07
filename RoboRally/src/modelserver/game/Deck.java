package modelserver.game;

import java.util.ArrayList;

/**
 * This class is responsible for handling the Cards in the game.
 *
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 */
abstract class Deck {

    private int deckSpam;
    private int deckVirus;
    private int deckWorm;
    private int trojanHorse;
    private ArrayList<Card> deckDiscard;
    private ArrayList<Card> deckDraw;


    /**
     * This method initializes the deck of the programming cards
     */
    public void initializeDeck() {

    }

    /**
     * This method shuffles the deck
     */
    public void shuffleDeck() {

    }

    /**
     * This method is used for drawing cards from the deck
     */
    public void drawCard() {

    }

    /**
     * This method is used for discarding cards from the deck
     */
    public void discardCard() {

    }

    /**
     * This method is used for discarding damage cards from the deck
     */
    public void discardDamageCard() {

    }

}
