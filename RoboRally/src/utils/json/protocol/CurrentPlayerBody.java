package utils.json.protocol;

public class CurrentPlayerBody {
    private int playerID;

    public CurrentPlayerBody(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
