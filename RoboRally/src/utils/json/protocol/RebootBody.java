package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'Reboot' protocol JSON message.
 * @author Manuel Neumayer
 */
public class RebootBody implements ServerMessageAction<RebootBody> {
    @Expose
    private Integer playerID;

    public RebootBody(Integer playerID) {
        this.playerID = playerID;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, RebootBody bodyObject) {
        MessageDistributer.handleReboot(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
