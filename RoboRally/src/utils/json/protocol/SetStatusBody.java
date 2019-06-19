package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'SetStatus' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SetStatusBody {
    @Expose
    private Boolean ready;

    public SetStatusBody(Boolean ready) {
        this.ready = ready;
    }

    public Boolean isReady() {
        return ready;
    }
}
