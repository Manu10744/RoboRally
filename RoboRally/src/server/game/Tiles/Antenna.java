package server.game.Tiles;

import com.google.gson.annotations.Expose;
import javafx.geometry.Point2D;
import utils.Parameter;
import java.util.ArrayList;
import static java.lang.Math.abs;

/**
 * This class is responsible for the antenna.
 *
 * @author Ivan Dovecar
 */

public class Antenna extends Tile {
    @Expose
    private String type;
    @Expose
    private ArrayList<String> orientations;

    /**
     * This is the constructor for the default antenna that is used in any of the predefined maps. Because all values are fixed,
     * the constructor does not need any arguments
     */
    public Antenna(){
        super();
        this.orientations = new ArrayList<>();

        this.type = Parameter.ANTENNA_NAME;
        orientations = new ArrayList<>();
        orientations.add("right");
    }

    /**
     * This is the constructor for an antenna which is placed and turned deliberately - it is only relevant for custom maps;
     * The orientation can thus differ; Its place will be set via a Map constructor and cannot be chosen here
     * @param orientation
     */
    public Antenna(String orientation){
        super();
        this.orientations = new ArrayList<>();

        this.type = "CustomAntenna";
        this.orientations.add(orientation);

    }

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
     */
    public double distanceCalculatorByVectorLength (Point2D start, Point2D end){
        double distanceVector = start.distance(end);
        return distanceVector;
    }

    @Override
    public String getTileType() {
        return type;
    }

    @Override
    public ArrayList<String> getOrientations() {
        return orientations;
    }
}
