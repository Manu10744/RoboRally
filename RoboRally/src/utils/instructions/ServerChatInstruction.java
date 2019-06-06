package utils.instructions;

/**
 * This class contains all chat-related instructions that the server needs to have access to.
 * These instructions will be sent to the <b>Client</b> when necessary.<br>
 *
 * {@link ServerChatInstructionType} The instructions represented as enum value.<br>
 */
public class ServerChatInstruction {

    public enum ServerChatInstructionType {
        NAME_INVALID,
        NAME_SUCCESS,
        CLIENT_JOINED,
        CLIENT_REGISTER,
        CLIENT_WELCOME,
        NEW_MESSAGE,
        CLIENT_LEAVES,
        KILL_CLIENT
    }
}
