package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'SetStatus' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SetStatusBody {
    @Expose
    private boolean ready;

    public SetStatusBody(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
}
