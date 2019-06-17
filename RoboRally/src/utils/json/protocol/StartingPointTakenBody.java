package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'StartingPointTaken' protocol JSON message.
 * @author Manuel Neumayer
 */
public class StartingPointTakenBody {
    @Expose
    private int x;
    @Expose
    private int y;
    @Expose
    private int playerID;

    public StartingPointTakenBody(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlayerID() {
        return playerID;
    }
}
