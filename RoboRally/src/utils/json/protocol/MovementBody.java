package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'Movement' protocol JSON message.
 * @author Manuel Neumayer
 */
public class MovementBody {
    @Expose
    private Integer playerID;
    @Expose
    private Integer x;
    @Expose
    private Integer y;

    public MovementBody(Integer playerID, Integer x, Integer y) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
