package modelserver.game;

/**
 * This class defines a kind of damage.
 *
 * @author Vincent Tafferner
 */
public class VirusCard extends DamageCard {

    String cardName = "VirusCard";
    int cardAmount = 18;

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case all robots within a 6-tile radius take a SpamCard. <br>
     * You play the top card of your programming deck.
     */
    @Override
    public void activateDamage() {
        // TODO
    }
}
