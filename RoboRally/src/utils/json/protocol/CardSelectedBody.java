package utils.json.protocol;

public class CardSelectedBody {
    private int playerID;
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
