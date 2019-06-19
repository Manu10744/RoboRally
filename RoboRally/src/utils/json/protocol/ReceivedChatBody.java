package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'ReceivedChat' protocol JSON message.
 * @author Manuel Neumayer
 */
public class ReceivedChatBody {
    @Expose
    private String message;
    @Expose
    private Integer from;
    @Expose
    private Boolean isPrivate;

    public ReceivedChatBody(String message, Integer from, Boolean isPrivate) {
        this.message = message;
        this.from = from;
        this.isPrivate = isPrivate;
    }

    public String getMessage() {
        return message;
    }

    public Integer getFrom() {
        return from;
    }

    public Boolean isPrivate() {
        return isPrivate;
    }
}
