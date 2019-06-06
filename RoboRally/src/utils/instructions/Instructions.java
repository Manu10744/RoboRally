package utils.instructions;

import java.io.Serializable;

/**
 * //TODO add a suiting description.
 *
 * @author Ivan Dovecar
 */
public class Instructions implements Serializable {

    private ClientToServerInstructionType clientToServerInstructionType;
    private ServerToClientInstructionType serverToClientInstructionType;
    private String content;
    private String addressedClient;
    private int age;

    public Instructions(ClientToServerInstructionType clientToServerInstructionType, String content) {
        this.clientToServerInstructionType = clientToServerInstructionType;
        this.content = content;
    }

    public Instructions(ServerToClientInstructionType serverToClientInstructionType, String content) {
        this.serverToClientInstructionType = serverToClientInstructionType;
        this.content = content;
    }

    public Instructions(ClientToServerInstructionType clientToServerInstructionType, String content, String addressedClient) {
        this.clientToServerInstructionType = clientToServerInstructionType;
        this.content = content;
        this.addressedClient = addressedClient;
    }

    public Instructions(ClientToServerInstructionType clientToServerInstructionType, String content, int age) {
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
            case CLIENT_WELCOME:
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

        CHECK_NAME, // MOVED ->  ClientChatInstruction
        SEND_MESSAGE, // MOVED -> ClientChatInstruction
        SEND_PRIVATE_MESSAGE, // MOVED -> ClientChatInstruction
        BYE, // MOVED -> ClientChatInstruction

        // GAME
        INIT_GAME, // ENTFÄLLT
        JOIN_GAME, // ENTFÄLLT
        START_GAME // ENTFÄLLT
    }

    /**
     * ChatInstruction: Server to Client
     */
    public enum ServerToClientInstructionType {
        // CHAT
        NAME_INVALID, // MOVED -> ServerChatInstruction
        NAME_SUCCESS, // MOVED -> ServerChatInstruction
        CLIENT_JOINED, // MOVED -> ServerChatInstruction
        CLIENT_REGISTER, // MOVED -> ServerChatInstruction
        CLIENT_WELCOME, // MOVED -> ServerChatInstruction
        NEW_MESSAGE, // MOVED -> ServerChatInstruction
        CLIENT_LEAVES, // MOVED -> ServerChatInstruction
        KILL_CLIENT, // UNSICHER OB FUSION MIT CLIENT_LEAVES, FALLS OK BITTE MACHEN,
                     // VORERST VERSCHOBEN IN ServerChatInstruction

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
    }
}