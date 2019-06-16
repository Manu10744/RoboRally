package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'Welcome' protocol JSON message.
 * @author Manuel Neumayer
 */
public class WelcomeBody {
    @Expose
    private int playerID;

    public WelcomeBody(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
