package server.game.ProgrammingCards;

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
    /*
    public void activateCard() {

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
    */
}
