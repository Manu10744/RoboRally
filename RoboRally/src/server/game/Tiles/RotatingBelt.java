package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

import java.util.ArrayList;

/**
 * If isCrossing is true, the field is a crossing and not changing the orientation of the robot on it
 * If isCrossing is false, the Rotating Belt is curved and hence it changes the orientation of the robot on it according to its orientation
 */
//Todo javadoc all classes

public class RotatingBelt extends Tile {
    @Expose
    private String type;
    @Expose
    private Integer speed;
    @Expose
    private Boolean isCrossing;
    @Expose
    private ArrayList<String> orientations;

    public RotatingBelt(Integer speed, String orientationBeforeTurn, String orientationAfterTurn, Boolean isCrossing) {
        super();
        this.orientations = new ArrayList<>();

        this.type = Parameter.ROTATINGBELT_NAME;
        this.speed = speed;
        this.orientations.add(orientationBeforeTurn);
        this.orientations.add(orientationAfterTurn);
        this.isCrossing = isCrossing;
    }

    @Override
    public String getTileType(){
        return this.type;
    }

    @Override
    public ArrayList<String> getOrientations() {
        return this.orientations;
    }

    @Override
    public Boolean getCrossing(){
        return this.isCrossing;
    }

    @Override
    public Integer getSpeed() {
        return speed;
    }
}
