package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'SetStartingPoint' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SetStartingPointBody {
    @Expose
    private Integer x;
    @Expose
    private Integer y;

    public SetStartingPointBody(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
