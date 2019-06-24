package server.game.ProgrammingCards;

/**
 * This class implements the PowerUp card.
 *
 * @author Vincent Tafferner
 */
public class PowerUp extends ProgrammingCard {

    public PowerUp() {
        cardName = "PowerUp";
    }

    /**
     * Even though the name suggests a move, in this case the robot will activate a powerup instead of moving <br>
     * around on the map.
     */
    @Override
    public void moveRobot() {
        // TODO
    }
}
