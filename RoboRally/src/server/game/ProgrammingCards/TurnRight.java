package server.game.ProgrammingCards;

import static server.game.ProgrammingCards.TurnLeft.*;

/**
 * This class implements the TurnRight card.
 *
 * @author Vincent Tafferner
 */
public class TurnRight extends ProgrammingCard {

    public TurnRight() {
        cardName = "TurnRight";
    }

    /**
     * This will make the robot turn 90 degrees to the right.
     */
    public static void activateCard() {
        TurnLeft.activateCard();
        TurnLeft.activateCard();
        TurnLeft.activateCard();
    }
}
