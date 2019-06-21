package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'CurrentPlayer' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CurrentPlayerBody implements ServerMessageAction<CurrentPlayerBody> {
    @Expose
    private Integer playerID;

    public CurrentPlayerBody(Integer playerID) {
        this.playerID = playerID;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, CurrentPlayerBody bodyObject) {
        MessageDistributer.handleCurrentPlayer(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
