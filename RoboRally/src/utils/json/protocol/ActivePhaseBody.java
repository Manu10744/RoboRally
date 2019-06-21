package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'ActivePhase' protocol JSON message.
 * @author Manuel Neumayer
 */
public class ActivePhaseBody implements ServerMessageAction<ActivePhaseBody> {
    @Expose
    private Integer phase;

    public ActivePhaseBody(Integer phase) {
        this.phase = phase;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, ActivePhaseBody bodyObject) {
        MessageDistributer.handleActivePhase(client, task, bodyObject);
    }

    public Integer getPhase() {
        return phase;
    }
}
