package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'SelectionFinished' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SelectionFinishedBody implements ServerMessageAction<SelectionFinishedBody> {
    @Expose
    private Integer playerID;

    public SelectionFinishedBody(Integer playerID) {
        this.playerID = playerID;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, SelectionFinishedBody bodyObject) {
        MessageDistributer.handleSelectionFinished(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
