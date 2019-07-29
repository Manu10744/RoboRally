package server.game.ProgrammingCards;

import server.game.Player;
import server.game.Robot;
import server.game.Tiles.*;
import utils.json.MessageDistributer;

import java.util.Map;
import java.util.logging.Logger;

/**
 * This class implements the Backup card.
 *
 * @author Vincent Tafferner
 * @author Manu Neumayer
 * @author Jessica Gerlach
 * @author Verena Sadtler
 */
public class BackUp extends server.game.Card {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger(MessageDistributer.class.getName());

    public BackUp() {
        cardName = "BackUp";
    }

    /**
     * This move will make the robot take one step back. <br>
     * It is important, that the robot still faces the same direction.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap, Map<String, Robot> robotMap, Map<String, Antenna> antennaMap, Map<String, Belt> beltMap, Map<String, RotatingBelt> rotatingBeltMap) {
        logger.info(ANSI_GREEN + "ACTIVATING CARD 'BACKUP' ..." + ANSI_RESET);

        String lineOfSight = player.getPlayerRobot().getLineOfSight();

        int xPosition = player.getPlayerRobot().getxPosition();
        int yPosition = player.getPlayerRobot().getyPosition();

        String oldPos = xPosition + "-" + yPosition;
        String newPos;
        Robot robot = player.getPlayerRobot();

        switch (lineOfSight) {
            case ("up"):
                newPos = xPosition + "-" + (yPosition - 1);
                if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap, rotatingBeltMap, beltMap, oldPos, newPos, "up", "down")) {
                    robot.setyPosition(yPosition - 1);

                    Robot robotInFront = robotMap.get(robot.getxPosition() + "-" + robot.getyPosition());
                    //test if you move robot other one is also moved
                    String currentPos = robot.getxPosition() + "-" + robot.getyPosition();

                    if (robotInFront != null) {
                        //update other robot robot in robotMap
                        robotMap.get(newPos).setyPosition(yPosition+2);
                        robotMap.put(xPosition + "-" + (yPosition + 2), robotMap.get(currentPos));
                        robotMap.remove(currentPos); //remove current robot

                        logger.info(ANSI_GREEN + "NEW OTHER ROBOT OF  IN ROBOTMAP: ( " + robotMap.get(xPosition + "-" + (yPosition + 2)).getxPosition() + " | " +
                                robotMap.get(xPosition + "-" + (yPosition + 2)).getyPosition() + " )" + ANSI_RESET);
                    }
                    //update robot in robotMap
                    robotMap.remove(oldPos);
                    robotMap.put(currentPos, robot);
                    logger.info(ANSI_GREEN + "NEW ROBOT POSITION IN ROBOTMAP: ( " + robotMap.get(currentPos).getxPosition() + " | " +
                            robotMap.get(currentPos).getyPosition() + " )" + ANSI_RESET);
                }
                break;
            case ("right"):
                newPos = (xPosition - 1) + "-" + yPosition;
                if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap, rotatingBeltMap, beltMap, oldPos, newPos, "right", "left")) {
                    robot.setxPosition(xPosition - 1);

                    Robot robotInFront = robotMap.get(robot.getxPosition() + "-" + robot.getyPosition());
                    //test if you move robot other one is also moved
                    String currentPos = robot.getxPosition() + "-" + robot.getyPosition();

                    if (robotInFront != null) {
                        //update other robot robot in robotMap
                        robotMap.get(newPos).setxPosition(xPosition+2);
                        robotMap.put(xPosition + 2 + "-" + yPosition, robotMap.get(currentPos));
                        robotMap.remove(currentPos); //remove current robot

                        logger.info(ANSI_GREEN + "NEW OTHER ROBOT IN ROBOTMAP: ( " + robotMap.get(xPosition + 2 + "-" + yPosition).getxPosition() + " | " +
                                robotMap.get(xPosition + 2 + "-" + yPosition).getyPosition() + " )" + ANSI_RESET);
                    }
                    //update robot in robotMap
                    robotMap.remove(oldPos);
                    robotMap.put(currentPos, robot);
                    logger.info(ANSI_GREEN + "NEW ROBOT POSITION IN ROBOTMAP: ( " + robotMap.get(currentPos).getxPosition() + " | " +
                            robotMap.get(currentPos).getyPosition() + " )" + ANSI_RESET);
                }
                break;
            case ("down"):
                newPos = xPosition + "-" + (yPosition + 1);
                if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap,rotatingBeltMap , beltMap, oldPos, newPos, "down", "up")) {
                    robot.setyPosition(yPosition + 1);

                    Robot robotInFront = robotMap.get(robot.getxPosition() + "-" + robot.getyPosition());
                    //test if you move robot other one is also moved
                    String currentPos = robot.getxPosition() + "-" + robot.getyPosition();

                    if (robotInFront != null) {
                        //update other robot robot in robotMap
                        robotMap.get(newPos).setyPosition(yPosition-2);
                        robotMap.put(xPosition + "-" + (yPosition - 2), robotMap.get(currentPos));
                        robotMap.remove(currentPos); //remove current robot

                        logger.info(ANSI_GREEN + "NEW OTHER ROBOT IN ROBOTMAP: ( " + robotMap.get(xPosition + "-" + (yPosition - 2)).getxPosition() + " | " +
                                robotMap.get(xPosition + "-" + (yPosition - 2)).getyPosition() + " )" + ANSI_RESET);
                    }
                    //update robot in robotMap
                    robotMap.remove(oldPos);
                    robotMap.put(currentPos, robot);
                    logger.info(ANSI_GREEN + "NEW ROBOT POSITION IN ROBOTMAP: ( " + robotMap.get(currentPos).getxPosition() + " | " +
                            robotMap.get(currentPos).getyPosition() + " )" + ANSI_RESET);
                }
                break;
            case ("left"):
                newPos = (xPosition + 1) + "-" + yPosition;
                if (this.isValidMove(pitMap, wallMap, pushPanelMap, robotMap, antennaMap, rotatingBeltMap, beltMap, oldPos, newPos, "left", "right")) {
                    robot.setxPosition(xPosition + 1);
                    Robot robotInFront = robotMap.get(robot.getxPosition() + "-" + robot.getyPosition());
                    //test if you move robot other one is also moved
                    String currentPos = robot.getxPosition() + "-" + robot.getyPosition();

                    if (robotInFront != null) {
                        //update other robot robot in robotMap
                        robotMap.get(newPos).setxPosition(xPosition-2);
                        robotMap.put(xPosition - 2 + "-" + yPosition, robotMap.get(currentPos));
                        robotMap.remove(currentPos); //remove current robot

                        logger.info(ANSI_GREEN + "NEW OTHER ROBOT IN ROBOTMAP: ( " + robotMap.get(xPosition - 2 + "-" + yPosition).getxPosition() + " | " +
                                robotMap.get(xPosition - 1 + "-" + yPosition).getyPosition() + " )" + ANSI_RESET);
                    }
                    //update robot in robotMap
                    robotMap.remove(oldPos);
                    robotMap.put(currentPos, robot);
                    logger.info(ANSI_GREEN + "NEW ROBOT POSITION IN ROBOTMAP: ( " + robotMap.get(currentPos).getxPosition() + " | " +
                            robotMap.get(currentPos).getyPosition() + " )" + ANSI_RESET);
                }
                break;
            default:
                System.out.println("There was a problem with the lineOfSight variable.");
        }

    }

    @Override
    public boolean isDamageCard() {
        return false;
    }

}
