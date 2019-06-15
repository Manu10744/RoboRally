package utils.json.protocol;

public class GameFinishedBody {
    private int playerID;

    public GameFinishedBody(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
