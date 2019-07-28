package server.game.ProgrammingCards;

import server.game.Player;
import server.game.Robot;
import server.game.Tiles.*;
import utils.json.MessageDistributer;

import java.util.Map;
import java.util.logging.Logger;

/**
 * This class implements the MoveII card. <br>
 * It does this by calling the MoveI method twice. <br>
 * This is important because otherwise the Robot might be able to jump over walls.
 *
 * @author Vincent Tafferner
 * @author Verena Sadtler
 * @author Manu Neumayer
 * @author Jessica Gerlach
 */
public class MoveII extends server.game.Card {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger(MessageDistributer.class.getName());

    public MoveII() {
        cardName = "MoveII";
    }

    /**
     * This will move the robot two tiles in the direction he is facing.
     */

    @Override
    public void activateCard(Player player, Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap, Map<String, Robot> robotMap, Map<String, Antenna> antennaMap, Map<String, Belt> beltMap, Map<String, RotatingBelt> rotatingBeltMap) {
        logger.info(ANSI_GREEN + "ACTIVATING CARD 'MOVE II' ...");

        String lineOfSight = player.getPlayerRobot().getLineOfSight();

        int xPosition = player.getPlayerRobot().getxPosition();
        int yPosition = player.getPlayerRobot().getyPosition();

        String oldPos = xPosition + "-" + yPosition;
        String newPos;
        Robot robot = player.getPlayerRobot();

        switch (lineOfSight) {
            case ("up"):
                for (int i = 0; i < 2; i++) {
                    //update new position for algorithm to check
                    newPos = xPosition + "-" + (yPosition + 1);
                    if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap, rotatingBeltMap, beltMap, oldPos, newPos, "down", "up")) {
                        // Update robots y-positionbeltMap
                        robot.setyPosition(yPosition + 1);
                        // Update the local y-Position for the algorithm
                        yPosition = yPosition + 1;
                        // Move was valid, update old position for algorithm for next iteration
                        oldPos = xPosition + "-" + yPosition;
                    }
                }
                logger.info(ANSI_GREEN + "NEW ROBOT POSITION: ( " + robot.getxPosition() + " | " +
                        robot.getyPosition() + " )" + ANSI_RESET);
                break;
            case ("right"):
                for (int i = 0; i < 2; i++) {
                    newPos = (xPosition + 1) + "-" + yPosition;
                    if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap,rotatingBeltMap, beltMap, oldPos, newPos, "left", "right")) {
                        robot.setxPosition(xPosition + 1);
                        xPosition = xPosition + 1;
                        oldPos = xPosition + "-" + yPosition;
                    }
                }
                logger.info(ANSI_GREEN + "NEW ROBOT POSITION: ( " + robot.getxPosition() + " | " +
                        robot.getyPosition() + " )" + ANSI_RESET);
                break;
            case ("down"):
                for (int i = 0; i < 2; i++) {
                    newPos = xPosition + "-" + (yPosition - 1);
                    if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap, rotatingBeltMap, beltMap, oldPos, newPos, "up", "down")) {
                        robot.setyPosition(yPosition - 1);
                        yPosition = yPosition - 1;
                        oldPos = xPosition + "-" + yPosition;
                    }
                }
                logger.info(ANSI_GREEN + "NEW ROBOT POSITION: ( " + robot.getxPosition() + " | " +
                        robot.getyPosition() + " )" + ANSI_RESET);
                break;
            case ("left"):
                for (int i = 0; i < 2; i++) {
                    newPos = (xPosition - 1) + "-" + yPosition;
                    if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap, rotatingBeltMap, beltMap, oldPos, newPos, "right", "left")) {
                        robot.setxPosition(xPosition - 1);
                        xPosition = xPosition - 1;
                        oldPos = xPosition + "-" + yPosition;

                    }
                }
                logger.info(ANSI_GREEN + "NEW ROBOT POSITION: ( " + robot.getxPosition() + " | " +
                        robot.getyPosition() + " )" + ANSI_RESET);
                break;
            default:
                System.out.println("There was a problem with the lineOfSight variable.");
        }

        //update robot in robotMap
        String currentPos = robot.getxPosition() + "-" + robot.getyPosition();
        robotMap.remove(currentPos);
        robotMap.put(currentPos, robot);
        logger.info(ANSI_GREEN + "NEW ROBOT POSITION IN ROBOTMAP: ( " + robotMap.get(currentPos).getxPosition() + " | " +
                robotMap.get(currentPos).getyPosition() + " )" + ANSI_RESET);
    }

    @Override
    public boolean isDamageCard() {
        return false;
    }
}
