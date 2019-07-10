package server.game.Tiles;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;

public class RestartPoint extends Tile {
    @Expose
    String type;

    public RestartPoint(){
        this.type = "RestartPoint";
    }

    @Override
    public String getTileType() {
        return this.type;
    }
}
