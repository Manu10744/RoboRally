package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'StartingPointTaken' protocol JSON message.
 * @author Manuel Neumayer
 */
public class StartingPointTakenBody {
    @Expose
    private Integer x;
    @Expose
    private Integer y;
    @Expose
    private Integer playerID;

    public StartingPointTakenBody(Integer x, Integer y, Integer playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
