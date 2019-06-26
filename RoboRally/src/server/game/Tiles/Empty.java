package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

/**
 * This class's sole purpose is to construct normal map tiles which only have a name
 * @author  Mia
 */
public class Empty extends Tile {
    @Expose
    private String type;

    public Empty (){
    super();
    this.type = Parameter.EMPTY_NAME;
    }

    @Override
    public String getTileType() {
        return type;
    }
}
