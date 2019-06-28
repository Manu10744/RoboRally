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
     * This is the constructor for tile fields called PushPanel. The constructor parameters are the values that PushPanel need minus its type (name)
     * as the name will be given automatically. For each parameter that could change, in this case three kind,
     * there is a declaration in the Parameter class which should be used in initialising said tiles to circumvent errors.
     * @param orientation
     * @param register1
     * @param register2
     * @author Mia
     */
    public PushPanel(String orientation, Integer register1, Integer register2){
        super();
        this.orientations = new ArrayList<>();
        this.registers = new ArrayList<>();

        this.type = Parameter.PUSHPANEL_NAME;
        this.orientations.add(orientation);
        this.registers.add(register1);
        this.registers.add(register2);
    }

    public PushPanel(String orientation, Integer register1, Integer register2, Integer register3){
        super();
        this.orientations = new ArrayList<>();
        this.registers = new ArrayList<>();

        this.type = Parameter.PUSHPANEL_NAME;
        this.orientations.add(orientation);
        this.registers.add(register1);
        this.registers.add(register2);
        this.registers.add(register3);
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
