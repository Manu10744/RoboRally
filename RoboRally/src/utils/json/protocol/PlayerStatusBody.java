package utils.json.protocol;

public class PlayerStatusBody {
    private int playerID;
    private boolean ready;

    public PlayerStatusBody(int playerID, boolean ready) {
        this.playerID = playerID;
        this.ready = ready;
    }

    public int getPlayerID() {
        return playerID;
    }

    public boolean isReady() {
        return ready;
    }
}
