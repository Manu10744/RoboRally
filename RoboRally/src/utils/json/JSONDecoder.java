package utils.json;

import com.google.gson.*;
import modelclient.Client;
import modelserver.Server;
import utils.instructions.*;
import utils.instructions.ServerGameInstruction;

/**
 * This class is responsible for the deserialization (JSON -> Java) of JSON-Messages.
 * It makes use of the Gson library.
 *
 * @author Manuel Neumayer
 * @author Mia
 */

public class JSONDecoder {

    public static JSONMessage deserializeJSON(String json) {
        Gson gson = new Gson();
        // Map the received message to the JSONMessage class
        JSONMessage messageObj = gson.fromJson(json, JSONMessage.class);

        return messageObj;

    }

    public Instruction getInstructionByMessageType(JSONMessage jsonMessage) {
        Instruction instruction;
        switch (jsonMessage.getMessageType()) {
            case "HelloClient":
                instruction = new ServerChatInstruction(ServerChatInstruction.ServerChatInstructionType.HELLO_CLIENT, null);
                return instruction;
            case "HelloServer":
                instruction = new ClientChatInstruction(ClientChatInstruction.ClientChatInstructionType.HELLO_SERVER, null);
                return instruction;
            case "Welcome":
                instruction = new ServerChatInstruction(ServerChatInstruction.ServerChatInstructionType.WELCOME, null);
                return instruction;
            case "PlayerValues":
                instruction = new ClientGameInstruction(ClientGameInstruction.ClientGameInstructionType.PLAYER_VALUES, null);
                return instruction;
            case "PlayerAdded":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.PLAYER_ADDED, null);
                return instruction;
            case "SetStatus":
                instruction = new ClientGameInstruction(ClientGameInstruction.ClientGameInstructionType.SET_STATUS, null);
                return instruction;
            case "PlayerStatus":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.PLAYER_STATUS, null);
                return instruction;
            case "GameStarted":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.GAME_STARTED, null);
                return instruction;
            case "SendChat":
                // Is public message
                if (jsonMessage.getMessageBody().getTo() == -1) {
                    instruction = new ClientChatInstruction(ClientChatInstruction.ClientChatInstructionType.SEND_CHAT, null);
                    return instruction;
                } else {
                    // Is private message
                    instruction = new ClientChatInstruction(ClientChatInstruction.ClientChatInstructionType.SEND_PRIVATE_CHAT, null);
                    return instruction;
                }
            case "ReceivedChat":
                // Is public message
                if (jsonMessage.getMessageBody().getPrivate() == false){
                    instruction = new ServerChatInstruction(ServerChatInstruction.ServerChatInstructionType.RECEIVED_CHAT, null);
                return instruction;
                } else {
                    // Is private message
                    instruction = new ServerChatInstruction(ServerChatInstruction.ServerChatInstructionType.RECEIVED_PRIVATE_CHAT, null);
                    return instruction;
                }
            case "Error":
                    instruction = new ServerChatInstruction(ServerChatInstruction.ServerChatInstructionType.ERROR, null);
                return  instruction;
            case "PlayCard":
                instruction = new ClientGameInstruction(ClientGameInstruction.ClientGameInstructionType.PLAY_CARD, null);
                return instruction;
            case "CardPlayed":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.CARD_PLAYED, null);
                return instruction;
            case "CurrentPlayer":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.CURRENT_PLAYER, null);
                return instruction;
            case "ActivePhase":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.ACTIVE_PHASE, null);
                return instruction;
            case "SetStartingPoint":
                instruction = new ClientGameInstruction(ClientGameInstruction.ClientGameInstructionType.SET_STARTING_POINT, null);
                return instruction;
            case "StartingPointTaken":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.STARTING_POINT_TAKEN, null);
                return  instruction;
            case "YourCards":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.YOUR_CARDS, null);
                return instruction;
            case "NotYourCards":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.NOT_YOUR_CARD, null);
                return instruction;
            case "ShuffleCoding":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.SHUFFLE_CODING, null);
                return instruction;
            case "SelectCard":
                instruction = new ClientGameInstruction(ClientGameInstruction.ClientGameInstructionType.SELECT_CARDS, null);
                return instruction;
            case"CardSelected":
                instruction = new ClientGameInstruction(ClientGameInstruction.ClientGameInstructionType.CARDS_SELECTED, null);
                return instruction;
            case "SelectionFinished":
                instruction = new ClientGameInstruction(ClientGameInstruction.ClientGameInstructionType.SELECTION_FINISHED, null);
                return instruction;
            case "TimerStarted":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.TIMER_STARTED, null);
                return instruction;
            case "TimerEnded":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.TIMER_ENDED, null);
                return instruction;
            case "CardsYouGotNow":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.CARDS_YOU_GOT_NOW, null);
                return  instruction;
            case "CurrentCards":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.CURRENT_CARDS, null);
                return instruction;
            case "Movement":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.MOVEMENT, null);
                return instruction;
            case "DrawDamage":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.DRAW_DAMAGE, null);
                return instruction;
            case "PlayerShooting":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.PLAYER_SHOOTING, null);
                return instruction;
            case "Reboot":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.REBOOT, null);
                return  instruction;
            case "PlayerTurning":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.PLAYER_TURNING, null);
                return instruction;
            case "Energy":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.ENERGY, null);
                return instruction;
            case "CheckPointReached":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.CHECKPOINT_REACHED, null);
                return  instruction;
            case "GameFinished":
                instruction = new ServerGameInstruction(ServerGameInstruction.ServerGameInstructionType.GAME_FINISHED, null);
                return instruction;

            default:
                return null;
        }
    }



    // JSON Message No.2 according to the protocol PDF
    // NOTE: This code is pure example code to show how deserialization works! Will be deleted later.
    public static void main(String[] args) {
        // Create a new Gson object
        Gson gson = new Gson();

        // Note: You dont have to write the json it down. A FileReader will work too.
        String jsonmsg = "{'messageType':'HelloClient','messageBody':{'protocol':'Version 0.1'}}";
        // Deserialize the above JSON message to a java object
        JSONMessage message = gson.fromJson(jsonmsg, JSONMessage.class);

        // Test, if it worked
        System.out.println(message.getMessageType());
        System.out.println(message.getMessageBody().getProtocol());

    }
}
