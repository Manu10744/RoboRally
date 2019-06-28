package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

public class CheckPoint extends Tile {
    @Expose
    private String type;
    @Expose
    private Integer count;

    public CheckPoint(Integer count){
            this.type = Parameter.CHECKPOINT_NAME;

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
