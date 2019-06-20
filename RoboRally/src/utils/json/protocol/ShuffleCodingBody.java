package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'ShuffleCoding' protocol JSON message.
 * @author Manuel Neumayer
 */
public class ShuffleCodingBody {
    @Expose
    private Integer playerID;

    public ShuffleCodingBody(Integer playerID) {
        this.playerID = playerID;
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
