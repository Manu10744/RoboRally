package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'PlayerTurning' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerTurningBody {
    @Expose
    private Integer playerID;
    @Expose
    private String direction;

    public PlayerTurningBody(Integer playerID, String direction) {
        this.playerID = playerID;
        this.direction = direction;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public String getDirection() {
        return direction;
    }
}
