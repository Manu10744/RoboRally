package modelserver.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

public class EnergySpace extends Tile {
    @Expose
    private String tileType;
    @Expose
    private Integer count;

    public EnergySpace (Integer count){
        super();
        this.tileType = Parameter.ENERGYSPACE_NAME;
        this.count = count;
    }

    @Override
    public String getTileType() {
        return tileType;
    }

    @Override
    public Integer getCount() {
        return count;
    }
}
