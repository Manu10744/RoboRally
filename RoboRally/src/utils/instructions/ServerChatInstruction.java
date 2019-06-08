package utils.instructions;

/**
 * This class contains all chat-related instructions that the server needs to have access to.
 * These instructions will be sent to the <b>Client</b> when necessary.<br>
 *
 * {@link ServerChatInstructionType} The instructions represented as enum value.<br>
 */
public class ServerChatInstruction extends Instruction {

    private  ServerChatInstruction serverChatInstruction;

    public enum ServerChatInstructionType {
        // ServerChatInstructions
        HELLO_CLIENT, //Server sends Protocol-vs to Client
        WELCOME, // Client gets a player ID from the server
        RECEIVED_CHAT, //Server distributes message to all
        RECEIVED_PRIVATE_CHAT, // Server distributes private message to the appropriate player

        ERROR, // Server informs client that a transmission error occurred
        NAME_INVALID, // Will be part of meesage type "error", message body contains name_invalid -> new method call
    }

    public ServerChatInstruction getServerChatInstruction(){
        return this.serverChatInstruction;
    }

}
