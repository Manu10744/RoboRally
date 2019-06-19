package utils.json.protocol;

import com.google.gson.annotations.Expose;
import modelserver.game.Maps.Map;

/** This is the wrapper class for the message body of the 'GameStarted' protocol JSON message. Its main prupose is to sent the map
 * @author Mia
 */

public class GameStartedBody {
    @Expose
    private Map gameMap;

    public GameStartedBody(Map gameMap) {
        this.gameMap = gameMap;
    }


    public Map getGameMap() {
        return this.gameMap;
    }
}
