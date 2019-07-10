package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import server.game.Card;
import utils.json.MessageDistributer;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'DrawDamage' protocol JSON message.
 * @author Manuel Neumayer
 */
public class DrawDamageBody implements ServerMessageAction<DrawDamageBody> {
    @Expose
    private Integer playerID;
    @Expose
    private ArrayList<Card> cards;

    public DrawDamageBody(Integer playerID, ArrayList<Card> cards) {
        this.playerID = playerID;
        this.cards = cards;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, DrawDamageBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleDrawDamage(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
