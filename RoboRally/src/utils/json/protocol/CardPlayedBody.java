package utils.json.protocol;

public class CardPlayedBody {
    private int playerID;
    private String card;

    public CardPlayedBody(int playerID, String card) {
        this.playerID = playerID;
        this.card = card;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getCard() {
        return card;
    }
}
