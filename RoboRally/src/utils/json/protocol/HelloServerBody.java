package utils.json.protocol;

import com.google.gson.annotations.Expose;
import server.Server;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'HelloServer' protocol JSON message.
 * @author Manuel Neumayer
 */
public class HelloServerBody implements ClientMessageAction<HelloServerBody> {
    @Expose
    private String group;
    @Expose
    private Boolean isAI;
    @Expose
    private String protocol;

    public HelloServerBody(String group, Boolean isAI, String protocol) {
        this.group = group;
        this.isAI = isAI;
        this.protocol = protocol;
    }

    @Override
    public void triggerAction(Server server, Server.ServerReaderTask task, HelloServerBody messageBodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleHelloServer(server, task, messageBodyObject);
    }

    public String getGroup() {
        return group;
    }

    public Boolean isAI() {
        return isAI;
    }

    public String getProtocol() {
        return protocol;
    }
}
