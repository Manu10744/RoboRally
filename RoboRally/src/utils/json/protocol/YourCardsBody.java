package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import server.game.Card;
import utils.json.MessageDistributer;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'YourCards' protocol JSON message.
 * @author Manuel Neumayer
 */
public class YourCardsBody implements ServerMessageAction<YourCardsBody> {
    @Expose
    private ArrayList<Card> cardsInHand;
    @Expose
    private Integer cardsInPile;

    public YourCardsBody(ArrayList<Card> cardsInHand, Integer cardsInPile) {
        this.cardsInHand = cardsInHand;
        this.cardsInPile = cardsInPile;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, YourCardsBody bodyObject) {
        MessageDistributer.handleYourCards(client, task, bodyObject);
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public Integer getCardsInPile() {
        return cardsInPile;
    }
}
