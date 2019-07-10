package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'PlayerStatus' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerStatusBody implements ServerMessageAction<PlayerStatusBody> {
    @Expose
    private Integer playerID;
    @Expose
    private Boolean ready;

    public PlayerStatusBody(Integer playerID, Boolean ready) {
        this.playerID = playerID;
        this.ready = ready;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, PlayerStatusBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handlePlayerStatus(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Boolean isReady() {
        return ready;
    }
}
