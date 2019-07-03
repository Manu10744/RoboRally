package server.game.DamageCards;
import static utils.Parameter.WORM_CARDS_AMOUNT;

/**
 * This class defines a kind of damage.
 *
 * @author Vincent Tafferner
 */
public class Worm extends DamageCard {

    public Worm() {
        cardName = "Worm";
    }

    int cardAmount = WORM_CARDS_AMOUNT;

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case the robot has to reboot.
     */
    public void activateCard() {
        //TODO
    }
}
