package utils.json.protocol;

import com.google.gson.annotations.Expose;
import modelserver.game.Card;

/** This is the wrapper class for the message body of the 'PlayCard' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayCardBody {
    @Expose
    private Card card;

    public PlayCardBody(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
