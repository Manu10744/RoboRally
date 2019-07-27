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
 * This class implements the TurnRight card.
 *
 * @author Vincent Tafferner
 * @author Manu Neumayer
 * @author Jessica Gerlach
 * @author Verena Sadtler
 */
public class TurnRight extends server.game.Card {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger(MessageDistributer.class.getName());

    public TurnRight() {
        cardName = "TurnRight";
    }

    /**
     * This will make the robot turn 90 degrees to the right.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap, Map<String, Robot> robotMap, Map<String, Antenna> antennaMap) {
        logger.info(ANSI_GREEN + "ACTIVATING CARD 'TURNRIGHT' ...");

        String lineOfSight = player.getPlayerRobot().getLineOfSight();
        Robot robot = player.getPlayerRobot();

        switch (lineOfSight) {
            case ("up"):
                robot.setLineOfSight("right");
                logger.info(ANSI_GREEN + "NEW LINE OF SIGHT: "+ robot.getLineOfSight() + ANSI_RESET);
                break;
            case ("right"):
                robot.setLineOfSight("down");
                logger.info(ANSI_GREEN + "NEW LINE OF SIGHT: "+ robot.getLineOfSight() + ANSI_RESET);
                break;
            case ("down"):
                robot.setLineOfSight("left");
                logger.info(ANSI_GREEN + "NEW LINE OF SIGHT: "+ robot.getLineOfSight() + ANSI_RESET);
                break;
            case ("left"):
                robot.setLineOfSight("up");
                logger.info(ANSI_GREEN + "NEW LINE OF SIGHT: "+ robot.getLineOfSight() + ANSI_RESET);
                break;
            default:
                System.out.println("There is a Problem with the lineOfSight variable.");
        }

    }

}
