package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'CheckPointReached' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CheckPointReachedBody implements ServerMessageAction<CheckPointReachedBody> {
    @Expose
    private Integer playerID;
    @Expose
    private Integer number;

    public CheckPointReachedBody(Integer playerID, Integer number) {
        this.playerID = playerID;
        this.number = number;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, CheckPointReachedBody bodyObject) {
        MessageDistributer.handleCheckPointReached(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Integer getNumber() {
        return number;
    }
}
