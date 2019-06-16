package utils.json.protocol;

import com.google.gson.annotations.Expose;
import modelserver.game.Card;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'DrawDamage' protocol JSON message.
 * @author Manuel Neumayer
 */
public class DrawDamageBody {
    @Expose
    private int playerID;
    @Expose
    private ArrayList<Card> cards;

    public DrawDamageBody(int playerID, ArrayList<Card> cards) {
        this.playerID = playerID;
        this.cards = cards;
    }

    public int getPlayerID() {
        return playerID;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
