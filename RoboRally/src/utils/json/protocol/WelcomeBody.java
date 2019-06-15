package utils.json.protocol;

public class WelcomeBody {
    private int playerID;

    public WelcomeBody(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
