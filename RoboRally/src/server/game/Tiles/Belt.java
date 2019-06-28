package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

public class Belt extends Tile {
    @Expose
    private String type;
    @Expose
    private String orientation;
    @Expose
    private Integer speed;

    public Belt (String color, String orientation){
        super();
        this.type = Parameter.BELT_NAME;
        this.orientation = orientation;
        if (color.equals(Parameter.GREEN_BELT)) { this.speed = Parameter.GREEN_BELT_SPEED; }
        else if (color.equals(Parameter.BLUE_BELT)){this.speed = Parameter.BLUE_BELT_SPEED; }
    }

    @Override
    public String getTileType() {
        return type;
    }

    @Override
    public Integer getSpeed() {
        return speed;
    }

    public String getOrientation() {
        return orientation;
    }
}
