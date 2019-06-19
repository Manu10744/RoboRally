package modelserver.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

public class CheckPoint extends Tile {
    @Expose
    private String tileType;
    @Expose
    private Integer count;

    public CheckPoint(Integer count){
            super();
            this.tileType = Parameter.CHECKPOINT_NAME;
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
