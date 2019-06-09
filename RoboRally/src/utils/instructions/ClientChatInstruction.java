package utils.instructions;

/**
 * This class contains all chat-related instructions that the client needs to have access to.
 * These instructions will be sent to the <b>Server</b> when necessary.<br>
 *
 */
public class ClientChatInstruction extends Instruction {

    private ClientToServerInstructionType type;
    private String content;

    // Constructor for a ClientChatInstruction
    public ClientChatInstruction(ClientToServerInstructionType type, String content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public ClientToServerInstructionType getClientToServerInstructionType() {
        return this.type;
    }
}
