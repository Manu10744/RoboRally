package utils.json.protocol;

import client.Client;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import server.game.Game;
import server.game.Tiles.Tile;
import utils.json.JSONDecoder;
import utils.json.MessageDistributer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'GameStarted' protocol JSON message. Its main purpose is to sent the map
 * @author Mia
 */

public class GameStartedBody implements ServerMessageAction<GameStartedBody> {

    @Expose @SerializedName("gameMap")
    private ArrayList<ArrayList<ArrayList<Tile>>> mapBody;
    @Expose
    private ArrayList<ArrayList<Tile>> doubledNestedArray;
    @Expose
    private ArrayList<Tile> tileList;

    public GameStartedBody(ArrayList<ArrayList<ArrayList<Tile>>> mapBody) {
        this.mapBody = mapBody;
    }

    public ArrayList<ArrayList<ArrayList<Tile>>> getXArray() {
        return mapBody;
    }


    public ArrayList<Tile> getTileArrayFromMapBody(int posX, int posY) {
        ArrayList<Tile> tileList = new ArrayList<>();
        try {
                tileList = this.mapBody.get(posX).get(posY);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return tileList;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, GameStartedBody bodyObject) {
        MessageDistributer.handleGameStarted(client, task, bodyObject);
    }

    public ArrayList<ArrayList<Tile>> getYArray() {
        return doubledNestedArray;
    }

    public ArrayList<Tile> getTileList() {
        return tileList;
    }
}
