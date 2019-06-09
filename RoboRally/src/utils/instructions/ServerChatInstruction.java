package utils.instructions;

/**
 * This class contains all chat-related instructions that the server needs to have access to.
 * These instructions will be sent to the <b>Client</b> when necessary.<br>
 *
 */
public class ServerChatInstruction extends Instruction {

    private ServerToClientInstructionType type;
    private String content;

    // Constructor for a ServerChatInstruction
    public ServerChatInstruction(ServerToClientInstructionType type, String content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public ServerToClientInstructionType getServerToClientInstructionType() {
        return this.type;
    }
}
