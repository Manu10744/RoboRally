package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'NotYourCards' protocol JSON message.
 * @author Manuel Neumayer
 */
public class NotYourCardsBody implements ServerMessageAction<NotYourCardsBody> {
    @Expose
    private Integer playerID;
    @Expose
    private Integer cardsInHand;
    @Expose
    private Integer cardsInPile;

    public NotYourCardsBody(Integer playerID, Integer cardsInHand, Integer cardsInPile) {
        this.playerID = playerID;
        this.cardsInHand = cardsInHand;
        this.cardsInPile = cardsInPile;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, NotYourCardsBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleNotYourCards(client, task, bodyObject);
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public Integer getCardsInHand() {
        return cardsInHand;
    }

    public Integer getCardsInPile() {
        return cardsInPile;
    }
}
