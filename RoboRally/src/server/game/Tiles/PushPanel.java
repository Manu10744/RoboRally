package server.game.Tiles;

import com.google.gson.annotations.Expose;
import utils.Parameter;

import java.util.ArrayList;

public class PushPanel extends Tile {
    @Expose
    private String type;
    @Expose
    private ArrayList<String> orientations;
    @Expose
    private ArrayList<Integer> registers;

    /**
     * This is the constructor for tile fields called PushPanel. The constructor parameter is for one the orientation and for two the number of registers there are. For each parameter that could change, in this case three kind,
     * there is a declaration in the Parameter class which should be used in initialising said tiles to circumvent errors. From the number of registers during which a pushpanel is activated derive the registers which activate them:
     * for two registers there are only pushpanels activating at register 2 and 4, for three they activate at 1, 3, 5. This is taken from the maps that came with RoboRally.
     * @param orientation
     * @param numOfRegisters
     * @author Mia
     */
    public PushPanel(String orientation, int numOfRegisters){
        this.type = Parameter.PUSHPANEL_NAME;

        this.orientations = new ArrayList<>();
        this.orientations.add(orientation);

        this.registers = new ArrayList<>();
        if(numOfRegisters == Parameter.PUSHPANEL_REG_NUM_TWO){
        this.registers.add(Parameter.REGISTER_TWO);
        this.registers.add(Parameter.REGISTER_FOUR);
        }else if (numOfRegisters == Parameter.REGISTER_THREE){
            this.registers.add(Parameter.REGISTER_ONE);
            this.registers.add(Parameter.REGISTER_THREE);
            this.registers.add(Parameter.REGISTER_FIVE);
        }


    }


    @Override
    public String getTileType() {
        return type;
    }

    @Override
    public ArrayList<String> getOrientations() {
        return orientations;
    }

    @Override
    public ArrayList<Integer> getRegisters() {
        return registers;
    }
}
