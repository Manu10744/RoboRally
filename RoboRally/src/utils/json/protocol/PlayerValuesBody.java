package utils.json.protocol;

import com.google.gson.annotations.Expose;
import server.Server;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'PlayerValues' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerValuesBody implements ClientMessageAction<PlayerValuesBody> {
    @Expose
    private String name;
    @Expose
    private Integer figure;

    public PlayerValuesBody(String name, Integer figure) {
        this.name = name;
        this.figure = figure;
    }

    @Override
    public void triggerAction(Server server, Server.ServerReaderTask task, PlayerValuesBody messageBodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handlePlayerValues(server, task, messageBodyObject);
    }

    public String getName() {
        return name;
    }

    public Integer getFigure() {
        return figure;
    }
}
