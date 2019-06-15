package utils.json.protocol;

public class CheckPointReachedBody {
    private int playerID;
    private int number;

    public CheckPointReachedBody(int playerID, int number) {
        this.playerID = playerID;
        this.number = number;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getNumber() {
        return number;
    }
}
