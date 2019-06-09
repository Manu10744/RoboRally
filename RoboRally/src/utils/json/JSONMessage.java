package utils.json;

/**
 * This class is the wrapper class for all JSON messages of the protocol. It both
 * contains the type and body of the message.
 * Depending on the type of message, the needed variables will be initialized by
 * the appropriate constructor.
 *
 * @author Manuel Neumayer
 */
public class JSONMessage {
    private String messageType;
    private MessageBody messageBody;


    // Constructor for a JSON message
    public JSONMessage(String messageType, MessageBody messageBody) {
    this.messageType = messageType;
    this.messageBody = messageBody;
}


    public String getMessageType() {
        return messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }
}
