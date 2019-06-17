package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'ReceivedChat' protocol JSON message.
 * @author Manuel Neumayer
 */
public class ReceivedChatBody {
    @Expose
    private String message;
    @Expose
    private int from;
    @Expose
    private boolean isPrivate;

    public ReceivedChatBody(String message, int from, boolean isPrivate) {
        this.message = message;
        this.from = from;
        this.isPrivate = isPrivate;
    }

    public String getMessage() {
        return message;
    }

    public int getFrom() {
        return from;
    }

    public boolean isPrivate() {
        return isPrivate;
    }
}
