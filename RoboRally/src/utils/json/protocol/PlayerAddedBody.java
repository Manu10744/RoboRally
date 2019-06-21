package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'PlayerAdded' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerAddedBody implements ServerMessageAction<PlayerAddedBody> {
    @Expose
    private Integer playerID;
    @Expose
    private String name;
    @Expose
    private Integer figure;

    public PlayerAddedBody(Integer playerID, String name, Integer figure) {
        this.playerID = playerID;
        this.name = name;
        this.figure = figure;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, PlayerAddedBody bodyObject) {
        MessageDistributer.handlePlayerAdded(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public String getName() {
        return name;
    }

    public Integer getFigure() {
        return figure;
    }
}
