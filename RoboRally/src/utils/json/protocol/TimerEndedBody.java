package utils.json.protocol;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'TimerEnded' protocol JSON message.
 * @author Manuel Neumayer
 */
public class TimerEndedBody {
    @Expose
    private ArrayList<Integer> playerIDs;

    public TimerEndedBody(ArrayList<Integer> playerIDs) {
        this.playerIDs = playerIDs;
    }

    public ArrayList<Integer> getPlayerIDs() {
        return playerIDs;
    }
}
