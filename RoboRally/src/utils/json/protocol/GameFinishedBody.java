package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'GameFinished' protocol JSON message.
 * @author Manuel Neumayer
 */
public class GameFinishedBody {
    @Expose
    private Integer playerID;

    public GameFinishedBody(Integer playerID) {
        this.playerID = playerID;
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
