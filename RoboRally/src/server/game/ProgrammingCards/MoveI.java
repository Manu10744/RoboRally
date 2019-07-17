package server.game.ProgrammingCards;

import server.game.Card;
import server.game.Robot;

import java.util.ArrayList;

/**
 * This class implements the MoveI card.
 *
 * @author Vincent Tafferner
 */
public class MoveI extends Card {

    public MoveI() {
        cardName = "MoveI";
    }

    /**
     * This will move the robot one tile in the direction he is facing.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Robot robot, ArrayList<Card> register) {
        String lineOfSight = robot.getLineOfSight();

        int xPosition = robot.getxPosition();
        int yPosition = robot.getyPosition();

        switch (lineOfSight){
            case ("up"):
                xPosition = xPosition -1;
                break;
            case ("right"):
                yPosition = yPosition +1;
                break;
            case ("down"):
                xPosition = xPosition +1;
                break;
            case ("left"):
                yPosition = yPosition -1;
                break;
            default:
                System.out.println("There was a problem with the lineOfSight variable.");
        }
    }

}
