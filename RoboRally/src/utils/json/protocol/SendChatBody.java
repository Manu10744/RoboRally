package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'SendChat' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SendChatBody {
    @Expose
    private String message;
    @Expose
    private int to;

    public SendChatBody(String message, int to) {
        this.message = message;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public int getTo() {
        return to;
    }
}
