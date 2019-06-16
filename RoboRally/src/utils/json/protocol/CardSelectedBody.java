package utils.json.protocol;

import com.google.gson.annotations.Expose;

/** This is the wrapper class for the message body of the 'CardSelected' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CardSelectedBody {
    @Expose
    private int playerID;
    @Expose
    private int register;

    public CardSelectedBody(int playerID, int register) {
        this.playerID = playerID;
        this.register = register;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getRegister() {
        return register;
    }
}
