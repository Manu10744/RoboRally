package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

public class Laser extends Tile {
    @Expose
    private String type;
    @Expose
    private String orientations;
    @Expose
    private Integer count; //amount of lasers in a laser field

    public Laser (Integer count, String orientation){
        super();
        this.orientations = orientation;

        this.type = Parameter.LASER_NAME;
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

    public String getOrientation() {
        return this.orientations;
    }
}
