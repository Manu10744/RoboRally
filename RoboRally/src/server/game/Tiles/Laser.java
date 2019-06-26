package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

import java.util.ArrayList;

public class Laser extends Tile {
    @Expose
    private String type;
    @Expose
    private Integer count; //amount of lasers in a laser field
    @Expose
    ArrayList<String> orientations;

    public Laser (Integer count, String orientation){
        super();
        this.type = Parameter.LASER_NAME;
        this.count = count;
        this.orientations.add(orientation);
    }

    @Override
    public String getTileType() {
        return type;
    }

    @Override
    public Integer getCount() {
        return count;
    }

    @Override
    public ArrayList<String> getOrientations(){
        return this.orientations;
    }
}
