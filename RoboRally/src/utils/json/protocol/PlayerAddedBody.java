package utils.json.protocol;

public class PlayerAddedBody {
    private int playerID;
    private String name;
    private int figure;

    public PlayerAddedBody(int playerID, String name, int figure) {
        this.playerID = playerID;
        this.name = name;
        this.figure = figure;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getName() {
        return name;
    }

    public int getFigure() {
        return figure;
    }
}
