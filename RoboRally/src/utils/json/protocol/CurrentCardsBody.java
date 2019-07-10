package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import server.game.Card;
import utils.json.MessageDistributer;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'CurrentCards' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CurrentCardsBody implements ServerMessageAction<CurrentCardsBody>{
    @Expose
    private ArrayList<ActiveCardsObject> activeCards = new ArrayList<ActiveCardsObject>();

    public CurrentCardsBody(ArrayList<ActiveCardsObject> activeCards) {
        this.activeCards = activeCards;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, CurrentCardsBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleCurrentCards(client, task, bodyObject);
    }

    public ArrayList<ActiveCardsObject> getActiveCards() {
        return activeCards;
    }

    public static class ActiveCardsObject {
        @Expose
        private Integer playerID;
        @Expose
        private Card card;

        public ActiveCardsObject(Integer playerID, Card card) {
            this.playerID = playerID;
            this.card = card;
        }

        public Integer getPlayerID() { return playerID; }

        public Card getCard() { return card; }
    }
}
