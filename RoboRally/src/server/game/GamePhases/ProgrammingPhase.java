package server.game.GamePhases;

import static utils.Countdown.*;
import static utils.Parameter.*;


/**
 * This class implements the programming phase.
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 */
public class ProgrammingPhase {

    public static boolean ready;

    public ProgrammingPhase(){
        ready = false;
    }

    /**
     * This method handles the programming phase.
     */
    public static void startProgrammingPhase() {
        //TODO
    }

    /**
     * This method starts the Timer, when a player has finished programming. <br>
     * The makeTimer method is implemented in the Countdown class.
     */
    public static void startTimer() {
        makeTimer();
    }

    /**
     * This method checks if a player has finished programming.
     */
    public static void isFinishedProgramming() {
        ready = true;
    }

    /**
     * This method tells each player about the current state of the opponents registers.
     */
    public static void tellPlayers() {
        //TODO
    }

    /**
     * This method tells the Clients which cards they have.
     */
    public static void giveCards() {
        //TODO
    }

    /**
     * This method receives the cards from the Players.
     */
    public static void receiveCards() {
        //TODO
    }


}
