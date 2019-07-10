package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'PlayerTurning' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerTurningBody implements ServerMessageAction<PlayerTurningBody> {
    @Expose
    private Integer playerID;
    @Expose
    private String direction;

    public PlayerTurningBody(Integer playerID, String direction) {
        this.playerID = playerID;
        this.direction = direction;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, PlayerTurningBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handlePlayerTurning(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public String getDirection() {
        return direction;
    }
}
