package utils.json.protocol;

import com.google.gson.annotations.Expose;
import modelserver.game.Card;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'YourCards' protocol JSON message.
 * @author Manuel Neumayer
 */
public class YourCardsBody {
    @Expose
    private ArrayList<Card> cardsInHand;
    @Expose
    private int cardsInPile;

    public YourCardsBody(ArrayList<Card> cardsInHand, int cardsInPile) {
        this.cardsInHand = cardsInHand;
        this.cardsInPile = cardsInPile;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public int getCardsInPile() {
        return cardsInPile;
    }
}
