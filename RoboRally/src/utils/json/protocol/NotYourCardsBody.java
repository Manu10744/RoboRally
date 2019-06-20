package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'NotYourCards' protocol JSON message.
 * @author Manuel Neumayer
 */
public class NotYourCardsBody {
    @Expose
    private Integer playerID;
    @Expose
    private Integer cardsInHand;
    @Expose
    private Integer cardsInPile;

    public NotYourCardsBody(Integer playerID, Integer cardsInHand, Integer cardsInPile) {
        this.playerID = playerID;
        this.cardsInHand = cardsInHand;
        this.cardsInPile = cardsInPile;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Integer getCardsInHand() {
        return cardsInHand;
    }

    public Integer getCardsInPile() {
        return cardsInPile;
    }
}
