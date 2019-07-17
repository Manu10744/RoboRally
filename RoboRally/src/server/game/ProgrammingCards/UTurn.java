package server.game.ProgrammingCards;

import server.game.Card;
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
    public void activateCard(Robot robot, ArrayList<Card> register) {
        String lineOfSight = robot.getLineOfSight();

        int xPosition = robot.getxPosition();
        int yPosition = robot.getyPosition();
    }

}
