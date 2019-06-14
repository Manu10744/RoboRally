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


    //Server
    public static final int SERVER_PORT = 9998;

    //GUI
    public static final int GUIS_TO_START = 2;

    //Game
    public  static final int MAX_PLAYERSIZE = 6;
    public static final int MIN_PLAYERSIZE = 2;
}
