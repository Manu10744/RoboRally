package utils.json.protocol;

public class RebootBody {
    private int playerID;

    public RebootBody(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
