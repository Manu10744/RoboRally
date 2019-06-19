package modelserver.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

public class StartPoint extends Tile {
    @Expose
    private String tileType;

    public StartPoint(){
        super();
        this.tileType = Parameter.STARTPOINT_NAME;
    }

    @Override
    public String getTileType(){
        return this.tileType;
    }
}
