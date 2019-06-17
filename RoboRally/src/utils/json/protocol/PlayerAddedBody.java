package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'PlayerAdded' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerAddedBody {
    @Expose
    private int playerID;
    @Expose
    private String name;
    @Expose
    private int figure;

    public PlayerAddedBody(int playerID, String name, int figure) {
        this.playerID = playerID;
        this.name = name;
        this.figure = figure;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getName() {
        return name;
    }

    public int getFigure() {
        return figure;
    }
}
