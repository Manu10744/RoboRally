package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'GameFinished' protocol JSON message.
 * @author Manuel Neumayer
 */
public class GameFinishedBody implements ServerMessageAction<GameFinishedBody> {
    @Expose
    private Integer playerID;

    public GameFinishedBody(Integer playerID) {
        this.playerID = playerID;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, GameFinishedBody bodyObject) {
        MessageDistributer.handleGameFinished(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
