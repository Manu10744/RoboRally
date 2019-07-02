package server.game.ProgrammingCards;

import server.game.Card;

/**
 * This class defines a programming card.
 *
 * @author Vincent Tafferner
 */
public class ProgrammingCard extends Card {

    /**
     * This method will be overwritten in the specific programming card classes. <br>
     * Most often it will move the Robot in different ways, always one step at a time.
     */
    public static void activateCard() {
        //Please overload
    }
}
