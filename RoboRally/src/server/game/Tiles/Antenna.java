package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

import java.util.ArrayList;

public class Antenna extends Tile {
    @Expose
    private String tileType;
    @Expose
    private ArrayList<String> orientations;

    /**
     * This is the constructor for the default antenna that is used in any of the predefined maps. Because all values are fixed,
     * the constructor does not need anyarguments
     */
    public Antenna(){
        super();
        this.tileType = Parameter.ANTENNA_NAME;
        orientations.add("right");
    }

    /**
     * This is the constructor for an antenna which is placed and turned deliberately - it is only relevant for custom maps;
     * The orientation can thus differ; Its place will be set via a Map constructor and cannot be chosen here
     * @param orientation
     */
    public Antenna(String orientation){
        super();
        this.tileType = "CustomAntenna";
        this.orientations.add(orientation);

    }

    @Override
    public String getTileType() {
        return tileType;
    }

    @Override
    public ArrayList<String> getOrientations() {
        return orientations;
    }
}
