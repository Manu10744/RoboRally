package modelserver.game.Maps;
import utils.Parameter;

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
     MapBody gameMap;


    public Map (String mapName){
        Map map;

        if (mapName.equals(Parameter.DIZZY_HIGHWAY)){
            map = new DizzyHighway();

            mapName = map.getMapName();
            gameMap = map.getGameMap();

        }else{
            map = new Map();
        }
    }

    public Map() {
    }

    public MapBody getGameMap(){
        return this.gameMap;
    }

    public  Map getMap(){
        return this;
    }


    public String getMapName() {
        return mapName;
    }


}
