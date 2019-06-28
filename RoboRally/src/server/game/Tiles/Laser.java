package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

import java.util.ArrayList;

public class Laser extends Tile {
    @Expose
    private String type;
    @Expose
    private ArrayList<String> orientations;
    @Expose
    private Integer count; //amount of lasers in a laser field

    public Laser (Integer count, String orientation){
        this.type = Parameter.LASER_NAME;

        this.orientations = new ArrayList<>();
        this.orientations.add(orientation);

        this.count = count;
    }

    @Override
    public String getTileType() {
        return type;
    }

    @Override
    public Integer getCount() {
        return count;
    }

    public ArrayList<String> getOrientation() {
        return this.orientations;
    }
}
