package server.game.ProgrammingCards;

import server.game.Player;
import server.game.Robot;
import server.game.Tiles.*;
import utils.json.MessageDistributer;

import java.util.Map;
import java.util.logging.Logger;

/**
 * This class implements the Again card.
 *
 * @author Vincent Tafferner
 * @author Manu Neumayer
 * @author Jessica Gerlach
 * @author Verena Sadtler
 *
 */
public class Again extends server.game.Card {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger(MessageDistributer.class.getName());

    public Again() {
        cardName = "Again";
    }

    /**
     * This move will repeat the move that was performed in the previous register. <br>
     * If there was no previous move the robot shall do nothing.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap, Map<String, Robot> robotMap, Map<String, Antenna> antennaMap, Map<String, Belt> beltMap, Map<String, RotatingBelt> rotatingBeltMap) {
        logger.info(ANSI_GREEN + "ACTIVATING CARD 'AGAIN' ..." + ANSI_RESET);
    }

    @Override
    public boolean isDamageCard() {
        return false;
    }

}
