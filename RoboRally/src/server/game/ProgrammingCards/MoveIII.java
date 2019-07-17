package server.game.ProgrammingCards;

import server.game.Card;
import server.game.Player;
import server.game.Robot;
import utils.json.MessageDistributer;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class implements the MoveIII card. <br>
 * It does this by calling the MoveI method three times. <br>
 * This is important because otherwise the Robot might be able to jump over walls.
 *
 * @author Vincent Tafferner
 */
public class MoveIII extends server.game.Card {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger(MessageDistributer.class.getName());

    public MoveIII() {
        cardName = "MoveIII";
    }

    /**
     * This will move the robot three tiles in the direction he is facing. <br>
     * It is important, that he moves one tile at a time so he cant jump over holes or walls.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, ArrayList<Card> register) {
        logger.info(ANSI_GREEN + "ACTIVATING CARD 'MOVE III' ...");

        String lineOfSight = player.getPlayerRobot().getLineOfSight();

        int xPosition = player.getPlayerRobot().getxPosition();
        int yPosition = player.getPlayerRobot().getyPosition();

        Robot robot = player.getPlayerRobot();

        switch (lineOfSight){
            case ("up"):
                robot.setyPosition(yPosition +3);
                break;
            case ("right"):
                robot.setxPosition(xPosition +3);
                break;
            case ("down"):
                robot.setyPosition(yPosition -3);
                break;
            case ("left"):
                robot.setxPosition(xPosition - 3);
                break;
            default:
                System.out.println("There was a problem with the lineOfSight variable.");
        }

    }
}
