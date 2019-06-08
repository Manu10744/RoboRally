package utils.instructions;

/**
 * This class contains all chat-related instructions that the client needs to have access to.
 * These instructions will be sent to the <b>Server</b> when necessary.<br>
 *
 * {@link ClientChatInstructionType} The instructions represented as enum value.<br>
 */
public class ClientChatInstruction extends Instruction {

    private ClientChatInstructionType type;
    private String content;

    public enum ClientChatInstructionType {
        //ClientChatInstructions
        HELLO_SERVER, //Client sends group name, protocol-vs and KI-on/off to Server
        SEND_CHAT, //Client sends public message to all, the value of "to" of the JSON-message must be -1
        SEND_PRIVATE_CHAT, //Clients sends private message to another player via the server
        CLIENT_LEAVES, // Client informs Server if clients leaves the game
        BYE, // Client leaves game and informs server thereof
    }

    // Constructor for a ClientChatInstruction
    public ClientChatInstruction(ClientChatInstructionType type, String content) {
        this.type = type;
        this.content = content;
    }

    public ClientChatInstructionType getClientChatInstructionType() {
        return this.type;
    }
}
