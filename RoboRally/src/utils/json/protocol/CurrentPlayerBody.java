package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'CurrentPlayer' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CurrentPlayerBody {
    @Expose
    private int playerID;

    public CurrentPlayerBody(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
