package modelserver.game.Maps;
import modelserver.game.Tiles.Tile;


import java.util.ArrayList;

/**
 * There can be created different maps in Roborally.
 * The map is made of Tiles.
 *
 * @author Vincent Tafferner
 */
public class Map {

    String mapName;
    /** first ArrayList for x Variable
     * second for y variable
     * third for nested fields (more than one funvtion, e.g. wall with laser)
     * @author Mia
     */
    ArrayList <Tile> mapTiles;
    ArrayList<ArrayList<ArrayList<Tile>>> map;


    public Map (String mapName,  ArrayList<ArrayList<ArrayList<Tile>>> map){
        this.mapName =  mapName;
        this.map = map;
    }

    public Map() {
    }

    public  ArrayList<ArrayList<ArrayList<Tile>>> getMap(){
        return this.map;
    }


    public String getMapName() {
        return mapName;
    }


}
