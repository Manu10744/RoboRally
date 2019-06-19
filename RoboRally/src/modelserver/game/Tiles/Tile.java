package modelserver.game.Tiles;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import utils.Parameter;

import java.util.ArrayList;

public class Tile {
    private String tileType;
    private Integer speed;
    private Integer count;
    private Boolean isCrossing;
    private ArrayList<String> orientations;
    private ArrayList<Integer> registers;

    public Tile() {

    }

    public Tile(String tileType, Integer speed, Integer count, Boolean isCrossing, ArrayList<String> orientation, ArrayList<Integer> registers) {
        this.tileType = tileType;
        this.speed = speed;
        this.count = count;
        this.isCrossing = isCrossing;
        this.orientations = orientation;
        this.registers = registers;
    }

    public String getTileType() {
        return tileType;
    }

    public Integer getSpeed() {
        return speed;
    }

    public Integer getCount() {
        return count;
    }

    public Boolean getCrossing() {
        return isCrossing;
    }

    public ArrayList<String> getOrientations() {
        return orientations;
    }

    public ArrayList<Integer> getRegisters() {
        return registers;
    }

    /**
     * This method returns the path to and thus the image belonging to the tile it is applied to.
     * In case there are more than one images for one tileType, the method differentiates between those different elements
     * in taking account of further tile attributes (as can be seen within the if-else-clauses).
     * Which additional tile attributes were chosen depend on the tile classes - the attributes that differ from the parent class tile
     * are the only ones that are not null in the subclasses and are hence the only viable option to differentiate between different objects of the same subclass
     *
     * @return
     * @author Mia
     */
    public static Image getTileImage(Tile tile) {
        StringBuilder path = new StringBuilder("resources/images/mapelements");
        String tileType = tile.getTileType();

        switch (tileType) {
            //normal tile
            case "Empty":
                path.append("normal1.png");
                //Antenna
            case "Antenna":
                path.append("priority-antenna.png");
                //Pit
            case "Pit":
                path.append("pit.png");
                //Start Point
            case "StartPoint":
                path.append("start-position.png");
                //Energy space -> we have actually different designs but cannot send them due to the protocol
            case "EnergySpace": path.append("energyspace-left-right.png");
                //Gears
            case "Gear": {
                ArrayList<String> gearOrientation = tile.getOrientations();
                //Red and Green Gears differ mainly in their orientation: greens turn left, reds turn right
                if ((gearOrientation.get(Parameter.FIRST_ELEMENT_POS)).equals(Parameter.ORIENTATION_LEFT)) {
                    path.append("gear-green.png");
                } else if (gearOrientation.get(Parameter.FIRST_ELEMENT_POS).equals((Parameter.ORIENTATION_RIGHT))) {
                    path.append("gear-red.png");
                }
            }
            //The six victory icons
            case "CheckPoint":
                int checkpointNumber = (int) tile.getCount();
                //there are six different checkpoint icons and its corresponding images like so:
                if (checkpointNumber == Parameter.CHECKPOINT_ONE) {path.append("victory-1.png");}
                else if (checkpointNumber == Parameter.CHECKPOINT_TWO) {path.append("victory-2.png");}
                else if (checkpointNumber == Parameter.CHECKPOINT_THREE) {path.append("victory-3.png");}
                else if (checkpointNumber == Parameter.CHECKPOINT_FOUR) {path.append("victory-4.png");}
                else if (checkpointNumber == Parameter.CHECKPOINT_FIVE) {path.append("victory-5.png");}
                else if (checkpointNumber == Parameter.CHECKPOINT_SIX) {path.append("victory-6.png");}
                    //Push Panels
            case "PushPanel":
                ArrayList<Integer> registers = tile.getRegisters();
                String orientation = tile.getOrientations().get(Parameter.FIRST_ELEMENT_POS);
                //There are either two or three registers specified
                //Two registers are filled, on our maps this is always register 2 and 4, therefor only one switch case works
               if (registers.size() == Parameter.REGISTER_TWO){
                    switch (orientation){
                        case Parameter.ORIENTATION_BOTTOM:
                            path.append("pushpanel-24-bottom.png");
                        case Parameter.ORIENTATION_TOP:
                            path.append("pushpanel-24-top.png");
                        case Parameter.ORIENTATION_LEFT:
                            path.append("pushpanel-24-left.png");
                        case Parameter.ORIENTATION_RIGHT:
                            path.append("pushpanel-24-right.png");
                    }
                //Three registers are filled, on our maps this is always register 1, 3 and 5, therefor only one switch case works
                }else if (registers.size() == Parameter.REGISTER_THREE){
                    switch (orientation){
                        case Parameter.ORIENTATION_BOTTOM:
                            path.append("pushpanel-135-bottom.png");
                        case Parameter.ORIENTATION_TOP:
                            path.append("pushpanel-135-top.png");
                        case Parameter.ORIENTATION_LEFT:
                            path.append("pushpanel-135-left.png");
                        case Parameter.ORIENTATION_RIGHT:
                            path.append("pushpanel-135-right.png");
                    }
                }
               //Walls, double and single
            case "Wall":
                ArrayList<String> wallOrientations = tile.getOrientations();
                //single walls
                if(wallOrientations.size() == Parameter.ONE_WALL){
                    switch (wallOrientations.get(Parameter.FIRST_ELEMENT_POS)) {
                        case Parameter.ORIENTATION_BOTTOM:
                            path.append("wall-one-bottom.png");
                        case Parameter.ORIENTATION_TOP:
                            path.append("wall-one-top.png");
                        case Parameter.ORIENTATION_LEFT:
                            path.append("wall-one-left.png");
                        case Parameter.ORIENTATION_RIGHT:
                            path.append("wall-one-right.png");
                    }
                    //double walls
                }else if (wallOrientations.size() == Parameter.TWO_WALLS){
                    String wallOrientation1 = wallOrientations.get(Parameter.FIRST_ELEMENT_POS);
                    String wallOrientation2 = wallOrientations.get(Parameter.SECOND_ELEMENT_POS);
                    if (wallOrientation1.equals(Parameter.ORIENTATION_TOP)){
                        if (wallOrientation2.equals(Parameter.ORIENTATION_RIGHT)){path.append("wall-two-top-right");}
                        else {path.append("wall-two-left-top.png");}
                    } else if (wallOrientation1.equals(Parameter.ORIENTATION_BOTTOM)) {
                        if(wallOrientation2.equals(Parameter.ORIENTATION_RIGHT)){path.append("wall-two-bottom-right.png");}
                        else {path.append("wall-two-bottom-left.png");}
                    }
                }
                /*Lasers - TODO: we actually only need the lasers without the beams, if we want to animate them? We also cannt differentiate between
                             the end of a laser or its beginning */
            case "Laser":
                int laserBeamNumber = tile.getCount();
                String laserOrientation = tile.getOrientations().get(Parameter.FIRST_ELEMENT_POS);
                switch (laserBeamNumber){
                    case (Parameter.LASER_ONE):
                       switch (laserOrientation) {
                           case Parameter.ORIENTATION_BOTTOM:  path.append("laser-onebeam-start-bottom.png");
                           case Parameter.ORIENTATION_TOP:     path.append("laser-onebeam-start-top.png");
                           case Parameter.ORIENTATION_LEFT:    path.append("laser-onebeam-start-left.png");
                           case Parameter.ORIENTATION_RIGHT:   path.append("laser-onebeam-start-right.png");
                       }
                    case (Parameter.LASER_TWO):
                       switch (laserOrientation) {
                           case Parameter.ORIENTATION_BOTTOM:  path.append("laser-twobeam-start-bottom.png");
                           case Parameter.ORIENTATION_TOP:     path.append("laser-twobeam-start-top.png");
                           case Parameter.ORIENTATION_LEFT:    path.append("laser-twobeam-start-left.png");
                           case Parameter.ORIENTATION_RIGHT:   path.append("laser-twobeam-start-right.png");
                        }
                    case (Parameter.LASER_THREE):
                        switch (laserOrientation) {
                           case Parameter.ORIENTATION_BOTTOM:  path.append("laser-threebeam-onefield-bottom.png");
                           case Parameter.ORIENTATION_TOP:     path.append("laser-threebeam-onefield-top.png");
                           case Parameter.ORIENTATION_LEFT:    path.append("laser-threebeam-onefield-left.png");
                           case Parameter.ORIENTATION_RIGHT:   path.append("laser-threebeam-onefield-right.png");
                        }
            }
            //Belts (blue and green conveyor belts insofar as they are straight
            case "Belt":
                int beltSpeed = tile.getSpeed();
                String beltOrientation = tile.getOrientations().get(Parameter.FIRST_ELEMENT_POS);
                //The Conveyor belts differ mainly in speed: blue speed = 2, green speed = 1
                if (beltSpeed == (int) Parameter.BLUE_BELT_SPEED){
                    switch (beltOrientation){
                        case Parameter.ORIENTATION_BOTTOM:  path.append("bluecvb-straight-bottom.png");
                        case Parameter.ORIENTATION_TOP:     path.append("bluecvb-straight-top.png");
                        case Parameter.ORIENTATION_LEFT:    path.append("bluecvb-straight-left.png");
                        case Parameter.ORIENTATION_RIGHT:   path.append("bluecvb-straight-bottom.png");
                    }

                }else{
                    switch (beltOrientation){
                        case Parameter.ORIENTATION_BOTTOM:  path.append("greencvb-straight-bottom.png");
                        case Parameter.ORIENTATION_TOP:     path.append("greencvb-straight-top.png");
                        case Parameter.ORIENTATION_LEFT:    path.append("greencvb-straight-left.png");
                        case Parameter.ORIENTATION_RIGHT:   path.append("greencvb-straight-right.png");
                    }

                }

            //Belts (blue and green insofar as they are curved
            case "RotatingBelt":
                int speed = tile.getSpeed();
                boolean isCrossing = tile.getCrossing();
                String curvedBeltOrientationBefore = tile.getOrientations().get(Parameter.FIRST_ELEMENT_POS);
                String curvedBeltOrientationAfter = tile.getOrientations().get(Parameter.SECOND_ELEMENT_POS);
                //The Conveyor belts differ mainly in speed: blue speed = 2, green speed = 1

                //Blue conveyor belt
                if (speed == (int) Parameter.BLUE_BELT_SPEED){
                    //There are two types of blue conveyor belts: one that is just curved and one that is curving into a crossing
                    //Crossings
                    if (isCrossing) {
                        switch(curvedBeltOrientationBefore){
                            case Parameter.ORIENTATION_BOTTOM:
                                //from each direction the belt could either go top or down starting from either left or right (4*2) hence the if else
                                if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {path.append("bluecvb-curved-bottom-right.png");}
                                else {path.append("bluecvb-curved-bottom-left.png");}
                            case Parameter.ORIENTATION_TOP:
                                if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {path.append("bluecvb-curved-top-right.png");}
                                else {path.append("bluecvb-curved-top-left.png");}
                            case Parameter.ORIENTATION_LEFT:
                                if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {path.append("bluecvb-curved-left-top.png");}
                                else {path.append("bluecvb-curved-left-bottom.png");}
                            case Parameter.ORIENTATION_RIGHT:
                                if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {path.append("bluecvb-curved-right-top.png");}
                                else {path.append("bluecvb-curved-right-bottom.png");}
                        }
                        }
                    //just curved ones
                    else {
                    switch (curvedBeltOrientationBefore) {
                        case Parameter.ORIENTATION_BOTTOM:
                            //Here the same holds true as in the cases above: the belt can either go left or right starting from top or bottom and so on...
                            if(curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {path.append("bluecvb-curved-sharp-bottom-right.png");}
                            else {path.append("bluecvb-curved-sharp-bottom-left.png");}
                        case Parameter.ORIENTATION_TOP:
                            if(curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {path.append("bluecvb-curved-sharp-top-right.png");}
                            else {path.append("bluecvb-curved-sharp-top-left.png");}
                        case Parameter.ORIENTATION_LEFT:
                            if(curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_BOTTOM)) {path.append("bluecvb-curved-sharp-left-bottom.png");}
                            else {path.append("bluecvb-curved-sharp-left-top.png");}
                        case Parameter.ORIENTATION_RIGHT:
                            if(curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {path.append("bluecvb-curved-sharp-right-top.png");}
                            else {path.append("bluecvb-curved-sharp-left-bottom.png");}
                    }
                }
                    //Green conveyor belts
                }else if (speed == (int) Parameter.GREEN_BELT_SPEED){
                    if (isCrossing) {
                        switch(curvedBeltOrientationBefore){
                            case Parameter.ORIENTATION_BOTTOM:
                                if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {path.append("greencvb-curved-bottom-right.png");}
                                else {path.append("greencvb-curved-bottom-left.png");}
                            case Parameter.ORIENTATION_TOP:
                                if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {path.append("greencvb-curved-top-right.png");}
                                else {path.append("greencvb-curved-top-left.png");}
                            case Parameter.ORIENTATION_LEFT:
                                if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {path.append("greencvb-curved-left-top.png");}
                                else {path.append("greencvb-curved-left-bottom.png");}
                            case Parameter.ORIENTATION_RIGHT:
                                if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {path.append("greencvb-curved-right-top.png");}
                                else {path.append("greencvb-curved-right-bottom.png");}
                        }
                    }
                    //just green curved ones
                    else {
                        switch (curvedBeltOrientationBefore) {
                            case Parameter.ORIENTATION_BOTTOM:
                                if(curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {path.append("greencvb-curved-sharp-bottom-right.png");}
                                else {path.append("greencvb-curved-sharp-bottom-left.png");}
                            case Parameter.ORIENTATION_TOP:
                                if(curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {path.append("greencvb-curved-sharp-top-right.png");}
                                else {path.append("greencvb-curved-sharp-top-left.png");}
                            case Parameter.ORIENTATION_LEFT:
                                if(curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_BOTTOM)) {path.append("greencvb-curved-sharp-left-bottom.png");}
                                else {path.append("greencvb-curved-sharp-left-top.png");}
                            case Parameter.ORIENTATION_RIGHT:
                                if(curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {path.append("greencvb-curved-sharp-right-top.png");}
                                else {path.append("greencvb-curved-sharp-left-bottom.png");}
                        }
                    }

                }
                    break;
                    //for empty tiles in case the board si not rectangular
                    default:
                        return null;
                }
                return new Image(path.toString());
    }


}
