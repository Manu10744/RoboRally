package utils.json.protocol;

import com.google.gson.annotations.Expose;
import server.game.Card;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'CardsYouGotNow' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CardsYouGotNowBody {
    @Expose
    private ArrayList<Card> cards;

    public CardsYouGotNowBody(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
