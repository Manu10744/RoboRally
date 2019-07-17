package server.game.ProgrammingCards;

import server.game.Card;
import server.game.Robot;

import java.util.ArrayList;

/**
 * This class implements the Backup card.
 *
 * @author Vincent Tafferner
 */
public class BackUp extends server.game.Card {

    public BackUp() {
        cardName = "BackUp";
    }

    /**
     * This move will make the robot take one step back. <br>
     * It is important, that the robot still faces the same direction.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Robot robot, ArrayList<Card> register) {
        String lineOfSight = robot.getLineOfSight();

        int xPosition = robot.getxPosition();
        int yPosition = robot.getyPosition();

        switch (lineOfSight){
            case ("up"):
                xPosition = xPosition +1;
                break;
            case ("right"):
                yPosition = yPosition -1;
                break;
            case ("down"):
                xPosition = xPosition -1;
                break;
            case ("left"):
                yPosition = yPosition +1;
                break;
            default:
                System.out.println("There was a problem with the lineOfSight Variable");
        }
    }


}
