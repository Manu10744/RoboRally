package utils.instructions;

import modelclient.Client;
import utils.json.MessageBody;

public class ClientInstruction extends Instruction {
    ClientInstructionType clientInstruction;
    MessageBody content;

    public ClientInstruction (ClientInstructionType clientInstruction){
        this.clientInstruction = clientInstruction;
    }

    public enum ClientInstructionType {
        //ClientChatInstructions
        HELLO_SERVER, //Client sends group name, protocol-vs and KI-on/off to Server
        SEND_CHAT, //Client sends public message to all, the value of "to" of the JSON-message must be -1
        SEND_PRIVATE_CHAT, //Clients sends private message to another player via the server
        CLIENT_LEAVES, // Client informs Server if clients leaves the game

        //ClientGameInstruction
        PLAYER_VALUES, //Client sends player-name and player figure is sent to server
        SET_STATUS, // Client signals to server if player is ready
        PLAY_CARD, // Player plays a card
        SET_STARTING_POINT, // Player chooses starting point and informs server of her or his choice
        SELECT_CARDS, /* Player selects cards, each selected card is sent to the srever after five have been chosen.
                        If a register is emptied the card value is 'null'*/
        CARDS_SELECTED, //Client informs client that a card has been put in the register
        SELECTION_FINISHED, // Client informs server that a player has filled his or her full register
        PLAYER_SHOOTING, //For animation purposes (?)
    }
}
