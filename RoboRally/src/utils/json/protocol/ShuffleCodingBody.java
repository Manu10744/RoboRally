package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'ShuffleCoding' protocol JSON message.
 * @author Manuel Neumayer
 */
public class ShuffleCodingBody {
    @Expose
    private int playerID;

    public ShuffleCodingBody(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
