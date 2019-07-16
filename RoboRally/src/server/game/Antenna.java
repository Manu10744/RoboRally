package server.game;

import javafx.geometry.Point2D;
import static java.lang.Math.abs;

public class Antenna {

    /**
     * This method calculates the distance between two objects on the map and returns its' distance by a number which
     * represents the fields the first object would have to move vertically and horizontally to get to the second
     * objects position.
     *
     * By this method it is possible to determine the distance between antenna and robot.
     *
     * The Point2D class defines a point representing a location in (x,y) coordinate space.
     * @param start (which is represented by the coordinates of the antenna)
     * @param end (which is represented by the coordinates of the chosen robot)
     * @return double value which stands for fields between the objects
     *
     * @author Ivan Dovecar
     */
    public double distanceCalculatorByFields (Point2D start, Point2D end){
        Point2D startEndDifference = start.subtract(end);
        double distanceFields = abs(startEndDifference.getX()) + abs(startEndDifference.getY());
        return distanceFields;
    }

    /**
     * This method calculates the distance between two objects on the map and returns its' distance by a number which
     * represents the vector length between the objects. Its' purpose is to help the AI to determine the shortest way
     * to the checkpoint.
     *
     * By this method it is possible to determine the distance between robot and checkpoint
     *
     * The Point2D class defines a point representing a location in (x,y) coordinate space.
     * @param start (which is represented by the coordinates of the chosen robot)
     * @param end (which is represented by the coordinates of the actual checkpoint)
     * @return double value which stands for the vector length between the objects
     *
     * @author Ivan Dovecar
     */
    public double distanceCalculatorByVectorLength (Point2D start, Point2D end){
        double distanceVector = start.distance(end);
        return distanceVector;
    }
}
