package utils.json.protocol;

public class SelectionFinishedBody {
    private int playerID;

    public SelectionFinishedBody(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
