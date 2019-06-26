package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import server.game.Tiles.Tile;
import utils.json.MessageDistributer;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'GameStarted' protocol JSON message. Its main purpose is to sent the map
 * @author Mia
 */

public class GameStartedBody implements ServerMessageAction<GameStartedBody> {

    @Expose @SerializedName("gameMap")
    private ArrayList<ArrayList<ArrayList<Tile>>> mapBody;
    private ArrayList<Tile> tileList;
    private ArrayList<ArrayList<Tile>> doubledNestedArray;

    public GameStartedBody(ArrayList<ArrayList<ArrayList<Tile>>> mapBody) {
        this.mapBody = mapBody;
    }

    public ArrayList<ArrayList<ArrayList<Tile>>> getXArray() {
        return mapBody;
    }
    public ArrayList<ArrayList<Tile>> getYArray() {
        return doubledNestedArray;
    }


    public ArrayList<Tile> getTileArrayFromMapBody(int posX, int posY) {
        ArrayList<Tile> tileList = new ArrayList<>();
        try {
            if (this.mapBody.isEmpty()) {
                doubledNestedArray.add(tileList);
                mapBody.add(doubledNestedArray);
                tileList = mapBody.get(posX).get(posY);
            } else if (!this.mapBody.get(posX).get(posX).isEmpty()) {
                tileList = this.mapBody.get(posX).get(posY);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return tileList;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, GameStartedBody bodyObject) {
        MessageDistributer.handleGameStarted(client, task, bodyObject);
    }
}
