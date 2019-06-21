package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;


/** This is the wrapper class for the message body of the 'HelloClient' protocol JSON message.
 * @author Manuel Neumayer
 */
public class HelloClientBody implements ServerMessageAction<HelloClientBody> {
    @Expose
    String protocol;

    public HelloClientBody(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, HelloClientBody message) {
        MessageDistributer.handleHelloClient(client, message);
    }

    public String getProtocol() {
        return protocol;
    }
}
