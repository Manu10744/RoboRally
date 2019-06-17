package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'GameFinished' protocol JSON message.
 * @author Manuel Neumayer
 */
public class GameFinishedBody {
    @Expose
    private int playerID;

    public GameFinishedBody(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
