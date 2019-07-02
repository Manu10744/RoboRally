package server.game.ProgrammingCards;

import static server.game.Robot.*;

/**
 * This class implements the TurnLeft card.
 *
 * @author Vincent Tafferner
 */
public class TurnLeft extends ProgrammingCard {

    public TurnLeft() {
        cardName = "TurnLeft";
    }

    /**
     * This will make the robot turn 90 degrees to the left.
     */
    public static void activateCard() {
        getLineOfSight();

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
