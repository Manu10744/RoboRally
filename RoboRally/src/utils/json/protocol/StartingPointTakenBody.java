package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'StartingPointTaken' protocol JSON message.
 * @author Manuel Neumayer
 */
public class StartingPointTakenBody implements ServerMessageAction<StartingPointTakenBody> {
    @Expose
    private Integer x;
    @Expose
    private Integer y;
    @Expose
    private Integer playerID;

    public StartingPointTakenBody(Integer x, Integer y, Integer playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, StartingPointTakenBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleStartingPointTaken(client, task, bodyObject);
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
