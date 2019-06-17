package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'PlayerStatus' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerStatusBody {
    @Expose
    private int playerID;
    @Expose
    private boolean ready;

    public PlayerStatusBody(int playerID, boolean ready) {
        this.playerID = playerID;
        this.ready = ready;
    }

    public int getPlayerID() {
        return playerID;
    }

    public boolean isReady() {
        return ready;
    }
}
