package server.game.ProgrammingCards;

/**
 * This class implements the MoveII card. <br>
 * It does this by calling the MoveI method twice. <br>
 * This is important because otherwise the Robot might be able to jump over walls.
 *
 * @author Vincent Tafferner
 */
public class MoveII extends ProgrammingCard {

    public MoveII() {
        cardName = "MoveII";
    }

    /**
     * This will move the robot two tiles in the direction he is facing. <br>
     * It is important, that he moves one tile at a time so he cant jump over holes or walls.
     */
    @Override
    public void activateCard() {
        //TODO
    }
}
