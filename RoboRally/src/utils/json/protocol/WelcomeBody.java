package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'Welcome' protocol JSON message.
 * @author Manuel Neumayer
 */
public class WelcomeBody implements ServerMessageAction<WelcomeBody> {
    @Expose
    private Integer playerID;

    public WelcomeBody(Integer playerID) {
        this.playerID = playerID;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, WelcomeBody bodyObject) {
        MessageDistributer.handleWelcome(client, task, bodyObject);
    }
}
