package utils.json;

import com.google.gson.*;
import utils.instructions.*;
import utils.json.JSONMessage;

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

    public static ServerChatInstruction.ServerChatInstructionType getInstructionByMessageType(JSONMessage message){
        switch (message.getMessageType()){
            case "HelloClient": return ServerChatInstruction.ServerChatInstructionType.CLIENT_WELCOME;
        }
        return null; //error?
    }

   /* public static ClientChatInstruction.ClientChatInstructionType getInstructionByMessageType(JSONMessage message){
        ServerChatInstruction.ServerChatInstructionType instruction;

        switch (message.getMessageType()){
            case "HelloServer": return ClientChatInstruction.ClientChatInstructionType.CHECK_NAME; //correct instructions missing
        }
        return null;
    }

    public static ServerGameInstruction.ServerGameInstructionType getInstructionByMessageType(JSONMessage message){
        ServerChatInstruction.ServerChatInstructionType instruction;

        switch (message.getMessageType()){
            case "HelloClient": instruction = ServerChatInstruction.ServerChatInstructionType.CLIENT_WELCOME;;
        }
        return instruction;
    }

    public static ClientGameInstruction.ClientGameInstructionType getInstructionByMessageType(JSONMessage message){

        switch (message.getMessageType()){
            case "HelloClient": return ServerGameInstruction.ServerGameInstructionType;
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

    */
}
