package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'Error' protocol JSON message.
 * @author Manuel Neumayer
 */
public class ErrorBody implements ServerMessageAction<ErrorBody> {
    @Expose
    private String error;

    public ErrorBody(String error) {
        this.error = error;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, ErrorBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleError(client, task, bodyObject);
    }

    public String getError() {
        return error;
    }
}
