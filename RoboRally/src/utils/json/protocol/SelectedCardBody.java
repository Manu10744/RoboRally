package utils.json.protocol;

import com.google.gson.annotations.Expose;
import server.Server;
import server.game.Card;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'SelectCard' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SelectedCardBody implements ClientMessageAction<SelectedCardBody> {
    @Expose
    private Card card;
    @Expose
    private Integer register;

    public SelectedCardBody(Card card, Integer register) {
        this.card = card;
        this.register = register;
    }

    @Override
    public void triggerAction(Server server, Server.ServerReaderTask task, SelectedCardBody messageBodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleSelectedCard(server, task, messageBodyObject);
    }

    public Card getCard() {
        return card;
    }

    public Integer getRegister() {
        return register;
    }
}
