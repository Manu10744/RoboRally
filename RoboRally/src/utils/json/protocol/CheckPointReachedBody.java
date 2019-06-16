package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'CheckPointReached' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CheckPointReachedBody {
    @Expose
    private int playerID;
    @Expose
    private int number;

    public CheckPointReachedBody(int playerID, int number) {
        this.playerID = playerID;
        this.number = number;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getNumber() {
        return number;
    }
}
