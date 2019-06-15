package utils.json.protocol;

public class EnergyBody {
    private int playerID;
    private int count;
    private String source;

    public EnergyBody(int playerID, int count, String source) {
        this.playerID = playerID;
        this.count = count;
        this.source = source;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getCount() {
        return count;
    }

    public String getSource() {
        return source;
    }
}
