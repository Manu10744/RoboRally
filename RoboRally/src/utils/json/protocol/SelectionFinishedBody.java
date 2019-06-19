package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'SelectionFinished' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SelectionFinishedBody {
    @Expose
    private Integer playerID;

    public SelectionFinishedBody(Integer playerID) {
        this.playerID = playerID;
    }

    public Integer getPlayerID() {
        return playerID;
    }
}
