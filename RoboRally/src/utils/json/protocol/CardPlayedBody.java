package utils.json.protocol;

import com.google.gson.annotations.Expose;
import modelserver.game.Card;

/** This is the wrapper class for the message body of the 'CardPlayed' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CardPlayedBody {
    @Expose
    private int playerID;
    @Expose
    private Card card;

    public CardPlayedBody(int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Card getCard() {
        return card;
    }
}
