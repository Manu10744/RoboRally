package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

import java.util.ArrayList;

public class Antenna extends Tile {
    @Expose
    private String type;
    @Expose
    private ArrayList<String> orientations;

    /**
     * This is the constructor for the default antenna that is used in any of the predefined maps. Because all values are fixed,
     * the constructor does not need any arguments
     */
    public Antenna(){
        super();
        this.orientations = new ArrayList<>();

        this.type = Parameter.ANTENNA_NAME;
        orientations = new ArrayList<>();
        orientations.add("right");
    }

    /**
     * This is the constructor for an antenna which is placed and turned deliberately - it is only relevant for custom maps;
     * The orientation can thus differ; Its place will be set via a Map constructor and cannot be chosen here
     * @param orientation
     */
    public Antenna(String orientation){
        super();
        this.orientations = new ArrayList<>();

        this.type = "CustomAntenna";
        this.orientations.add(orientation);

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
