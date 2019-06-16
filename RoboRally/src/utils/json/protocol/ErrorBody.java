package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'Error' protocol JSON message.
 * @author Manuel Neumayer
 */
public class ErrorBody {
    @Expose
    private String error;

    public ErrorBody(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
