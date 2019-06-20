package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'PlayerAdded' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerAddedBody {
    @Expose
    private Integer playerID;
    @Expose
    private String name;
    @Expose
    private Integer figure;

    public PlayerAddedBody(Integer playerID, String name, Integer figure) {
        this.playerID = playerID;
        this.name = name;
        this.figure = figure;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public String getName() {
        return name;
    }

    public Integer getFigure() {
        return figure;
    }
}
