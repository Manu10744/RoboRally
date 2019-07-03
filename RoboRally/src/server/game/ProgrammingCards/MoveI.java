package server.game.ProgrammingCards;

import static server.game.Robot.*;

/**
 * This class implements the MoveI card.
 *
 * @author Vincent Tafferner
 */
public class MoveI extends ProgrammingCard {

    public MoveI() {
        cardName = "MoveI";
    }

    /**
     * This will move the robot one tile in the direction he is facing. <br>
     */
    public static void activateCard() {

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
