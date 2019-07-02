package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Wall extends Tile {
    @Expose
    private String type;
    @Expose
    private ArrayList<String> orientations;

    public Wall (String wallOrientation){
      this.type = Parameter.WALL_NAME;

      this.orientations = new ArrayList<>();
      this.orientations.add(wallOrientation);

    }

    public Wall (String wallOrientation1, String wallOrientation2){
      this.type = "Wall";

      this.orientations = new ArrayList<>();
      this.orientations.add(wallOrientation1);
      this.orientations.add(wallOrientation2);
    }

    @Override
    public String getTileType() {
        return type;
    }

    @Override
    public ArrayList<String> getOrientations() {
        return orientations;
    }
}
