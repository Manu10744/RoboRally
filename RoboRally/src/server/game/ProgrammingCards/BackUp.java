package server.game.ProgrammingCards;

import server.game.Player;
import server.game.Robot;
import utils.json.MessageDistributer;

import java.util.logging.Logger;

/**
 * This class implements the Backup card.
 *
 * @author Vincent Tafferner
 */
public class BackUp extends server.game.Card {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger(MessageDistributer.class.getName());

    public BackUp() {
        cardName = "BackUp";
    }

    /**
     * This move will make the robot take one step back. <br>
     * It is important, that the robot still faces the same direction.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player) {
        logger.info(ANSI_GREEN + "ACTIVATING CARD 'BACKUP' ..." + ANSI_RESET);

        String lineOfSight = player.getPlayerRobot().getLineOfSight();

        int xPosition = player.getPlayerRobot().getxPosition();
        int yPosition = player.getPlayerRobot().getyPosition();
        Robot robot = player.getPlayerRobot();

        switch (lineOfSight){
            case ("up"):
                robot.setyPosition(yPosition - 1);
                break;
            case ("right"):
                robot.setxPosition(xPosition - 1);
                break;
            case ("down"):
                robot.setyPosition(yPosition + 1);
                break;
            case ("left"):
                robot.setxPosition(xPosition + 1);
                break;
            default:
                System.out.println("There was a problem with the lineOfSight variable.");
        }
    }

}
