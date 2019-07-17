package server.game.ProgrammingCards;

import server.game.Player;
import utils.json.MessageDistributer;

import java.util.logging.Logger;

/**
 * This class implements the PowerUp card.
 *
 * @author Vincent Tafferner
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
    public void activateCard(Player player) {
        logger.info(ANSI_GREEN + "ACTIVATING CARD 'POWERUP' ...");

        int currentEnergy = player.getEnergy();
        player.setEnergy(currentEnergy + 1);
    }

}
