package server.game.Maps;

import server.game.Tiles.Empty;
import server.game.Tiles.Tile;
import utils.Parameter;

import java.util.ArrayList;

public class DizzyHighway extends Map {
    private String mapName;
    private MapBody dizzyHighway;


    public DizzyHighway() {
        this.mapName = Parameter.DIZZY_HIGHWAY;
        dizzyHighway = new MapBody(); //initialises empty MapBody
        ArrayList<Tile> dizzyTiles = new ArrayList<>();

        for (int i = 0; i <= Parameter.DIZZY_HIGHWAY_WIDTH; i++){
            for (int j = 0; j <= Parameter.DIZZY_HIGHWAY_HEIGHT; j++){
            dizzyTiles.add(new Empty());
        }}
        dizzyHighway.setTileList(dizzyTiles);
    }

    @Override
    public String getMapName(){
        return this.mapName;
    }

    @Override
    public Map getMap(){
        return this;
    }

    @Override
    public MapBody getGameMap(){
        return this.gameMap;
    }
}


