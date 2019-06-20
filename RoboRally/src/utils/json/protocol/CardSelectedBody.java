package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'CardSelected' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CardSelectedBody {
    @Expose
    private Integer playerID;
    @Expose
    private Integer register;

    public CardSelectedBody(int playerID, Integer register) {
        this.playerID = playerID;
        this.register = register;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Integer getRegister() {
        return register;
    }
}
