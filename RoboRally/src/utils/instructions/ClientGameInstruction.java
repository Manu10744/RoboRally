package utils.instructions;

public class ClientGameInstruction extends Instruction {

    private ClientToServerInstructionType type;
    private String content;


    @Override
    public ClientToServerInstructionType getClientToServerInstructionType() {
        return this.type;
    }
}
