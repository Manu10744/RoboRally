package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'NotYourCards' protocol JSON message.
 * @author Manuel Neumayer
 */
public class NotYourCardsBody {
    @Expose
    private int playerID;
    @Expose
    private int cardsInHand;
    @Expose
    private int cardsInPile;

    public NotYourCardsBody(int playerID, int cardsInHand, int cardsInPile) {
        this.playerID = playerID;
        this.cardsInHand = cardsInHand;
        this.cardsInPile = cardsInPile;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getCardsInHand() {
        return cardsInHand;
    }

    public int getCardsInPile() {
        return cardsInPile;
    }
}
