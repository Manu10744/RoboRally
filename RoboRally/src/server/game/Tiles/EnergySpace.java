package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

public class EnergySpace extends Tile {
    @Expose
    private String type;
    @Expose
    private Integer count;

    public EnergySpace (Integer count){
        super();
        this.type = Parameter.ENERGYSPACE_NAME;
        this.count = count;
    }

    @Override
    public String getTileType() {
        return type;
    }

    @Override
    public Integer getCount() {
        return count;
    }
}
