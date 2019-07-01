package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

import java.util.ArrayList;

public class Belt extends Tile {
    @Expose
    private String type;
    @Expose
    private Integer speed;

    @Expose
    private ArrayList<String> orientations;

    public Belt (String color, String orientation){
        this.type = Parameter.BELT_NAME;

        this.orientations = new ArrayList<>();
        this.orientations.add(orientation);
        if(color.equals(Parameter.GREEN_BELT)){this.speed = Parameter.GREEN_BELT_SPEED;}
        else if(color.equals(Parameter.BLUE_BELT)){this.speed = Parameter.BLUE_BELT_SPEED;}
    }

    @Override
    public String getTileType() {
        return type;
    }

    @Override
    public Integer getSpeed() {
        return speed;
    }

    @Override
    public ArrayList<String> getOrientations() {
        return orientations;
    }
}
