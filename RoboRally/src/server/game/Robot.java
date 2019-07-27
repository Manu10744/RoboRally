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
    private String name;

    public Robot(){

    }

    public Robot(String name, Image robotImage, String lineOfSight, int xPosition, int yPosition) {
        this.name = name;
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

    public void setLineOfSight(String lineOfSight) {
        this.lineOfSight = lineOfSight;
    }

    /**
     * Get the x position of the robot
     * @return xPosition The xPosition of the robot
     */
    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int x) {
        this.xPosition = x;
    }
    /**
     * Get the y position of the robot
     * @return yPosition The yPosition of the robot
     */
    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int y) {
        this.yPosition = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Image getRobotImage() {
        return robotImage;
    }

    public void setRobotImage(Image robotImage) {
        this.robotImage = robotImage;
    }
}
