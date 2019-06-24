package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import server.game.Card;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'CardPlayed' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CardPlayedBody implements ServerMessageAction<CardPlayedBody> {
    @Expose
    private Integer playerID;
    @Expose
    private Card card;

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, CardPlayedBody bodyObject) {
        MessageDistributer.handleCardPlayed(client, task, bodyObject);
    }

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
