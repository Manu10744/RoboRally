package utils.json.protocol;

public class MovementBody {
    private int playerID;
    private int x;
    private int y;

    public MovementBody(int playerID, int x, int y) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
