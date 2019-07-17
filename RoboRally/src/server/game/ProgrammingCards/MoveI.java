package server.game.ProgrammingCards;

import server.game.Player;
import server.game.Robot;
import utils.json.MessageDistributer;

import java.util.logging.Logger;

/**
 * This class implements the MoveI card.
 *
 * @author Vincent Tafferner
 */
public class MoveI extends server.game.Card {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger(MessageDistributer.class.getName());

    public MoveI() {
        cardName = "MoveI";
    }

    /**
     * This will move the robot one tile in the direction he is facing.
     * //TODO remove if not needed in final version.
     */
    @Override
    public void activateCard(Player player) {
        logger.info(ANSI_GREEN + "ACTIVATING CARD 'MOVE I' ..." + ANSI_RESET);

        String lineOfSight = player.getPlayerRobot().getLineOfSight();

        int xPosition = player.getPlayerRobot().getxPosition();
        int yPosition = player.getPlayerRobot().getyPosition();
        Robot robot = player.getPlayerRobot();

        switch (lineOfSight){
            case ("up"):
                robot.setyPosition(yPosition + 1);
                break;
            case ("right"):
                robot.setxPosition(xPosition + 1);
                break;
            case ("down"):
                robot.setyPosition(yPosition - 1);
                break;
            case ("left"):
                robot.setxPosition(xPosition - 1);
                break;
            default:
                System.out.println("There was a problem with the lineOfSight variable.");
        }
    }

}
