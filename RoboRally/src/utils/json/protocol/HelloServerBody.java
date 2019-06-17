package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'HelloServer' protocol JSON message.
 * @author Manuel Neumayer
 */
public class HelloServerBody {
    @Expose
    private String group;
    @Expose
    private boolean isAI;
    @Expose
    private String protocol;

    public HelloServerBody(String group, boolean isAI, String protocol) {
        this.group = group;
        this.isAI = isAI;
        this.protocol = protocol;
    }

    public String getGroup() {
        return group;
    }

    public boolean isAI() {
        return isAI;
    }

    public String getProtocol() {
        return protocol;
    }
}
