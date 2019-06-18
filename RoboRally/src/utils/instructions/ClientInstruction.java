package utils.instructions;

public class ClientInstruction extends Instruction {
    ClientInstructionType clientInstructionType;

    public ClientInstruction(ClientInstructionType clientInstructionType){
        this.clientInstructionType = clientInstructionType;
    }

    public ClientInstructionType getClientInstructionType(){
        return clientInstructionType;
    }

    public enum ClientInstructionType{
        //ClientChatInstructions
        HELLO_SERVER, //Client sends group name, protocol-vs and KI-on/off to Server
        SEND_CHAT, //Client sends public message to all, the value of "to" of the JSON-message must be -1
        SEND_PRIVATE_CHAT, //Clients sends private message to another player via the server

        //ClientGameInstruction
        PLAYER_VALUES, //Client sends player-name and player figure is sent to server
        SET_STATUS, // Client signals to server if player is ready
        PLAY_CARD, // Player plays a card
        SET_STARTING_POINT, // Player chooses starting point and informs server of her or his choice
        SELECT_CARD, /* Player selects cards, each selected card is sent to the srever after five have been chosen.
                        If a register is emptied the card value is 'null'*/
        CARD_SELECTED, //Client informs client that a card has been put in the register
        SELECTION_FINISHED, // Client informs server that a player has filled his or her full register
    }
}
