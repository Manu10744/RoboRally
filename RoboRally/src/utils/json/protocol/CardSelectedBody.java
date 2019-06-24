package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'CardSelected' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CardSelectedBody implements ServerMessageAction<CardSelectedBody> {
    @Expose
    private Integer playerID;
    @Expose
    private Integer register;

    public CardSelectedBody(int playerID, Integer register) {
        this.playerID = playerID;
        this.register = register;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, CardSelectedBody bodyObject) {
        MessageDistributer.handleCardSelected(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Integer getRegister() {
        return register;
    }
}
