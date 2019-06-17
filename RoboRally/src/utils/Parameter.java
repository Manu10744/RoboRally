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
    public static final int GUIS_TO_START = 2;

    //Game
    public  static final int MAX_PLAYERSIZE = 6;
    public static final int MIN_PLAYERSIZE = 2;

    public static final int REGISTER_ONE = 1;
    public static final int REGISTER_TWO = 2;
    public static final int REGISTER_THREE = 3;
    public static final int REGISTER_FOUR = 4;
    public static final int REGISTER_FIVE = 5;

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

}
