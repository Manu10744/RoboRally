package utils.instructions;

import java.io.Serializable;

/**
 * //TODO add a suiting description.
 *
 * @author Ivan Dovecar
 */
public class Instruction implements Serializable {

    public Instruction() {

    }

    // Übersicht (für Ivan)
    public enum InstructionType{
        // ServerChatInstructions
        HELLO_CLIENT, //Server sends Protocol-vs to Client
        WELCOME, // Client gets a player ID from the server
        RECEIVED_CHAT, //Server distributes message to all
        RECEIVED_PRIVATE_CHAT, // Server distributes private message to the appropriate player

        ERROR, // Server informs client that a transmission error occurred
        NAME_INVALID, // Will be part of meesage type "error", message body contains name_invalid -> new method call

        //ClientChatInstructions
        HELLO_SERVER, //Client sends group name, protocol-vs and KI-on/off to Server
        SEND_CHAT, //Client sends public message to all, the value of "to" of the JSON-message must be -1
        SEND_PRIVATE_CHAT, //Clients sends private message to another player via the server
        CLIENT_LEAVES, // Client informs Server if clients leaves the game
        BYE, // Client leaves game and informs server thereof

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

        //ServerGameInstruction
        PLAYER_ADDED, //Server confirms player_name and player_figure
        PLAYER_STATUS, // Server informs all other players of the status of the new player
        GAME_STARTED, // Server sends maps to clients
        CARD_PLAYED, // Server notifies all other players of the played card
        CURRENT_PLAYER, // Server informs all of the player that is to move
        ACTIVE_PHASE, // Server informs all about the current game phase
        STARTING_POINT_TAKEN, // Server confirms starting point and informs other players of it
        YOUR_CARDS, // Server informs player of her or his hand
        NOT_YOUR_CARD, // Server informs other players of the number of cards another has
        SHUFFLE_CODING, // If not enough cards are on the discarded pile it ha sto be reshuffled
        TIMER_STARTED, // Server starts timer if someone has a full register
        TIMER_ENDED, // Server informs all clients that time for choosing programming cards has run out; player IDs of too slow players is saved
        CARDS_YOU_GOT_NOW, // Server fills empty registers after time ran out
        CURRENT_CARDS, // Server informs all palyers of the cards in the current register
        MOVEMENT, // Server informs other players of a move made (just walking, no turning)
        DRAW_DAMAGE, // Server informs player of damage suffered in round; the damage will be handed out in fixed bundles, no individual damage is given
        REBOOT, // Server informs all player if another one has to reboot
        PLAYER_TURNING, // Server informs all clients if a player turns (left, right)
        ENERGY, // Server informs client of new energy level and its reason for changing
        CHECKPOINT_REACHED, // Server informs all palyers if a player reached a checkpoint
        GAME_FINISHED // Server informs palyers if a player has won
    }

    private ClientToServerInstructionType clientToServerInstructionType;
    private ServerToClientInstructionType serverToClientInstructionType;
    private String content;
    private String addressedClient;
    private int age;

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

    public int getAge() {
        return age;
    }

    public String getAddendum(ServerToClientInstructionType serverToClientInstructionType) {
        switch (serverToClientInstructionType) {
            case CLIENT_JOINED:
                return " joined the room";
            case WELCOME:
                return "Welcome ";
            case GAME_INIT:
                return " initialized a new game\nYou wanna play? Join the Game!";
            case GAME_INIT_FAIL1:
                return " can't initialize. A game is already initialized";
            case GAME_JOIN:
                return " joins the game";
            case GAME_JOIN_FAIL1:
                return " can't join. No game has been initialized";
            case GAME_JOIN_FAIL2:
                return " can't join. Maximum number of players already joined the game";
            case GAME_JOIN_FAIL3:
                return " can't join. The game already started";
            case GAME_START:
                return " started the game";
            case GAME_START_FAIL1:
                return " can't start the game. At least one other player must join";
            case GAME_START_FAIL2:
                return " can't start an already running game";
            case GAME_START_FAIL3:
                return " can't start a not initialized game";
            case CLIENT_LEAVES:
                return " left the room";
        }
        return null; //ERROR
    }

