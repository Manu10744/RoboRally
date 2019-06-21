package utils.json.protocol;

import com.google.gson.annotations.Expose;
import server.Server;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'SendChat' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SendChatBody implements ClientMessageAction<SendChatBody> {
    @Expose
    private String message;
    @Expose
    private Integer to;

    public SendChatBody(String message, Integer to) {
        this.message = message;
        this.to = to;
    }

    @Override
    public void triggerAction(Server server, Server.ServerReaderTask task, SendChatBody bodyObject) {
        MessageDistributer.handleSendChat(server, task, bodyObject);
    }

    public String getMessage() {
        return message;
    }

    public Integer getTo() {
        return to;
    }
}
