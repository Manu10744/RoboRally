package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'PlayerTurning' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerTurningBody {
    @Expose
    private int playerID;
    @Expose
    private String direction;

    public PlayerTurningBody(int playerID, String direction) {
        this.playerID = playerID;
        this.direction = direction;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getDirection() {
        return direction;
    }
}
