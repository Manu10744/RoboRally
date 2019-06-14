package modelserver.game;

/**
 * This class defines what a card shall be in the game. <br>
 * There are more specific cards defined in the classes that inherit from Card.
 *
 * @author Vincent Tafferner
 */
public abstract class Card {

    public String cardName;
    public int cardAmount;

    /**
     * This method simply returns the name of the card.
     * @return cardName
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * This method simply returns how many cards there are of a certain type.
     * @return cardAmount
     */
    public int getCardAmount() {
        return cardAmount;
    }
}
