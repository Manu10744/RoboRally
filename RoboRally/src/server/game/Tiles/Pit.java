package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

public class Pit extends Tile {
    @Expose
    private String type;

    public Pit(){
        this.type = Parameter.PIT_NAME;
    }

    @Override
    public String getTileType(){
        return this.type;
    }
}
