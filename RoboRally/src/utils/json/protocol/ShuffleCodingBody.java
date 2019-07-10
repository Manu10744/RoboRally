package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'ShuffleCoding' protocol JSON message.
 * @author Manuel Neumayer
 */
public class ShuffleCodingBody implements ServerMessageAction<ShuffleCodingBody> {
    @Expose
    private Integer playerID;

    public ShuffleCodingBody(Integer playerID) {
        this.playerID = playerID;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, ShuffleCodingBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleShuffleCoding(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
