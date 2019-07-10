package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import server.game.Card;
import utils.json.MessageDistributer;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'CardsYouGotNow' protocol JSON message.
 * @author Manuel Neumayer
 */
public class CardsYouGotNowBody implements ServerMessageAction<CardsYouGotNowBody> {
    @Expose
    private ArrayList<Card> cards;

    public CardsYouGotNowBody(ArrayList<Card> cards) {
        this.cards = cards;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, CardsYouGotNowBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleCardsYouGotNow(client, task, bodyObject);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
