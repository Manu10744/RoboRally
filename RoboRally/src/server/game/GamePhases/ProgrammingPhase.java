package server.game.GamePhases;

import static utils.Countdown.*;
import static utils.Parameter.*;


/**
 * This class implements the programming phase.
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 */
public class ProgrammingPhase {

    public boolean ready = false;
    public int timer = TIMER_LENGTH;

    /**
     * This method handles the programming phase.
     */
    public void startProgrammingPhase() {
        //TODO
    }

    /**
     * This method starts the Timer, when a player has finished programming. <br>
     * The makeTimer method is implemented in the Countdown class.
     */
    public void startTimer() {
        makeTimer();
    }

    /**
     * This method checks if a player has finished programming.
     */
    public void isFinishedProgramming() {
        //TODO
    }

    /**
     * This method tells each player about the current state of the opponents registers.
     */
    public void tellPlayers() {
        //TODO
    }

    /**
     * This method tells the Clients which cards they have.
     */
    public void giveCards() {
        //TODO
    }

    /**
     * This method receives the cards from the Players.
     */
    public void receiveCards() {
        //TODO
    }


}
