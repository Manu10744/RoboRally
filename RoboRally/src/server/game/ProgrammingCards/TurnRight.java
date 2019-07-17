package server.game.ProgrammingCards;

import server.game.Card;
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
    public void activateCard(Robot robot, ArrayList<Card> register) {
        String lineOfSight = robot.getLineOfSight();

        int xPosition = robot.getxPosition();
        int yPosition = robot.getyPosition();
    }

}
