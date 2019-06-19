package utils.json.protocol;

import com.google.gson.annotations.Expose;
import modelserver.game.Player;

/** This is the wrapper class for the message body of the 'PlayerValues' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerValuesBody {
    @Expose
    private String name;
    @Expose
    private Integer figure;

    public PlayerValuesBody(String name, Integer figure) {
        this.name = name;
        this.figure = figure;
    }

    public String getName() {
        return name;
    }

    public Integer getFigure() {
        return figure;
    }
}
