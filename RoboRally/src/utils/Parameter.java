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
    public static  final int TROJAN_CARDS_AMOUNT = 12;
    public static final int VIRUS_CARDS_AMOUNT = 18;
    public static final int WORM_CARDS_AMOUNT = 6;

    //Server
    public static final int SERVER_PORT = 9998;

    //Game
    public  static final int MAX_PLAYERSIZE = 6;
    public static final int MIN_PLAYERSIZE = 2;
}
