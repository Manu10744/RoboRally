package utils.json.protocol;

import com.google.gson.annotations.Expose;
import server.Server;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'SetStatus' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SetStatusBody implements ClientMessageAction<SetStatusBody> {
    @Expose
    private Boolean ready;

    public SetStatusBody(Boolean ready) {
        this.ready = ready;
    }

    @Override
    public void triggerAction(Server server, Server.ServerReaderTask task, SetStatusBody messageBodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleSetStatus(server, task, messageBodyObject);
    }

    public Boolean isReady() {
        return ready;
    }
}
