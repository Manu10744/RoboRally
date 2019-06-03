package src.modelserver.game;

/**
 * Spam cards are the simplest form of damage a player can receive. <br>
 * A description of the cards effect can be found at the activateDamage method.
 *
 * @author Vincent Tafferner
 */
public class SpamCard extends DamageCard {

    String cardName = "SpamCard";
    int cardAmount = 36;

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case the player has to play the top card of his programming deck.
     */
    @Override
    public void activateDamage() {
        // TODO
    }
}
