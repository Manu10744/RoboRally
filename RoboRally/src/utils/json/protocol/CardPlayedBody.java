package utils.json.protocol;

import com.google.gson.annotations.Expose;
import server.game.Card;

/** This is the wrapper class for the message body of the 'CardPlayed' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CardPlayedBody {
    @Expose
    private Integer playerID;
    @Expose
    private Card card;

    public CardPlayedBody(Integer playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Card getCard() {
        return card;
    }
}
