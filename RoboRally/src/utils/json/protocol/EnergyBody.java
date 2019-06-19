package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'Energy' protocol JSON message.
 * @author Manuel Neumayer
 */
public class EnergyBody {
    @Expose
    private Integer playerID;
    @Expose
    private Integer count;
    @Expose
    private String source;

    public EnergyBody(Integer playerID, Integer count, String source) {
        this.playerID = playerID;
        this.count = count;
        this.source = source;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Integer getCount() {
        return count;
    }

    public String getSource() {
        return source;
    }
}
