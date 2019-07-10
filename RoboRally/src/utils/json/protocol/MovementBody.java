package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'Movement' protocol JSON message.
 * @author Manuel Neumayer
 */
public class MovementBody implements ServerMessageAction<MovementBody> {
    @Expose
    private Integer playerID;
    @Expose
    private Integer x;
    @Expose
    private Integer y;

    public MovementBody(Integer playerID, Integer x, Integer y) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, MovementBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleMovement(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
