package utils.json.protocol;

import modelserver.game.Card;

import java.util.ArrayList;

public class YourCardsBody {
    private ArrayList<Card> cardsInHand;
    private int cardsInPile;

    public YourCardsBody(ArrayList<Card> cardsInHand, int cardsInPile) {
        this.cardsInHand = cardsInHand;
        this.cardsInPile = cardsInPile;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public int getCardsInPile() {
        return cardsInPile;
    }
}
