package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'HelloClient' protocol JSON message.
 * @author Manuel Neumayer
 */
public class HelloClientBody {
    @Expose
    String protocol;

    public HelloClientBody(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }
}