    /**
     * ChatInstruction: Client to Server
    */
    public enum ClientToServerInstructionType {
        // CHAT
        CHECK_NAME, // Ersetzt, player_added
        SEND_MESSAGE, // Ersetzt, send_chat
        SEND_PRIVATE_MESSAGE, // Ersetzt, send_private_chat
        HELLO_SERVER, //Client sends group name, protocol-vs and KI-on/off to Server
        SEND_CHAT, //Client sends public message to all, the value of "to" of the JSON-message must be -1
        SEND_PRIVATE_CHAT, //Clients sends private message to another player via the server
        BYE, // Client leaves game and informs server thereof

        // GAME
        INIT_GAME, // ENTFÄLLT
        JOIN_GAME, // ENTFÄLLT
        START_GAME, // ENTFÄLLT
        PLAYER_VALUES, //Client sends player-name and player figure is sent to server
        SET_STATUS, // Client signals to server if player is ready
        PLAY_CARD, // Player plays a card
        SET_STARTING_POINT, // Player chooses starting point and informs server of her or his choice
        SELECT_CARDS, /* Player selects cards, each selected card is sent to the srever after five have been chosen.
                        If a register is emptied the card value is 'null'*/
        CARDS_SELECTED, //Client informs client that a card has been put in the register
        SELECTION_FINISHED, // Client informs server that a player has filled his or her full register
    }

    /**
     * ChatInstruction: Server to Client
     */
    public enum ServerToClientInstructionType {
        // CHAT
        NAME_SUCCESS, // Ersetzt, name_added
        CLIENT_JOINED, // Ersetzt, welcome
        CLIENT_REGISTER, // Ersetzt, player_added
        CLIENT_WELCOME, // Ersetzt,  welcome
        NEW_MESSAGE, // Ersetzt, receive_chat
        CLIENT_LEAVES,
        KILL_CLIENT, // Removed,  will be merged with client_leaves

        HELLO_CLIENT, //Server sends Protocol-vs to Client
        WELCOME, // Client gets a player ID from the server
        RECEIVED_CHAT, //Server distributes message to all
        RECEIVED_PRIVATE_CHAT, // Server distributes private message to the appropriate player
        ERROR, // Server informs client that a transmission error occurred
        NAME_INVALID, // Will be part of message type "error", message body contains name_invalid -> new method call

        // GAME
        GAME_INIT, // ENTFÄLLT
        GAME_INIT_FAIL1, // ENTFÄLLT
        GAME_JOIN, // ENTFÄLLT
        GAME_JOIN_FAIL1, // ENTFÄLLT
        GAME_JOIN_FAIL2, // UNSICHER
        GAME_JOIN_FAIL3, // UNSICHER
        GAME_START, // ENTFÄLLT
        GAME_START_FAIL1, // ENTFÄLLT, STARTET AUTOMATISCH
        GAME_START_FAIL2, // ENTFÄLLT
        GAME_START_FAIL3, // ENTFÄLLT, GAME IST IMMER INITIALIZED

        PLAYER_ADDED, //Server confirms player_name and player_figure
        PLAYER_STATUS, // Server informs all other players of the status of the new player
        GAME_STARTED, // Server sends maps to clients
        CARD_PLAYED, // Server notifies all other players of the played card
        CURRENT_PLAYER, // Server informs all of the player that is to move
        ACTIVE_PHASE, // Server informs all about the current game phase
        STARTING_POINT_TAKEN, // Server confirms starting point and informs other players of it
        YOUR_CARDS, // Server informs player of her or his hand
        NOT_YOUR_CARD, // Server informs other players of the number of cards another has
        SHUFFLE_CODING, // If not enough cards are on the discarded pile it ha sto be reshuffled
        TIMER_STARTED, // Server starts timer if someone has a full register
        TIMER_ENDED, // Server informs all clients that time for choosing programming cards has run out; player IDs of too slow players is saved
        CARDS_YOU_GOT_NOW, // Server fills empty registers after time ran out
        CURRENT_CARDS, // Server informs all players of the cards in the current register
        MOVEMENT, // Server informs other players of a move made (just walking, no turning)
        DRAW_DAMAGE, // Server informs player of damage suffered in round; the damage will be handed out in fixed bundles, no individual damage is given
        REBOOT, // Server informs all player if another one has to reboot
        PLAYER_TURNING, // Server informs all clients if a player turns (left, right)
        ENERGY, // Server informs client of new energy level and its reason for changing
        CHECKPOINT_REACHED, // Server informs all players if a player reached a checkpoint
        GAME_FINISHED, // Server informs players if a player has won
        PLAYER_SHOOTING, //For animation purposes (?)
    }
}