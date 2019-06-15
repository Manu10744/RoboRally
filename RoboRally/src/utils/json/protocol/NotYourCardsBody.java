package utils.json.protocol;

public class NotYourCardsBody {
    private int playerID;
    private int cardsInHand;
    private int cardsInPile;

    public NotYourCardsBody(int playerID, int cardsInHand, int cardsInPile) {
        this.playerID = playerID;
        this.cardsInHand = cardsInHand;
        this.cardsInPile = cardsInPile;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getCardsInHand() {
        return cardsInHand;
    }

    public int getCardsInPile() {
        return cardsInPile;
    }
}
