package server.game.ProgrammingCards;

import server.game.Card;
import server.game.Player;
import server.game.Robot;

import java.util.ArrayList;

/**
 * This class implements the UTurn card.
 *
 * @author Vincent Tafferner
 */
public class UTurn extends server.game.Card {

    public UTurn() {
        cardName = "UTurn";
    }

    /**
     * This will make the robot perform a 180.
     * //TODO remove if not needed in final version.
     */
    @Override
    public void activateCard(Player player, ArrayList<Card> register) {
        String lineOfSight = player.getPlayerRobot().getLineOfSight();
        Robot robot = player.getPlayerRobot();

        switch (lineOfSight) {
            case ("up"):
                robot.setLineOfSight("down");
                break;
            case ("right"):
                robot.setLineOfSight("left");
                break;
            case ("down"):
                robot.setLineOfSight("up");
                break;
            case ("left"):
                robot.setLineOfSight("right");
                break;
            default:
                System.out.println("There is a Problem with the lineOfSight variable.");
        }

    }
}
