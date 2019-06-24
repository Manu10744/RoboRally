package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import server.game.Maps.Map;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'GameStarted' protocol JSON message. Its main prupose is to sent the map
 * @author Mia
 */

public class GameStartedBody implements ServerMessageAction<GameStartedBody> {
    @Expose
    private Map gameMap;

    public GameStartedBody(Map gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, GameStartedBody bodyObject) {
        MessageDistributer.handleGameStarted(client, task, bodyObject);
    }

    public Map getGameMap() {
        return this.gameMap;
    }
}
