package utils.json;

import com.google.gson.*;
import utils.json.protocol.*;

/**
 * This class is responsible for the serialization (Java to JSON) of java objects.<br>
 * It makes use of the Gson library.
 *
 * @author Manuel Neumayer
 */
public class JSONEncoder {

    public static String serializeJSON(JSONMessage messageObj) {
        Gson gson = new Gson();
        // Convert the object into a JSON String
        String jsonString = gson.toJson(messageObj);

        return jsonString;
    }













    // JSON Message No.2 according to the protocol PDF
    // NOTE: This code is pure example code to show how deserialization works! Will be deleted later.
    public static void main(String[] args) {
        // Create a new Gson object
        Gson gson = new Gson();
        JSONMessage message = new JSONMessage("HelloClient", new HelloClientBody("Version 1.0"));
        String json = JSONEncoder.serializeJSON(message);
        System.out.println(json);

    }
}
