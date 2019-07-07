package server.game.GamePhases;

import java.util.Timer;
import java.util.TimerTask;

import static utils.Parameter.*;


/**
 * This class implements the programming phase.
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 */
public class ProgrammingPhase {

    //This variable is set to true if a player is done with programming.
    public boolean ready;

    //these variables are needed for the timer method.
    Timer timer = new Timer();
    int secs = TIMER_LENGTH;
    int delay = TIMER_DELAY;
    int period = TIMER_PERIOD;


    public ProgrammingPhase(){
        ready = false;
    }

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

        // System.out.println(secs);
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                //System.out.println(" Bla " + setInterval());
                setInterval();
            }
        }, delay, period);
    }

        /**
         * This method stops the timer if it runs out. <br>
         * It makes the drops the seconds counter by 1 each time it is called. <br>
         * It should be called once each second. (depends on Parameters)
         */
        private int setInterval() {
            if (secs == 1) {
                timer.cancel();
            }else {
                return --secs;
            }
            return 0;
        }

    /**
     * This method checks if a player has finished programming.
     */
    public void isFinishedProgramming() {
        ready = true;
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
