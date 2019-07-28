package server.game.ProgrammingCards;

import server.game.Player;
import server.game.Robot;
import server.game.Tiles.Antenna;
import server.game.Tiles.Pit;
import server.game.Tiles.PushPanel;
import server.game.Tiles.Wall;
import utils.json.MessageDistributer;

import java.util.Map;
import java.util.logging.Logger;

/**
 * This class implements the MoveIII card. <br>
 * It does this by calling the MoveI method three times. <br>
 * This is important because otherwise the Robot might be able to jump over walls.
 *
 * @author Vincent Tafferner
 * @author Verena Sadtler
 * @author Manu Neumayer
 * @author Jessica Gerlach
 */
public class MoveIII extends server.game.Card {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger(MessageDistributer.class.getName());

    public MoveIII() {
        cardName = "MoveIII";
    }

    /**
     * This will move the robot three tiles in the direction he is facing.
     */

    @Override
    public void activateCard(Player player, Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap, Map<String, Robot> robotMap, Map<String, Antenna> antennaMap) {
        logger.info(ANSI_GREEN + "ACTIVATING CARD 'MOVE III' ...");

        String lineOfSight = player.getPlayerRobot().getLineOfSight();

        int xPosition = player.getPlayerRobot().getxPosition();
        int yPosition = player.getPlayerRobot().getyPosition();

        String oldPos = xPosition + "-" + yPosition;
        String newPos;
        Robot robot = player.getPlayerRobot();

        switch (lineOfSight){
            case ("up"):
                for(int i = 0; i < 3; i++) {
                    //update new position for algorithm to check
                    newPos = xPosition + "-" + (yPosition + 1);
                    if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap, oldPos, newPos, "down", "up")) {
                        // Update robots y-position
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
                for(int i = 0; i < 3; i++) {
                    newPos = (xPosition + 1) + "-" + yPosition;
                    if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap, oldPos, newPos, "left", "right")) {
                        robot.setxPosition(xPosition + 1);
                        xPosition = xPosition + 1;
                        oldPos = xPosition + "-" + yPosition;

                    }
                }
                logger.info(ANSI_GREEN + "NEW ROBOT POSITION: ( " + robot.getxPosition() + " | " +
                        robot.getyPosition() + " )" + ANSI_RESET);
                break;
            case ("down"):
                for(int i = 0; i < 3; i++) {
                    newPos = xPosition + "-" + (yPosition - 1);
                    if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap, oldPos, newPos, "up", "down")) {
                        robot.setyPosition(yPosition - 1);
                        yPosition = yPosition - 1;
                        oldPos = xPosition + "-" + yPosition;

                    }
                }
                logger.info(ANSI_GREEN + "NEW ROBOT POSITION: ( " + robot.getxPosition() + " | " +
                        robot.getyPosition() + " )" + ANSI_RESET);
                break;
            case ("left"):
                for(int i = 0; i < 3; i++) {
                    newPos = (xPosition - 1) + "-" + yPosition;
                    if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap, oldPos, newPos, "right", "left")) {
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

        //Update robot in robotMap
        String currentPos = robot.getxPosition() + "-" + robot.getyPosition();
        robotMap.remove(currentPos);
        robotMap.put(currentPos, robot);
        logger.info(ANSI_GREEN + "NEW ROBOT POSITION IN ROBOTMAP: ( " + robotMap.get(currentPos).getxPosition() + " | " +
                robotMap.get(currentPos).getyPosition() + " )" + ANSI_RESET);
    }
}
