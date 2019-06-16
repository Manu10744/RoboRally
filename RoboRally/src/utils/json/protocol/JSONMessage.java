package utils.json.protocol;

import com.google.gson.annotations.Expose;

import java.util.Objects;

/**
 * This class is the wrapper class for all JSON messages of the protocol. It both
 * contains the type and body of the message.
 * Depending on the type of message, the needed variables will be initialized by
 * the appropriate constructor for the {@link messageBody}.
 *
 * @author Manuel Neumayer
 */
public class JSONMessage {
    @Expose
    private String messageType;
    @Expose
    private Object messageBody;

    public JSONMessage(String messageType, Object messageBody) {
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    public String getMessageType() {
        return messageType;
    }

    public Object getMessageBody() {
        return messageBody;
    }

}