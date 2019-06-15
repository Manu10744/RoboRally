package utils.json.protocol;

public class PlayerTurningBody {
    private int playerID;
    private String direction;

    public PlayerTurningBody(int playerID, String direction) {
        this.playerID = playerID;
        this.direction = direction;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getDirection() {
        return direction;
    }
}
