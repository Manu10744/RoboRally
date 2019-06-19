package modelserver.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

public class Pit extends Tile {
    @Expose
    private String tileType;

    public Pit(){
        super();
        this.tileType = Parameter.PIT_NAME;
    }

    @Override
    public String getTileType(){
        return this.tileType;
    }
}
