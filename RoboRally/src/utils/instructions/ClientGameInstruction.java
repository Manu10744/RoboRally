package utils.instructions;

public class ClientGameInstruction extends Instruction {

    private ClientToServerInstructionType type;
    private String content;

    // Constructor for a ClientGameInstruction
    public ClientGameInstruction(ClientToServerInstructionType type, String content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public ClientToServerInstructionType getClientToServerInstructionType() {
        return this.type;
    }
}
