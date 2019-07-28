package server.game.Tiles;

import com.google.gson.annotations.Expose;
import javafx.geometry.Point2D;
import server.*;
import utils.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private Server server;

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
     * This class contains a constructor getter and setter to handle robots by distance from antenna
     */
    public class DistanceAndName {
        private int playerID;
        private String name;
        private double distance;

        // Safe playerID, name and calculated distance in this object
        public DistanceAndName(int playerID, String name, double distance) {
            this.playerID = playerID;
            this.name = name;
            this.distance = distance;
        }

        public int getPlayerID() { return playerID; }
        public void setPlayerID() { this.playerID = playerID; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public double getDistance() { return distance; }
        public void setDistance() { this.distance = distance; }
    }

    // List which contains information to determine next active player
    ArrayList<DistanceAndName> nextToPlay = new ArrayList<>();

    // This method determines next player to play by call and returns next players' playerID
    public int determineNextPlayer() {
        server = new Server();
        ArrayList<Server.ClientWrapper> connectedClients = server.getConnectedClients();

        // TODO this dummy coordinates have to be replaced by real ones dependet on chosen map!
        Point2D antennaPoint = new Point2D(0.0, 6.0);

        // Fill list nextToPlay with objects which contain name and distance as values
        int i = 0;
        while (i < connectedClients.size()) {
            // Point is generated with robot x and y position
            Point2D robotPoint = new Point2D(
                    connectedClients.get(i).getPlayer().getPlayerRobot().getxPosition(),
                    connectedClients.get(i).getPlayer().getPlayerRobot().getyPosition());

            // determine playerID
            int playerID = connectedClients.get(i).getPlayerID();

            // determine name
            String name = connectedClients.get(i).getName();

            // determine distance to antenna
            double distance = distanceCalculatorByFields(antennaPoint, robotPoint);

            // safe object with determine info in nextToPlay
            nextToPlay.add(new DistanceAndName(playerID, name, distance));

            i++;
        }

        // sort DistanceAndName by distance
        Collections.sort(nextToPlay, Comparator.comparingDouble(DistanceAndName::getDistance));

        // nextToPlay is now sorted
        // if distance from first object is unique, first player in list is currentPlayer
        // if distance from first object occurs multiple times, make list of these players and select the one who is
        // hit by antenna beam first (line of sight "beam" which rotates clockwise)

        int k = 0;
        int nextPlayerID = nextToPlay.get(k).getPlayerID();
        double nextPlayerDistance = nextToPlay.get(k).getDistance();
        k++;

        if (nextPlayerDistance != nextToPlay.get(k).getDistance()) {
            return nextPlayerID;
        } else {
            //TODO place here code for selection by antenna beam. By now if there more than one robot to choose from
            // it simply takes the first in list to avoid the game to stuck
            return nextPlayerID;
        }
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
