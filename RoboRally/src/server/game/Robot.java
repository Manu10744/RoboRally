package server.game;

import static utils.Parameter.*;

/**
 * This class implements the robots which are the playing figures in the game.
 *
 * @author Vincent Tafferner
 */
public class Robot {

    private String lineOfSight;
    private int xPosition;
    private int yPosition;

    public Robot() {
        lineOfSight = ROBOT_START_LINEOFSIGHT;
        xPosition = ROBOT_START_XPOSITION;
        yPosition = ROBOT_START_YPOSITION;
    }
    /**
     * Get the line of sight of the robot
     * @return lineOfSight The lineOfSight of th robot
     */
    public String getLineOfSight() {
        return lineOfSight;
    }

    /**
     * Get the x position of the robot
     * @return xPosition The xPosition of the robot
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Get the y position of the robot
     * @return yPosition The yPosition of the robot
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * This method is responsible for moving the robot on the map
     */
    public void move() {
        //TODO not needed
    }

    /**
     * This method is responsible for rotating the robot on the map
     */
    public void rotate() {
        //TODO not needed
    }
}
