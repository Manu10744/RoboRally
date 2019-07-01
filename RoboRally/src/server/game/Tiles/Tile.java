package server.game.Tiles;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.Parameter;
import viewmodels.ChatController;
import viewmodels.ChatController;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Tile {
    private String type;
    private Integer speed;
    private Integer count;
    private Boolean isCrossing;
    private ArrayList<String> orientations;
    private ArrayList<Integer> registers;

    private static final Logger logger = Logger.getLogger(ChatController.class.getName());


    public Tile() {

    }

    public Tile(String tileType, Integer speed, Integer count, Boolean isCrossing, ArrayList<String> orientation, ArrayList<Integer> registers) {
        this.type = tileType;
        this.speed = speed;
        this.count = count;
        this.isCrossing = isCrossing;
        this.orientations = orientation;
        this.registers = registers;
    }

    public String getTileType() {
        return type;
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
    public Image getTileImage() {
        StringBuilder path = new StringBuilder("resources/images/mapelements/");
        String type = this.getTileType();
        Image image;

        switch (type) {
            //normal tile
            case "Empty": {
                path.append(("normal1.png"));
                break;
            }
            case "Antenna": {
                path.append("priority-antenna.png");
                break;
            }
            //Pit
            case "Pit": {
                path.append("pit.png");
                //Start Point
                break;
            }
            case "StartPoint": {
                path.append("start-position.png");
                //Energy space -> we have actually different designs but cannot send them due to the protocol
                break;
            }
            case "EnergySpace": {
                path.append("energyspace-left-right.png");
                break;
            }
            //Gears
            case "Gear": {
                String gearOrientation = this.getOrientations().get(Parameter.FIRST_ELEMENT_POS);
                //Red and Green Gears differ mainly in their orientation: greens turn left, reds turn right
                if (gearOrientation.equals(Parameter.ORIENTATION_LEFT)) {
                    path.append("gear-red.png");
                    break;
                } else if (gearOrientation.equals((Parameter.ORIENTATION_RIGHT))) {
                    path.append("gear-green.png");
                    break;
                }
                break;
            }
            //The six victory icons
            case "CheckPoint": {
                int checkpointNumber = (int) this.getCount();
                //there are six different checkpoint icons and its corresponding images like so:
                if (checkpointNumber == Parameter.CHECKPOINT_ONE) {
                    path.append("victory-1.png");
                } else if (checkpointNumber == Parameter.CHECKPOINT_TWO) {
                    path.append("victory-2.png");
                } else if (checkpointNumber == Parameter.CHECKPOINT_THREE) {
                    path.append("victory-3.png");
                } else if (checkpointNumber == Parameter.CHECKPOINT_FOUR) {
                    path.append("victory-4.png");
                } else if (checkpointNumber == Parameter.CHECKPOINT_FIVE) {
                    path.append("victory-5.png");
                } else if (checkpointNumber == Parameter.CHECKPOINT_SIX) {
                    path.append("victory-6.png");
                }
                break;
            }
            //Push Panels
            case "PushPanel": {
                ArrayList<Integer> registers = this.getRegisters();
                String orientation = this.getOrientations().get(Parameter.FIRST_ELEMENT_POS);
                //There are either two or three registers specified
                //Two registers are filled, on our maps this is always register 2 and 4, therefor only one switch case works
                if (registers.size() == Parameter.REGISTER_TWO) {
                    switch (orientation) {
                        case Parameter.ORIENTATION_BOTTOM: {
                            path.append("pushpanel-24-bottom.png");
                            break;
                        }
                        case Parameter.ORIENTATION_TOP: {
                            path.append("pushpanel-24-top.png");
                            break;
                        }
                        case Parameter.ORIENTATION_LEFT: {
                            path.append("pushpanel-24-left.png");
                            break;
                        }
                        case Parameter.ORIENTATION_RIGHT: {
                            path.append("pushpanel-24-right.png");
                            break;
                        }
                    }
                    //Three registers are filled, on our maps this is always register 1, 3 and 5, therefor only one switch case works
                } else if (registers.size() == Parameter.REGISTER_THREE) {
                    switch (orientation) {
                        case Parameter.ORIENTATION_BOTTOM: {
                            path.append("pushpanel-135-bottom.png");
                            break;
                        }
                        case Parameter.ORIENTATION_TOP: {
                            path.append("pushpanel-135-top.png");
                            break;
                        }
                        case Parameter.ORIENTATION_LEFT: {
                            path.append("pushpanel-135-left.png");
                            break;
                        }
                        case Parameter.ORIENTATION_RIGHT: {
                            path.append("pushpanel-135-right.png");
                            break;
                        }
                    }
                }
                break;
            }
            //Walls, double and single
            case "Wall": {
                ArrayList<String> wallOrientations = this.getOrientations();
                //single walls
                if (wallOrientations.size() == Parameter.ONE_WALL) {
                    switch (wallOrientations.get(Parameter.FIRST_ELEMENT_POS)) {
                        case Parameter.ORIENTATION_BOTTOM: {
                            path.append("wall-one-bottom.png");
                            break;
                        }
                        case Parameter.ORIENTATION_TOP: {
                            path.append("wall-one-top.png");
                            break;
                        }
                        case Parameter.ORIENTATION_LEFT: {
                            path.append("wall-one-left.png");
                            break;
                        }
                        case Parameter.ORIENTATION_RIGHT: {
                            path.append("wall-one-right.png");
                            break;
                        }
                    }
                    //double walls
                } else if (wallOrientations.size() == Parameter.TWO_WALLS) {
                    String wallOrientation1 = wallOrientations.get(Parameter.FIRST_ELEMENT_POS);
                    String wallOrientation2 = wallOrientations.get(Parameter.SECOND_ELEMENT_POS);
                    if (wallOrientation1.equals(Parameter.ORIENTATION_TOP)) {
                        if (wallOrientation2.equals(Parameter.ORIENTATION_RIGHT)) {
                            path.append("wall-two-top-right.png");
                            break;
                        } else {
                            path.append("wall-two-left-top.png");
                            break;
                        }
                    } else if (wallOrientation1.equals(Parameter.ORIENTATION_BOTTOM)) {
                        if (wallOrientation2.equals(Parameter.ORIENTATION_RIGHT)) {
                            path.append("wall-two-bottom-right.png");
                            break;
                        } else {
                            path.append("wall-two-bottom-left.png");
                            break;
                        }
                    }
                }
                break;
            }

            case "Laser": {
                int laserBeamNumber = this.getCount();
                //As lasers have only one direction, their direction can be found on pos 0 within the orientations array
                String laserOrientation =((Laser) this).getOrientation();
                switch (laserBeamNumber) {
                    case (Parameter.LASER_ONE): {
                        if ((laserOrientation.equals(Parameter.ORIENTATION_RIGHT) || (laserOrientation.equals(Parameter.ORIENTATION_LEFT)))) {
                            path.append("laser-onebeam-left-right.png");
                            break;
                        } else {
                            path.append("laser-onebeam-top-bottom.png");
                            break;
                        }
                    }
                    case (Parameter.LASER_TWO):
                        if ((laserOrientation.equals(Parameter.ORIENTATION_RIGHT) || (laserOrientation.equals(Parameter.ORIENTATION_LEFT)))) {
                            path.append("laser-twobeam-left-right.png");
                            break;
                        } else {
                            path.append("laser-twobeam-top-bottom.png");
                            break;
                        }
                        //Todo Verena: we need images of threebeam lasers without walls
                    case (Parameter.LASER_THREE): {
                        if ((laserOrientation.equals(Parameter.ORIENTATION_RIGHT) || (laserOrientation.equals(Parameter.ORIENTATION_LEFT)))) {
                            path.append("laser-threebeam-left-right.png");
                            break;
                        } else {
                            path.append("laser-threebeam-top-bottom.png");
                            break;
                        }
                    }
                }
                break;
            }

        //Belts (blue and green conveyor belts insofar as they are straight
        case "Belt": {
            int beltSpeed = this.getSpeed();
            String beltOrientation = this.getOrientations().get(Parameter.FIRST_ELEMENT_POS);
            //The Conveyor belts differ mainly in speed: blue speed = 2, green speed = 1
            if (beltSpeed == (int) Parameter.BLUE_BELT_SPEED) {
                switch (beltOrientation) {
                    case Parameter.ORIENTATION_BOTTOM: {
                        path.append("bluecvb-straight-bottom.png");
                        break;
                    }
                    case Parameter.ORIENTATION_TOP: {
                        path.append("bluecvb-straight-top.png");
                        break;
                    }
                    case Parameter.ORIENTATION_LEFT: {
                        path.append("bluecvb-straight-left.png");
                        break;
                    }
                    case Parameter.ORIENTATION_RIGHT: {
                        path.append("bluecvb-straight-right.png");
                        break;
                    }
                }
                // green belts
            } else {
                switch (beltOrientation) {
                    case Parameter.ORIENTATION_BOTTOM: {
                        path.append("greencvb-straight-bottom.png");
                        break;
                    }
                    case Parameter.ORIENTATION_TOP: {
                        path.append("greencvb-straight-top.png");
                        break;
                    }
                    case Parameter.ORIENTATION_LEFT: {
                        path.append("greencvb-straight-left.png");
                        break;
                    }
                    case Parameter.ORIENTATION_RIGHT: {
                        path.append("greencvb-straight-right.png");
                        break;
                    }
                }
                break;
            }
            break;
        }

        //Belts (blue and green insofar as they are curved
        case "RotatingBelt": {
            int speed = this.getSpeed();
            boolean isCrossing = this.getCrossing();
            String curvedBeltOrientationBefore = this.getOrientations().get(Parameter.FIRST_ELEMENT_POS);
            String curvedBeltOrientationAfter = this.getOrientations().get(Parameter.SECOND_ELEMENT_POS);
            //The Conveyor belts differ mainly in speed: blue speed = 2, green speed = 1

            //Blue conveyor belt
            if (speed == (int) Parameter.BLUE_BELT_SPEED) {
                //There are two types of blue conveyor belts: one that is just curved and one that is curving into a crossing
                //Crossings
                if (isCrossing) {
                    switch (curvedBeltOrientationBefore) {
                        case Parameter.ORIENTATION_BOTTOM: {
                            //from each direction the belt could either go top or down starting from either left or right (4*2) hence the if else
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {
                                path.append("bluecvb-curved-bottom-right.png");
                                break;
                            } else {
                                path.append("bluecvb-curved-bottom-left.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_TOP: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {
                                path.append("bluecvb-curved-top-right.png");
                                break;
                            } else {
                                path.append("bluecvb-curved-top-left.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_LEFT: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {
                                path.append("bluecvb-curved-left-top.png");
                                break;
                            } else {
                                path.append("bluecvb-curved-left-bottom.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_RIGHT: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {
                                path.append("bluecvb-curved-right-top.png");
                                break;
                            } else {
                                path.append("bluecvb-curved-right-bottom.png");
                                break;
                            }
                        }
                    }
                }
                //just curved ones
                else {
                    switch (curvedBeltOrientationBefore) {
                        case Parameter.ORIENTATION_BOTTOM: {
                            //Here the same holds true as in the cases above: the belt can either go left or right starting from top or bottom and so on...
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {
                                path.append("bluecvb-curved-sharp-bottom-right.png");
                                break;
                            } else {
                                path.append("bluecvb-curved-sharp-bottom-left.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_TOP: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {
                                path.append("bluecvb-curved-sharp-top-right.png");
                                break;
                            } else {
                                path.append("bluecvb-curved-sharp-top-left.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_LEFT: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_BOTTOM)) {
                                path.append("bluecvb-curved-sharp-left-bottom.png");
                                break;
                            } else {
                                path.append("bluecvb-curved-sharp-left-top.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_RIGHT: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {
                                path.append("bluecvb-curved-sharp-right-top.png");
                                break;
                            } else {
                                path.append("bluecvb-curved-sharp-left-bottom.png");
                                break;
                            }
                        }
                    }
                }
                //Green conveyor belts
                //Todo Verena: green belts that are crossing are missing (blue equivalent bluecv-top-left (example) are there; We need them still in green
            } else if (speed == (int) Parameter.GREEN_BELT_SPEED) {
                if (isCrossing) {
                    switch (curvedBeltOrientationBefore) {
                        case Parameter.ORIENTATION_BOTTOM: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {
                                path.append("greencvb-curved-bottom-right.png");
                                break;
                            } else {
                                path.append("greencvb-curved-bottom-left.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_TOP: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {
                                path.append("greencvb-curved-top-right.png");
                                break;
                            } else {
                                path.append("greencvb-curved-top-left.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_LEFT: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {
                                path.append("greencvb-curved-left-top.png");
                                break;
                            } else {
                                path.append("greencvb-curved-left-bottom.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_RIGHT: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {
                                path.append("greencvb-curved-right-top.png");
                                break;
                            } else {
                                path.append("greencvb-curved-right-bottom.png");
                                break;
                            }
                        }
                    }
                }
                //just green curved ones
                else {
                    switch (curvedBeltOrientationBefore) {
                        case Parameter.ORIENTATION_BOTTOM: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {
                                path.append("greencvb-curved-sharp-bottom-right.png");
                                break;
                            } else {
                                path.append("greencvb-curved-sharp-bottom-left.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_TOP: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_RIGHT)) {
                                path.append("greencvb-curved-sharp-top-right.png");
                                break;
                            } else {
                                path.append("greencvb-curved-sharp-top-left.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_LEFT: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_BOTTOM)) {
                                path.append("greencvb-curved-sharp-left-bottom.png");
                                break;
                            } else {
                                path.append("greencvb-curved-sharp-left-top.png");
                                break;
                            }
                        }
                        case Parameter.ORIENTATION_RIGHT: {
                            if (curvedBeltOrientationAfter.equals(Parameter.ORIENTATION_TOP)) {
                                path.append("greencvb-curved-sharp-right-top.png");
                                break;
                            } else {
                                path.append("greencvb-curved-sharp-left-bottom.png");
                                break;
                            }
                        }
                    }
                }
            }
            break;
        }
        //for empty tiles in case the board si not rectangular
        default:
        return null;
    }

        logger.info(path.toString());
        return image = new Image("/"+path.toString());
}


}
