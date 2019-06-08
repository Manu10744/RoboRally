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
