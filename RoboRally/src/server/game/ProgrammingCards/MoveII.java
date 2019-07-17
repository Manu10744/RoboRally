package server.game.ProgrammingCards;

import server.game.Card;
import server.game.Player;
import server.game.Robot;

import java.util.ArrayList;

/**
 * This class implements the MoveII card. <br>
 * It does this by calling the MoveI method twice. <br>
 * This is important because otherwise the Robot might be able to jump over walls.
 *
 * @author Vincent Tafferner
 */
public class MoveII extends server.game.Card {

    public MoveII() {
        cardName = "MoveII";
    }

    /**
     * This will move the robot two tiles in the direction he is facing. <br>
     * It is important, that he moves one tile at a time so he cant jump over holes or walls.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, ArrayList<Card> register) {
        String lineOfSight = player.getPlayerRobot().getLineOfSight();

        int xPosition = player.getPlayerRobot().getxPosition();
        int yPosition = player.getPlayerRobot().getyPosition();
        Robot robot = player.getPlayerRobot();

        switch (lineOfSight){
            case ("up"):
                robot.setyPosition(yPosition + 2);;
                break;
            case ("right"):
                robot.setxPosition(xPosition + 2);
                break;
            case ("down"):
                robot.setyPosition(yPosition -2);
                break;
            case ("left"):
                robot.setxPosition(xPosition - 2);
                break;
            default:
                System.out.println("There was a problem with the lineOfSight variable.");
    }

}
}
