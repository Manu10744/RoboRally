package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'Reboot' protocol JSON message.
 * @author Manuel Neumayer
 */
public class RebootBody {
    @Expose
    private int playerID;

    public RebootBody(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
