package utils.json.protocol;

import com.google.gson.annotations.Expose;
import server.Server;
import server.game.Card;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'PlayCard' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayCardBody implements ClientMessageAction<PlayCardBody> {
    @Expose
    private Card card;

    public PlayCardBody(Card card) {
        this.card = card;
    }

    @Override
    public void triggerAction(Server server, Server.ServerReaderTask task, PlayCardBody bodyObject) {
        MessageDistributer.handlePlayCard(server, task, bodyObject);
    }

    public Card getCard() {
        return card;
    }
}
