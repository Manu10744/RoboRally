package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'SelectionFinished' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SelectionFinishedBody {
    @Expose
    private int playerID;

    public SelectionFinishedBody(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}