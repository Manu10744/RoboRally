package utils.json;

import com.google.gson.*;
import utils.json.JSONMessage;
import utils.json.MessageBody;

/**
 * This class is responsible for the serialization (Java to JSON) of java objects
 * It makes use of the Gson library.
 *
 * @author Manuel Neumayer
 */
public class JSONEncoder {

    public String serializeJSON(JSONMessage messageObj) {
        Gson gson = new Gson();
        // Convert the object into a JSON String
        String json = gson.toJson(messageObj);

        return json;
    }











    // JSON Message No.2 according to the protocol PDF
    // NOTE: This code is pure example code to show how deserialization works! Will be deleted later.
    public static void main(String[] args) {
        // Create a new Gson object
        Gson gson = new Gson();
        // Create the messageBody for the message
        MessageBody body = new MessageBody();
        // Create the message
        JSONMessage message = new JSONMessage("HelloClient", body);
        String json = gson.toJson(message);


        // Test if it worked
        System.out.println(json);
    }
}