package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;
import java.util.ArrayList;

public class Gear extends Tile {
    @Expose
      private ArrayList<String> orientations;
    @Expose
      private String tileType;

    public Gear (String color){
        super();
        this.tileType = Parameter.GEAR_NAME;
        if (color.equals(Parameter.RED_GEAR)) {this.orientations.add(Parameter.ORIENTATION_LEFT);}
        else if (color.equals(Parameter.GREEN_GEAR)) {this.orientations.add(Parameter.ORIENTATION_RIGHT);}
    }

    @Override
    public ArrayList<String> getOrientations() {
        return orientations;
    }

    @Override
    public String getTileType() {
        return tileType;
    }
}
