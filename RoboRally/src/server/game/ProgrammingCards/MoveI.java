package server.game.ProgrammingCards;

/**
 * This class implements the MoveI card.
 *
 * @author Vincent Tafferner
 */
public class MoveI extends server.game.Card {

    public MoveI() {
        cardName = "MoveI";
    }

    /**
     * This will move the robot one tile in the direction he is facing.
     * //TODO remove if not needed in final version.
     */
    /*
    public void activateCard() {
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
    */
}
