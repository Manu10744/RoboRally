package utils.json.protocol;

public class StartingPointTakenBody {
    private int x;
    private int y;
    private int playerID;

    public StartingPointTakenBody(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlayerID() {
        return playerID;
    }
}
