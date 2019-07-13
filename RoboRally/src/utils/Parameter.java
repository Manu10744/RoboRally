package utils;

import javax.sound.sampled.Port;

/**
 * In this class all the Parameters that will occur in the diffenrent classes will show up. <br>
 * The goal is to have them all in one spot.
 *
 * @author Ivan Dovecar
 */
public class Parameter {

    // Chat
    public static final String INVALID_CLIENTNAME = "INVALID####";
    public static final int MAX_NAMESIZE = 15;
    public static final int PUBLIC_MESSAGE_VALUE = -1;

    //Cards
    public static final int SPAM_CARDS_AMOUNT = 36;
    public static final int TROJAN_CARDS_AMOUNT = 12;
    public static final int VIRUS_CARDS_AMOUNT = 18;
    public static final int WORM_CARDS_AMOUNT = 6;
    public static final int MOVEI_CARDS_AMOUNT = 5;
    public static final int MOVEII_CARDS_AMOUNT = 3;
    public static final int MOVEIII_CARDS_AMOUNT = 1;
    public static final int TURNRIGHT_CARDS_AMOUNT = 3;
    public static final int TURNLEFT_CARDS_AMOUNT = 3;
    public static final int AGAIN_CARDS_AMOUNT = 2;
    public static final int UTURN_CARDS_AMOUNT = 1;
    public static final int BACKUP_CARDS_AMOUNT = 1;
    public static final int POWERUP_CARDS_AMOUNT = 1;

    public static final int HAND_CARDS_AMOUNT = 9;
    public static final int REGISTER_CARDS_AMOUNT = 5;


    //Server
    public static final int SERVER_PORT = 9998;

    //GUI
    public static final int GUIS_TO_START = 1;

    //Game
    public static final int TIMER_LENGTH = 30;
    public static final int TIMER_DELAY = 1000; // This should be 1000 because a second has 1000 milliseconds.
    public static final int TIMER_PERIOD = 1000;

        //amount of players
    public static final int MAX_PLAYERSIZE = 6;
    public static final int MIN_PLAYERSIZE = 2;

        //Register numbers
    public static final int REGISTER_ONE = 1;
    public static final int REGISTER_TWO = 2;
    public static final int REGISTER_THREE = 3;
    public static final int REGISTER_FOUR = 4;
    public static final int REGISTER_FIVE = 5;

    //Amount of usual imageViews in each PlayerMat hBox
    public static final int CARDS_WIDTH = 8;


    //Tiles
       //Orientation
    public static final String ORIENTATION_RIGHT = "right";
    public static final String ORIENTATION_LEFT = "left";
    public static final String ORIENTATION_DOWN = "down";
    public static final String ORIENTATION_UP = "up";

        //Count number for amount of lasers
    public static final int LASER_ONE = 1;
    public static final int LASER_TWO = 2;
    public static final int LASER_THREE = 3;

        //Count number for checkpoint (victory) labels
    public static final int CHECKPOINT_ONE = 1;
    public static final int CHECKPOINT_TWO = 2;
    public static final int CHECKPOINT_THREE = 3;
    public static final int CHECKPOINT_FOUR = 4;
    public static final int CHECKPOINT_FIVE = 5;
    public static final int CHECKPOINT_SIX = 6;

        //names
    public static String ANTENNA_NAME = "Antenna";
    public static String BELT_NAME = "Belt";
    public static String CHECKPOINT_NAME = "CheckPoint";
    public static String EMPTY_NAME = "Empty";
    public static String ENERGYSPACE_NAME = "EnergySpace";
    public static String GEAR_NAME = "Gear";
    public static String LASER_NAME = "Laser";
    public static String PIT_NAME = "Pit";
    public static String PUSHPANEL_NAME = "PushPanel";
    public static String ROTATINGBELT_NAME = "RotatingBelt";
    public static String STARTPOINT_NAME = "StartPoint";
    public static String WALL_NAME = "Wall";


    //PushPanels number of Registers
    public static  final int PUSHPANEL_REG_NUM_TWO = 2;
    public static  final int PUSHPANEL_REG_NUM_THREE = 3;
    //Gear color
    public static final String RED_GEAR = "red";
    public static final String GREEN_GEAR = "green";

    //Belt
            // color
    public static final String GREEN_BELT = "green";
    public static final String BLUE_BELT = "blue";
            // speed
    public static final int GREEN_BELT_SPEED = 1;
    public static final int BLUE_BELT_SPEED = 2;

    //Wall
        //amount of walls
    public static final int ONE_WALL = 1;
    public static final int TWO_WALLS = 2;

    //ArrayElements
    public static int FIRST_ELEMENT_POS = 0;
    public static int SECOND_ELEMENT_POS = 1;

    //ChooseRobot
    public static final int CHOOSE_ROBOT_RATIO_WIDTH = 5;
    public static final int CHOOSE_ROBOT_RATIO_HEIGHT = 3;
    public static final int CHOOSE_ROBOT_RATIO_WIDTH_BGR = 1;
    public static final int CHOOSE_ROBOT_RATIO_HEIGHT_BGR = 1;

    //StartScreen
    public static final int START_SCREEN_RATIO_WIDTH = 1;
    public static final int START_SCREEN_RATIO_HEIGHT = 1;

    //Scoreboard
    public static final int SCOREBOARD_RATIO_WIDTH = 1;
    public static final int SCOREBOARD_RATIO_HEIGHT = 1;
    public static final int SCOREBOARD_SIEGERTREPPE_RATIO_WIDTH = 3;
    public static final int SCOREBOARD_SIEGERTREPPE_RATIO_HEIGHT = 2;
    public static final int SCOREBOARD_ROBOT_RATIO_WIDTH = 8;
    public static final int SCOREBOARD_ROBOT_RATIO_HEIGHT = 4;

    public static final int SCOREBOARD_ROBOT_LOSER_RATIO_WIDTH = 10;
    public static final int SCOREBOARD_ROBOT_LOSER_RATIO_HEIGHT = 5;


    //Lobby
    public static final int LOBBY_RATIO_WIDTH_BGR = 1;
    public static final int LOBBY_RATIO_HEIGHT_BGR = 1;

}
