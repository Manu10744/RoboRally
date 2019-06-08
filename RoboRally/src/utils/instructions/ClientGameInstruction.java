package utils.instructions;

public class ClientGameInstruction extends Instruction {

    private ClientGameInstructionType type;
    private String content;

    public enum ClientGameInstructionType {
        //ClientGameInstruction
        PLAYER_VALUES, //Client sends player-name and player figure is sent to server
        SET_STATUS, // Client signals to server if player is ready
        PLAY_CARD, // Player plays a card
        SET_STARTING_POINT, // Player chooses starting point and informs server of her or his choice
        SELECT_CARDS, /* Player selects cards, each selected card is sent to the srever after five have been chosen.
                        If a register is emptied the card value is 'null'*/
        CARDS_SELECTED, //Client informs client that a card has been put in the register
        SELECTION_FINISHED, // Client informs server that a player has filled his or her full register

    }

    // Constructor for a ClientGameInstruction
    public ClientGameInstruction(ClientGameInstructionType type, String content) {
        this.type = type;
        this.content = content;
    }

    public ClientGameInstructionType getClientGameInstructionType() {
        return this.type;
    }
}
