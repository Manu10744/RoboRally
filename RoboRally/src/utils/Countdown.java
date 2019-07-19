package utils;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static utils.Parameter.*;

/**
 * This class implements a countdown.
 * @ author Vincent Tafferner
 */
public class Countdown {
    Timer timer = new Timer();
    int secs;
    int delay;
    int period;

    /**
     * This is the constructor of a Countdown.
     */
    public Countdown(int secs, int delay, int period) {
        this.secs = secs;
        this.delay = delay;
        this.period = period;
    }

    /**
     * This method does the counting.
     */
    public void startTimer(CountDownLatch latch) {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("REMAINING SECONDS OF COUNTDOWN:" + latch.getCount());
                latch.countDown();

                // End the timer
                if (latch.getCount() == 0) {
                    timer.cancel();
                }
            }
        }, delay, period);
    }
}
