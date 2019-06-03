package src.modelserver.game;

/**
 * This class defines a kind of damage.
 *
 * @author Vincent Tafferner
 */
public class TrojanHorseCard extends DamageCard {

    String cardName = "TrojanHorseCard";
    int cardAmount = 12;

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case you have to take two SpamCards and play the top card of your programming deck.
     */
    @Override
    public void activateDamage() {
        // TODO
    }
}
