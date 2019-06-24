package utils.json.protocol;

import client.Client;
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
    public void triggerAction(Server server, Server.ServerReaderTask task, PlayerValuesBody bodyObject) {
        MessageDistributer.handlePlayerValues(server, task, bodyObject);
    }

    public String getName() {
        return name;
    }

    public Integer getFigure() {
        return figure;
    }
}
