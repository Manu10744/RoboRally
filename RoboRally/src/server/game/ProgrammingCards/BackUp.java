package server.game.ProgrammingCards;

import static server.game.Robot.*;

/**
 * This class implements the Backup card.
 *
 * @author Vincent Tafferner
 */
public class BackUp extends ProgrammingCard {

    public BackUp() {
        cardName = "BackUp";
    }

    /**
     * This move will make the robot take one step back. <br>
     * It is important, that the robot still faces the same direction.
     */
    public static void activateCard() {
        getLineOfSight();
        getxPosition();  // column
        getyPosition();  // row

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
