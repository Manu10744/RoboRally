package src.modelserver.game;

/**
 * This class implements the robots which are the playing figures in the game.
 *
 * @author Vincent Tafferner
 */
public class Robot {

    private String lineOfSight;
    private int xPosition;
    private int yPosition;

    /**
     * Get the line of sight of the robot
     * @return lineOfSight The lineOfSight of th robot
     */
    public String getLineOfSight() {
        return this.lineOfSight;
    }

    /**
     * Get the x position of the robot
     * @return xPosition The xPosition of the robot
     */
    public int getxPosition() {
        return this.xPosition;
    }

    /**
     * Get the y position of the robot
     * @return yPosition The yPosition of the robot
     */
    public int getyPosition() {
        return this.yPosition;
    }

    /**
     * This method is responsible for moving the robot on the map
     */
    public void move() {

    }

    /**
     * This method is responsible for rotating the robot on the map
     */
    public void rotate() {

    }
}
