package server.game.DamageCards;
import static utils.Parameter.SPAM_CARDS_AMOUNT;


/**
 * Spam cards are the simplest form of damage a player can receive. <br>
 * A description of the cards effect can be found at the activateDamage method.
 *
 * @author Vincent Tafferner
 */
public class Spam extends DamageCard {

    int cardAmount = SPAM_CARDS_AMOUNT;

    public Spam() {
        cardName = "Spam";
    }

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case the player has to play the top card of his programming deck.
     */
    @Override
    public void activateCard() {
        //TODO
    }
}
