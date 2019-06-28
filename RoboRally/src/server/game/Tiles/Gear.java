package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

import java.util.ArrayList;

public class Gear extends Tile {
    @Expose
      private ArrayList<String> orientations;
    @Expose
      private String type;

    public Gear (String color, String orientation){
        this.type = Parameter.GEAR_NAME;

        this.orientations = new ArrayList<>();
        this.orientations.add(orientation);

        if (color.equals(Parameter.RED_GEAR)) {this.orientations.add(Parameter.ORIENTATION_LEFT);}
        else if (color.equals(Parameter.GREEN_GEAR)) {this.orientations.add(Parameter.ORIENTATION_RIGHT);}
    }

    @Override
    public ArrayList<String> getOrientations() {
        return orientations;
    }

    @Override
    public String getTileType() {
        return type;
    }
}
