package server.game.Tiles;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;

public class Reboot extends Tile {
    @Expose
    String type;

    public Reboot(){
        this.type = "Reboot";
    }

    @Override
    public String getTileType() {
        return this.type;
    }
}
