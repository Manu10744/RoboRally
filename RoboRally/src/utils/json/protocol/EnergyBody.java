package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'Energy' protocol JSON message.
 * @author Manuel Neumayer
 */
public class EnergyBody {
    @Expose
    private int playerID;
    @Expose
    private int count;
    @Expose
    private String source;

    public EnergyBody(int playerID, int count, String source) {
        this.playerID = playerID;
        this.count = count;
        this.source = source;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getCount() {
        return count;
    }

    public String getSource() {
        return source;
    }
}
