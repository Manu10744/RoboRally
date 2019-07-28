package server.game.ProgrammingCards;

import server.game.Player;
import server.game.Robot;
import server.game.Tiles.Antenna;
import server.game.Tiles.Pit;
import server.game.Tiles.PushPanel;
import server.game.Tiles.Wall;
import utils.json.MessageDistributer;

import java.util.Map;
import java.util.logging.Logger;

/**
 * This class implements the PowerUp card.
 *
 * @author Vincent Tafferner
 * @author Manu Neumayer
 * @author Verena Sadtler
 * @author Jessica Gerlach
 */
public class PowerUp extends server.game.Card {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger(MessageDistributer.class.getName());

    public PowerUp() {
        cardName = "PowerUp";
    }

    /**
     * Even though the name suggests a move, in this case the robot will activate a powerup instead of moving <br>
     * around on the map.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap, Map<String, Robot> robotMap, Map<String, Antenna> antennaMap) {
        logger.info(ANSI_GREEN + "ACTIVATING CARD 'POWERUP' ...");

        int currentEnergy = player.getEnergy();
        player.setEnergy(currentEnergy + 1);

    }

    @Override
    public boolean isDamageCard() {
        return false;
    }

}
