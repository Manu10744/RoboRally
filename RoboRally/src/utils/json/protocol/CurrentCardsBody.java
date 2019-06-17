package utils.json.protocol;

import com.google.gson.annotations.Expose;
import modelserver.game.Card;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'CurrentCards' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CurrentCardsBody {
    @Expose
    private ArrayList<ActiveCardsObject> activeCards = new ArrayList<ActiveCardsObject>();

    public CurrentCardsBody(ArrayList<ActiveCardsObject> activeCards) {
        this.activeCards = activeCards;
    }

    public ArrayList<ActiveCardsObject> getActiveCards() {
        return activeCards;
    }

    public static class ActiveCardsObject {
        @Expose
        private int playerID;
        @Expose
        private Card card;

        public ActiveCardsObject(int playerID, Card card) {
            this.playerID = playerID;
            this.card = card;
        }

        public int getPlayerID() { return playerID; }

        public Card getCard() { return card; }
    }
}
