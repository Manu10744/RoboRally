package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'Energy' protocol JSON message.
 * @author Manuel Neumayer
 */
public class EnergyBody implements ServerMessageAction<EnergyBody> {
    @Expose
    private Integer playerID;
    @Expose
    private Integer count;
    @Expose
    private String source;

    public EnergyBody(Integer playerID, Integer count, String source) {
        this.playerID = playerID;
        this.count = count;
        this.source = source;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, EnergyBody bodyObject) {
        MessageDistributer.handleEnergy(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Integer getCount() {
        return count;
    }

    public String getSource() {
        return source;
    }
}
