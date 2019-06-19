package utils.json.protocol;

import com.google.gson.annotations.Expose;
import modelserver.game.Card;

/** This is the wrapper class for the message body of the 'SelectCard' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SelectCardBody {
    @Expose
    private Card card;
    @Expose
    private Integer register;

    public SelectCardBody(Card card, Integer register) {
        this.card = card;
        this.register = register;
    }

    public Card getCard() {
        return card;
    }

    public Integer getRegister() {
        return register;
    }
}
