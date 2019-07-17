package server.game.ProgrammingCards;

import server.game.Card;
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
    public void activateCard(Robot robot, ArrayList<Card> register) {
        String lineOfSight = robot.getLineOfSight();

        int xPosition = robot.getxPosition();
        int yPosition = robot.getyPosition();

        switch (lineOfSight) {
            case ("up"):
                lineOfSight = "left";
                break;
            case ("right"):
                lineOfSight = "up";
                break;
            case ("down"):
                lineOfSight = "right";
                break;
            case ("left"):
                lineOfSight = "down";
                break;
            default:
                System.out.println("There is a Problem with the lineOfSight variable.");
        }

    }

}
