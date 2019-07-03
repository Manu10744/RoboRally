package server.game.ProgrammingCards;

/**
 * This class implements the MoveIII card. <br>
 * It does this by calling the MoveI method three times. <br>
 * This is important because otherwise the Robot might be able to jump over walls.
 *
 * @author Vincent Tafferner
 */
public class MoveIII extends ProgrammingCard {

    public MoveIII() {
        cardName = "MoveIII";
    }

    /**
     * This will move the robot three tiles in the direction he is facing. <br>
     * It is important, that he moves one tile at a time so he cant jump over holes or walls.
     */
    public static void activateCard() {
        MoveI.activateCard();
        MoveI.activateCard();
        MoveI.activateCard();
    }
}
