package utils.instructions;

import java.io.Serializable;

/**
 * The Instruction class contains all chat and game enums (and related methods) which are used by the Server and
 * Client classes to establish client-server-connection, client-registration, chat- and game-communication.
 *
 * @author Ivan Dovecar
 */
public class Instruction implements Serializable {

    public Instruction() {

    }

    // Übersicht (für Ivan)
    public enum InstructionType{

    }

    private ClientToServerInstructionType clientToServerInstructionType;
    private ServerToClientInstructionType serverToClientInstructionType;
    private String content;
    private String addressedClient;

    //Constructor without arguments is needed
    public Instruction (){}

    public Instruction(ClientToServerInstructionType clientToServerInstructionType, String content) {
        this.clientToServerInstructionType = clientToServerInstructionType;
        this.content = content;
    }

    public Instruction(ServerToClientInstructionType serverToClientInstructionType, String content) {
        this.serverToClientInstructionType = serverToClientInstructionType;
        this.content = content;
    }

    public Instruction(ClientToServerInstructionType clientToServerInstructionType, String content, String addressedClient) {
        this.clientToServerInstructionType = clientToServerInstructionType;
        this.content = content;
        this.addressedClient = addressedClient;
    }

    public Instruction(ClientToServerInstructionType clientToServerInstructionType, String content, int age) {
        this.clientToServerInstructionType = clientToServerInstructionType;
        this.content = content;
        this.age = age;
    }

    public ClientToServerInstructionType getClientToServerInstructionType() {
        return clientToServerInstructionType;
    }

    public ServerToClientInstructionType getServerToClientInstructionType() {
        return serverToClientInstructionType;
    }

    public String getContent() {
        return content;
    }

    public String getAddressedClient() {
        return addressedClient;
    }

    public String getAddendum(ServerToClientInstructionType serverToClientInstructionType) {
        switch (serverToClientInstructionType) {
            case WELCOME:
                return "Welcome ";
            //TODO adapt text according to status (ready / not ready)
            case PLAYER_STATUS:
                return " PLAYER_STATUS text has to be edited";
            case PLAYER_STATUS_FAIL:
                return " the game has already started";
            case CLIENT_LEAVES:
                return " left the room";
        }
        return null; //ERROR
    }

    /**
     * Client to Server instructions
     *
    *  @author Ivan
    *  @author Manu
    *  @author Mia
    */

    public enum ClientToServerInstructionType {

        //ClientChatInstructions
        HELLO_SERVER, //Client sends group name, protocol-vs and KI-on/off to Server
        SEND_CHAT, //Client sends public message to all, the value of "to" of the JSON-message must be -1
        SEND_PRIVATE_CHAT, //Clients sends private message to another player via the server
        BYE, //Client leaves game and informs server thereof

        //ClientGameInstruction
        PLAYER_VALUES, //Client sends player-name and player figure to server, where availability is checked
        SET_STATUS, //Client signals to server if player is ready
        PLAY_CARD, //Player plays a card
        SET_STARTING_POINT, //Player chooses starting point and informs server of her or his choice
        SELECT_CARDS, /*Player selects cards, each selected card is sent to the server after five have been chosen.
                        If a register is emptied the card value is 'null'*/
        CARDS_SELECTED, //Client informs client that a card has been put in the register
        SELECTION_FINISHED, //Client informs server that a player has filled his or her full register
    }

    /**
     * Server to Client instructions
     *
     *  @author Ivan
     *  @author Manu
     *  @author Mia
     */

    public enum ServerToClientInstructionType {

        // ServerChatInstructions
        HELLO_CLIENT, //Server sends protocol version to client
        WELCOME, //Client gets a player ID from the server
        RECEIVED_CHAT, //Server distributes message to all
        RECEIVED_PRIVATE_CHAT, //Server distributes private message to the appropriate player
        ERROR, //Server informs client that a transmission error occurred
        PLAYER_STATUS_FAIL, //Server informs client that a game has already started
        CLIENT_LEAVES, // Server removes client from active clients and closes socket


        //ServerGameInstruction
        PLAYER_ADDED, //Server confirms player_name and player_figure
        PLAYER_STATUS, //Server informs all other players of the status of the new player
        GAME_STARTED, //Server sends maps to clients
        CARD_PLAYED, //Server notifies all other players of the played card
        CURRENT_PLAYER, //Server informs all of the player that is to move
        ACTIVE_PHASE, //Server informs all about the current game phase
        STARTING_POINT_TAKEN, //Server confirms starting point and informs other players of it
        YOUR_CARDS, //Server informs player of her or his hand
        NOT_YOUR_CARD, //Server informs other players of the number of cards another has
        SHUFFLE_CODING, //If not enough cards are on the draw pile, discarded pile has to be reshuffled
        TIMER_STARTED, //Server starts timer as soon as someone has a full register
        TIMER_ENDED, //Server informs all clients that time for choosing programming cards has run out; player IDs of too slow players are saved
        CARDS_YOU_GOT_NOW, //Server fills empty registers after timer ended
        CURRENT_CARDS, //Server informs all players of the cards in the current register
        MOVEMENT, //Server informs other players of a move made (just moving, not turning)
        PLAYER_TURNING, //Server informs all clients if a player turns (left, right)
        PLAYER_SHOOTING, //For animation purposes (?)
        DRAW_DAMAGE, //Server informs player of damage suffered in round; the damage will be handed out in fixed bundles, no individual damage is given
        REBOOT, //Server informs all player if another one has to reboot
        ENERGY, //Server informs client of new energy level and its reason for changing
        CHECKPOINT_REACHED, //Server informs all players if a player reached a checkpoint
        GAME_FINISHED, //Server informs players if a player has won
    }
}