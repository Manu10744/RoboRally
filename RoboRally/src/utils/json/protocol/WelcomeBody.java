package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'Welcome' protocol JSON message.
 * @author Manuel Neumayer
 */
public class WelcomeBody {
    @Expose
    private Integer playerID;

    public WelcomeBody(Integer playerID) {
        this.playerID = playerID;
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
