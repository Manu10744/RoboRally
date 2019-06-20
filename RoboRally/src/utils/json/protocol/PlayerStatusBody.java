package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'PlayerStatus' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerStatusBody {
    @Expose
    private Integer playerID;
    @Expose
    private Boolean ready;

    public PlayerStatusBody(Integer playerID, Boolean ready) {
        this.playerID = playerID;
        this.ready = ready;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Boolean isReady() {
        return ready;
    }
}
