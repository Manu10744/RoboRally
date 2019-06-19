package modelserver.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

public class Belt extends Tile {
    @Expose
    private String tileType;
    @Expose
    private Integer speed;

    public Belt (String color){
        super();
        this.tileType = Parameter.BELT_NAME;
        if(color.equals(Parameter.GREEN_BELT)){this.speed = Parameter.GREEN_BELT_SPEED;}
        else if(color.equals(Parameter.BLUE_BELT)){this.speed = Parameter.BLUE_BELT_SPEED;}
    }

    @Override
    public String getTileType() {
        return tileType;
    }

    @Override
    public Integer getSpeed() {
        return speed;
    }
}
