package utils.instructions;

/**
 * This class contains all chat-related instructions that the client needs to have access to.
 * These instructions will be sent to the <b>Server</b> when necessary.<br>
 *
 * {@link ClientChatInstructionType} The instructions represented as enum value.<br>
 */
public class ClientChatInstruction {

    public enum ClientChatInstructionType {
        CHECK_NAME,
        SEND_MESSAGE,
        SEND_PRIVATE_MESSAGE,
        BYE,
    }
}
