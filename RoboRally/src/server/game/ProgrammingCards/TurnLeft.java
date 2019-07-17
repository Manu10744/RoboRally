package server.game.ProgrammingCards;

import server.game.Card;
import server.game.Player;
import server.game.Robot;

import java.util.ArrayList;

/**
 * This class implements the TurnLeft card.
 *
 * @author Vincent Tafferner
 */
public class TurnLeft extends server.game.Card {

    public TurnLeft() {
        cardName = "TurnLeft";
    }

    /**
     * This will make the robot turn 90 degrees to the left.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, ArrayList<Card> register) {
    String lineOfSight = player.getPlayerRobot().getLineOfSight();
    Robot robot = player.getPlayerRobot();

        switch (lineOfSight) {
            case ("up"):
                robot.setLineOfSight("left");
                break;
            case ("right"):
                robot.setLineOfSight("up");
                break;
            case ("down"):
                robot.setLineOfSight("right");
                break;
            case ("left"):
                robot.setLineOfSight("down");
                break;
            default:
                System.out.println("There is a Problem with the lineOfSight variable.");
        }

    }

}
