package server.game.ProgrammingCards;

import server.game.Card;
import server.game.Robot;

import java.util.ArrayList;

/**
 * This class implements the MoveIII card. <br>
 * It does this by calling the MoveI method three times. <br>
 * This is important because otherwise the Robot might be able to jump over walls.
 *
 * @author Vincent Tafferner
 */
public class MoveIII extends server.game.Card {

    public MoveIII() {
        cardName = "MoveIII";
    }

    /**
     * This will move the robot three tiles in the direction he is facing. <br>
     * It is important, that he moves one tile at a time so he cant jump over holes or walls.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Robot robot, ArrayList<Card> register) {
        String lineOfSight = robot.getLineOfSight();

        int xPosition = robot.getxPosition();
        int yPosition = robot.getyPosition();
    }

}
