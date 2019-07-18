package utils;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

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

        Timer timer = new Timer();
    }

    /**
     * This method does the counting.
     */
    public int startTimer(Label label) {

        Timer timer = new Timer();

        // System.out.println(secs);
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                //System.out.println(" Bla " + setInterval());
                setInterval(label);
            }
        }, delay, period);
        return secs;
    }

    /**
     * This method stops the timer if it runs out. <br>
     * It makes the drops the seconds counter by 1 each time it is called. <br>
     * It should be called once each second. (depends on Parameters)
     */
    private int setInterval(Label label) {
        if (secs == 1) {
            timer.cancel();
            label.setVisible(false);

            return secs;
        }else {
            System.out.println(secs);
            Platform.runLater(() -> {
                ScaleTransition transition = new ScaleTransition(Duration.millis(100), label);
                transition.setByX(1.5f);
                transition.setByY(1.5f);
                transition.setCycleCount(4);
                transition.setAutoReverse(true);
                transition.play();

                label.setText(String.valueOf(secs));
            });
            return --secs;
        }
    }

    /**
     * This is the getter for the seconds parameter.
     * @return how long the timer is set in seconds.
     */
    public int getSecs() {
        return this.secs;
    }

    /**
     * This is the getter for the delay.
     * @return the delay.
     */
    public int getDelay() {
        return this.delay;
    }

    /**
     * this is the getter for the period.
     * @return period.
     */
    public int getPeriod() {
        return this.period;
    }


}
