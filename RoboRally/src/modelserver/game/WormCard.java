package modelserver.game;

/**
 * This class defines a kind of damage.
 *
 * @author Vincent Tafferner
 */
public class WormCard extends DamageCard {

    String cardName = "WormCard";
    int cardAmount = 6;

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case the robot has to reboot.
     */
    @Override
    public void activateDamage() {
        // TODO
    }
}
