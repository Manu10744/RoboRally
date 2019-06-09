package utils.instructions;

/**
 * This class contains all game-related instructions that the server needs to have access to.
 * These instructions will be sent to the <b>Client</b> when necessary.<br>
 *
 */
public class ServerGameInstruction extends Instruction {

    private ServerToClientInstructionType type;
    private String content;

    // Constructor for a ServerGameInstruction
    public ServerGameInstruction(ServerToClientInstructionType type, String content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public ServerToClientInstructionType getServerToClientInstructionType() {
        return this.type;
    }
}
