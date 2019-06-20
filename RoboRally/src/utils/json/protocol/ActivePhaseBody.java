package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'ActivePhase' protocol JSON message.
 * @author Manuel Neumayer
 */
public class ActivePhaseBody {
    @Expose
    private Integer phase;

    public ActivePhaseBody(Integer phase) {
        this.phase = phase;
    }

    public Integer getPhase() {
        return phase;
    }
}
