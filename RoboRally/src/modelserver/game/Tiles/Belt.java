package modelserver.game.Tiles;

import utils.Parameter;

public class Belt extends Tile {
    private String tileType;
    private Integer speed;

    public Belt (String color){
        super();
        this.tileType = "Belt";
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
