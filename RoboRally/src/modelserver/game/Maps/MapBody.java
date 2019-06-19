package modelserver.game.Maps;

import com.google.gson.annotations.Expose;
import modelserver.game.Tiles.Tile;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapBody {
    @Expose


    private ArrayList<Tile> tileList;
    private ArrayList<ArrayList<Tile>> doubledNestedArray;
    private ArrayList<ArrayList<ArrayList<Tile>>> mapBody;



    public MapBody(){
        this.mapBody = new ArrayList<ArrayList<ArrayList<Tile>>>();
        this.doubledNestedArray = new ArrayList<ArrayList<Tile>>();
        this.tileList = new ArrayList<>();

        this.doubledNestedArray.add(this.tileList);
        mapBody.add(this.doubledNestedArray);
    }

    public MapBody (ArrayList<ArrayList<ArrayList<Tile>>> mapBody){
        this.mapBody = mapBody;
    }

    public ArrayList<ArrayList<ArrayList<Tile>>> getMapBody() {
        return mapBody;
    }

    public ArrayList<ArrayList<Tile>> getXMapPosFromMapBody(int x) {
        return this.mapBody.get(x);
    }



    public ArrayList<Tile> getTileArrayFromMapBody(int posX, int posY){

       ArrayList<Tile> tileList  = new ArrayList<>();
       try{

           if(this.mapBody.isEmpty()) {
               doubledNestedArray.add(tileList);
               mapBody.add(doubledNestedArray);
               tileList = mapBody.get(posX).get(posY);


           }else if(!this.mapBody.get(posX).get(posX).isEmpty()){
               tileList = this.mapBody.get(posX).get(posY);;
           }

       }catch (NullPointerException e) {
           e.printStackTrace();
       }
        return tileList;
    }

    public ArrayList<ArrayList<Tile>> getDoubledNestedArray() {
        return doubledNestedArray;
    }

    public ArrayList<Tile> getTileList() {
        return tileList;
    }

    public void setMapBody(ArrayList<ArrayList<ArrayList<Tile>>> mapBody) {
        this.mapBody = mapBody;
    }

    public void setDoubledNestedArray(ArrayList<ArrayList<Tile>> doubledNestedArray) {
        this.doubledNestedArray = doubledNestedArray;
    }

    public void setTileList(ArrayList<Tile> tileList) {
        this.tileList = tileList;
    }

    public Tile getTileFromMapBody (int x, int y, int tilePos){
        return getTileArrayFromMapBody(x, y).get(tilePos);
    }
}
