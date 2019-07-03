package server.game.ProgrammingCards;

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
    public void activateCard() {
        /*
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

         */
    }

}
