package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import server.game.Tiles.Tile;
import utils.json.MessageDistributer;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'GameStarted' protocol JSON message. Its main purpose is to sent the map
 * @author Mia Brandtner
 */

public class GameStartedBody implements ServerMessageAction<GameStartedBody> {

    @Expose @SerializedName("gameMap")
    private ArrayList<ArrayList<ArrayList<Tile>>> mapBody;




    public GameStartedBody(ArrayList<ArrayList<ArrayList<Tile>>> mapBody) {
        this.mapBody = mapBody;
    }

    public ArrayList<ArrayList<ArrayList<Tile>>> getXArray() {
        return mapBody;
    }


    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, GameStartedBody bodyObject, MessageDistributer messageDistributer) {
       messageDistributer.handleGameStarted(client, task, bodyObject);
    }

}
