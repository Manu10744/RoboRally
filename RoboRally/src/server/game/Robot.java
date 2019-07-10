package server.game;

import javafx.scene.image.Image;

import static utils.Parameter.*;

/**
 * This class implements the robots which are the playing figures in the game.
 *
 * @author Vincent Tafferner
 */
public class Robot {

    private Image robotImage;
    private String lineOfSight;
    private int xPosition;
    private int yPosition;

    public Robot(Image robotImage, String lineOfSight, int xPosition, int yPosition) {
        this.robotImage = robotImage;
        this.lineOfSight = lineOfSight;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
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
        //TODO delete if not needed in the future
    }

    /**
     * This method is responsible for rotating the robot on the map
     */
    public void rotate() {
        //TODO not needed
    }

    public Image getRobotImage() {
        return robotImage;
    }

    public void setRobotImage(Image robotImage) {
        this.robotImage = robotImage;
    }
}
