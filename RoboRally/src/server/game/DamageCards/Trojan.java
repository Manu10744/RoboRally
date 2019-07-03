package server.game.DamageCards;

import static utils.Parameter.TROJAN_CARDS_AMOUNT;

/**
 * This class defines a kind of damage.
 *
 * @author Vincent Tafferner
 */
public class Trojan extends DamageCard {

    public Trojan() {
        cardName = "Trojan";
    }
    int cardAmount = TROJAN_CARDS_AMOUNT;

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case you have to take two SpamCards and play the top card of your programming deck.
     */
    @Override
    public void activateCard() {
        //TODO
    }
}
