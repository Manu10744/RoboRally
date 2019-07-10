package utils.json.protocol;

import com.google.gson.annotations.Expose;
import server.Server;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'SetStartingPoint' protocol JSON message.
 * @author Manuel Neumayer
 */
public class SetStartingPointBody implements ClientMessageAction<SetStartingPointBody> {
    @Expose
    private Integer x;
    @Expose
    private Integer y;

    public SetStartingPointBody(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void triggerAction(Server server, Server.ServerReaderTask task, SetStartingPointBody messageBodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleSetStartingPoint(server, task, messageBodyObject);
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}
