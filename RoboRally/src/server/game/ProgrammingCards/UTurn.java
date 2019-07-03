package server.game.ProgrammingCards;

import static server.game.ProgrammingCards.TurnLeft.*;

/**
 * This class implements the UTurn card.
 *
 * @author Vincent Tafferner
 */
public class UTurn extends ProgrammingCard {

    public UTurn() {
        cardName = "UTurn";
    }

    /**
     * This will make the robot perform a 180.
     */
    public static void activateCard() {
        TurnLeft.activateCard();
        TurnLeft.activateCard();
    }
}
