package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'ActivePhase' protocol JSON message.
 * @author Manuel Neumayer
 */
public class ActivePhaseBody {
    @Expose
    private int phase;

    public ActivePhaseBody(int phase) {
        this.phase = phase;
    }

    public int getPhase() {
        return phase;
    }
}
