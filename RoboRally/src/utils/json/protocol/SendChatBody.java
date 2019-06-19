package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'SendChat' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SendChatBody {
    @Expose
    private String message;
    @Expose
    private Integer to;

    public SendChatBody(String message, Integer to) {
        this.message = message;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public Integer getTo() {
        return to;
    }
}
