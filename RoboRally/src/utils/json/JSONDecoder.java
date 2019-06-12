package utils.json;

import com.google.gson.*;
import modelclient.Client;
import modelserver.Server;
import utils.instructions.*;
import utils.instructions.ServerGameInstruction;

/**
 * This class is responsible for the deserialization (JSON -> Java) of JSON-Messages.<br>
 * It makes use of the Gson library.
 *
 * @author Manuel Neumayer
 * @author Mia
 */

public class JSONDecoder {

    public static JSONMessage deserializeJSON(String json) {
        Gson gson = new Gson();
        // Map the received message into an JSONMessage object
        JSONMessage messageObj = gson.fromJson(json, JSONMessage.class);

        return messageObj;
    }

    public static ClientInstruction getClientInstructionByMessageType(JSONMessage jsonMessage) {
        // Declare a instruction and initialize it depending on the message type
        ClientInstruction clientInstruction;

        switch (jsonMessage.getMessageType()) {

            case "HelloServer":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.HELLO_SERVER);
                return clientInstruction;
            case "PlayerValues":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.PLAYER_VALUES);
                return clientInstruction;
            case "SetStatus":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SET_STATUS);
                return clientInstruction;
            case "SendChat":
                // Is public message
                if (jsonMessage.getMessageBody().getTo() == -1) {
                    clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SEND_CHAT);
                    return clientInstruction;
                } else {
                    // Is private message
                    clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SEND_PRIVATE_CHAT);
                    return clientInstruction;
                }
            case "PlayCard":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.PLAY_CARD);
                return clientInstruction;
            case "SetStartingPoint":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SET_STARTING_POINT);
                return clientInstruction;
            case "SelectCard":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SELECT_CARDS);
                return clientInstruction;
            case "CardSelected":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.CARDS_SELECTED);
                return clientInstruction;
            case "SelectionFinished":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SELECTION_FINISHED);
                return clientInstruction;
            default:
                return null;
        }
    }


    public static ServerInstruction getServerInstructionByMessageType(JSONMessage jsonMessage) {
       ServerInstruction serverInstruction;

        switch (jsonMessage.getMessageType()) {
            case "HelloClient":
                serverInstruction = new ServerInstruction(ServerInstruction.ServerInstructionType.HELLO_CLIENT);
                return serverInstruction;
            case "Welcome":
                serverInstruction = new ServerInstruction(ServerInstruction.ServerInstructionType.WELCOME);
                return serverInstruction;
            case "PlayerStatus":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.PLAYER_STATUS);
                return  serverInstruction;
            case "GameStarted":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.GAME_STARTED);
                return serverInstruction;
            case "ReceivedChat":
                // Is public message
                if (jsonMessage.getMessageBody().getPrivate() == false) {
                    serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.RECEIVED_CHAT);
                    return serverInstruction;
                } else {
                    // Is private message
                    serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.RECEIVED_PRIVATE_CHAT);
                    return serverInstruction;
                }
            case "Error":
                serverInstruction = new ServerInstruction(ServerInstruction.ServerInstructionType.ERROR);
                return serverInstruction;
            case "StartingPointTaken":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.STARTING_POINT_TAKEN);
                return serverInstruction;
            case "YourCards":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.YOUR_CARDS);
                return serverInstruction;
            case "NotYourCards":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.NOT_YOUR_CARD);
                return serverInstruction;
            case "ShuffleCoding":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.SHUFFLE_CODING);
                return serverInstruction;
            case "CardPlayed":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.CARD_PLAYED);
                return serverInstruction;
            case "CurrentPlayer":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.CURRENT_PLAYER);
                return serverInstruction;
            case "ActivePhase":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.ACTIVE_PHASE);
                return serverInstruction;
            case "Energy":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.ENERGY);
                return serverInstruction;
            case "CheckPointReached":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.CHECKPOINT_REACHED);
                return serverInstruction;
            case "GameFinished":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.GAME_FINISHED);
                return serverInstruction;
            case "PlayerAdded":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.PLAYER_ADDED);
                return serverInstruction;
            case "TimerStarted":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.TIMER_STARTED);
                return serverInstruction;
            case "TimerEnded":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.TIMER_ENDED);
                return serverInstruction;
            case "CardsYouGotNow":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.CARDS_YOU_GOT_NOW);
                return serverInstruction;
            case "CurrentCards":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.CURRENT_CARDS);
                return serverInstruction;
            case "Movement":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.MOVEMENT);
                return serverInstruction;
            case "DrawDamage":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.DRAW_DAMAGE);
                return serverInstruction;
            case "PlayerShooting":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.PLAYER_SHOOTING);
                return serverInstruction;
            case "Reboot":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.REBOOT);
                return serverInstruction;
            case "PlayerTurning":
                serverInstruction = new ServerInstruction(ServerInstruction.ServerInstructionType.PLAYER_TURNING);
                return serverInstruction;

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
