package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'CurrentPlayer' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CurrentPlayerBody {
    @Expose
    private Integer playerID;

    public CurrentPlayerBody(Integer playerID) {
        this.playerID = playerID;
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
