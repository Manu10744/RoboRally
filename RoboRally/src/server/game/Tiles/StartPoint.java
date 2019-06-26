package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

public class StartPoint extends Tile {
    @Expose
    private String type;

    public StartPoint(){
        super();
        this.type = Parameter.STARTPOINT_NAME;
    }

    @Override
    public String getTileType(){
        return this.type;
    }
}
