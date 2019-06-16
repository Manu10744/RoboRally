package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'Movement' protocol JSON message.
 * @author Manuel Neumayer
 */
public class MovementBody {
    @Expose
    private int playerID;
    @Expose
    private int x;
    @Expose
    private int y;

    public MovementBody(int playerID, int x, int y) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
