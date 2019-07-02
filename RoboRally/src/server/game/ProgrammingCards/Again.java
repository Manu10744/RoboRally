package server.game.ProgrammingCards;

/**
 * This class implements the Again card.
 *
 * @author Vincent Tafferner
 */
public class Again extends ProgrammingCard {

    public Again() {
        cardName = "Again";
    }

    /**
     * This move will repeat the move that was performed in the previous register. <br>
     * If there was no previous move the robot shall do nothing.
     */
    public static void activateCard() {
        //TODO get card from previous register and perfort that again
    }
}
