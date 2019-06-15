package utils.json.protocol;

import modelserver.game.Card;

import java.util.ArrayList;

public class CardsYouGotNowBody {
    private ArrayList<Card> cards;

    public CardsYouGotNowBody(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
