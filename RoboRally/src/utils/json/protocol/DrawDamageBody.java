package utils.json.protocol;

import modelserver.game.Card;

import java.util.ArrayList;

public class DrawDamageBody {
    private int playerID;
    private ArrayList<Card> cards;

    public DrawDamageBody(int playerID, ArrayList<Card> cards) {
        this.playerID = playerID;
        this.cards = cards;
    }

    public int getPlayerID() {
        return playerID;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
