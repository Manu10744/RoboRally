package server.game;

/**
 * This class implements the robots which are the playing figures in the game.
 *
 * @author Vincent Tafferner
 */
public class Robot {

    public static String lineOfSight;
    public static int xPosition;
    public static int yPosition;

    /**
     * Sets the direction in which the Robot is looking (up,right,down,left)
     * @return sets the lineOfSight variable to up
     */
    public static String setLineOfSightUp() {
        lineOfSight = "up";
        return lineOfSight;
    }

    /**
     * Sets the direction in which the Robot is looking (up,right,down,left)
     * @return sets the lineOfSight variable to up
     */
    public static String setLineOfSightRight() {
        lineOfSight = "right";
        return lineOfSight;
    }

    /**
     * Sets the direction in which the Robot is looking (up,right,down,left)
     * @return sets the lineOfSight variable to up
     */
    public static String setLineOfSightDown() {
        lineOfSight = "down";
        return lineOfSight;
    }

    /**
     * Sets the direction in which the Robot is looking (up,right,down,left)
     * @return sets the lineOfSight variable to up
     */
    public static String setLineOfSightLeft() {
        lineOfSight = "left";
        return lineOfSight;
    }

    /**
     * Get the line of sight of the robot
     * @return lineOfSight The lineOfSight of th robot
     */
    public static String getLineOfSight() {
        return lineOfSight;
    }

    /**
     * Get the x position of the robot
     * @return xPosition The xPosition of the robot
     */
    public static int getxPosition() {
        return xPosition;
    }

    /**
     * Get the y position of the robot
     * @return yPosition The yPosition of the robot
     */
    public static int getyPosition() {
        return yPosition;
    }


    // This is most likely not needed, but i'll leave it in for now - Vincent
    // It should be implemented in each Card
    /**
     * This method is responsible for moving the robot on the map
     */
    /*
    public void move() {

    }
     */

    /**
     * This method is responsible for rotating the robot on the map
     */
    /*
    public void rotate() {

    }
     */


}
