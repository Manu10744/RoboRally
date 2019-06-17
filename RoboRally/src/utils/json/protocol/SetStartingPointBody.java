package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'SetStartingPoint' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SetStartingPointBody {
    @Expose
    private int x;
    @Expose
    private int y;

    public SetStartingPointBody(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
