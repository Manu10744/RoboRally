package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'CheckPointReached' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CheckPointReachedBody {
    @Expose
    private Integer playerID;
    @Expose
    private Integer number;

    public CheckPointReachedBody(Integer playerID, Integer number) {
        this.playerID = playerID;
        this.number = number;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Integer getNumber() {
        return number;
    }
}
