package utils;

import java.util.Timer;
import java.util.TimerTask;

import static utils.Parameter.*;

/**
 * This class implements a countdown.
 * @ author Vincent Tafferner
 */
public class Countdown {
    static int duration;
    static Timer timer;


    /**
     * This method does the counting.
     */
    public static void makeTimer() {
        int secs = TIMER_LENGTH;
        int delay = TIMER_DELAY;
        int period = TIMER_PERIOD;
        timer = new Timer();
        duration = secs;
        // System.out.println(secs);
        timer.scheduleAtFixedRate(new TimerTask() {

            /**
             * This run method overwrites the standard run method.
             */
            @Override
            public void run() {
                //System.out.println(" Bla " + setInterval());
                setInterval();
            }
        }, delay, period);
    }

    /**
     * This method stops the timer if it runs out.
     */
    private static int setInterval() {
        if (duration == 1) {
            timer.cancel();
        }else {
            return --duration;
        }
        return 0;
    }

}