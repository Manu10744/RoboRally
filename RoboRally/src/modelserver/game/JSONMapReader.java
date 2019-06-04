package modelserver.game;


import java.util.ArrayList;


/**
 * This class reads a JSON File which contains the Map that is built in 'Tiled'. It saves the map into an ArrayList
 * (JSON-Arrays work like Java-ArrayLists) which will be used in the class Map to calculate distances etc.
 *
 * @author Mia
 */
// TODO remove this comment when class is working - until then disabled to enable testing other classes without interference
public class JSONMapReader {

    private ArrayList map;

 /*
    /**
     First a FileReader is initialised which gets the name of JSON-File containing the map.
     As FileReader throws an exception it is initalised within a try-catch block.
     In a second step the content of the file is parsed to a Java-Object.
     The Java-Object then is cast to an JSON-Array whose content we get via an Iterator
     (Iterator because an ArrayList (=JSONArray) has no fixed length)

     @author Mia

     */

    /**
     * This is the getter for the ArrayList Map. The map is called upon by the @link MapController Class.
     * @return value is the map we have converted from the JSON-Tiled-Document
     */
    /* TODO remove this comment when class is working - until then disabled to enable testing other classes without interference
    public ArrayList<Object> getMap(){
        return this.map;
    }

    FileReader file;

    {

        try {
            file = new FileReader("DizzyHighway.json");
            Object obj = Parser.parse(new FileReader("map.json")); //TODO: make the starting map and load it here


            JSONArray abArray = (JSONArray) obj.get("data"); //
            for (Object o : abArray) {
                map.add(o);
            }
            ;

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch(FileNotFoundException e)
        {e.printStackTrace();}

        catch(IOException e)
        {e.printStackTrace();}

        catch(ParseException e)
        {e.printStackTrace();}

        catch(Exception e)
        {e.printStackTrace();}
    }
  */

}

