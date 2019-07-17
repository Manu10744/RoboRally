package server.game.ProgrammingCards;

import server.game.Card;
import server.game.Player;
import server.game.Robot;

import java.util.ArrayList;

/**
 * This class implements the TurnRight card.
 *
 * @author Vincent Tafferner
 */
public class TurnRight extends server.game.Card {

    public TurnRight() {
        cardName = "TurnRight";
    }

    /**
     * This will make the robot turn 90 degrees to the right.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, ArrayList<Card> register) {
        String lineOfSight = player.getPlayerRobot().getLineOfSight();
        Robot robot = player.getPlayerRobot();

        switch (lineOfSight) {
            case ("up"):
                robot.setLineOfSight("right");
                break;
            case ("right"):
                robot.setLineOfSight("down");
                break;
            case ("down"):
                robot.setLineOfSight("left");
                break;
            case ("left"):
                robot.setLineOfSight("up");
                break;
            default:
                System.out.println("There is a Problem with the lineOfSight variable.");
        }

    }

}
