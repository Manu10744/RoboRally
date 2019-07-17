package server.game.DamageCards;

import server.game.Card;
import server.game.Player;

import java.util.ArrayList;

/**
 * This class defines a kind of damage.
 *
 * @author Vincent Tafferner
 */
public class Trojan extends server.game.Card {

    public Trojan() {
        cardName = "Trojan";
    }

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case you have to take two SpamCards and play the top card of your programming deck.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, ArrayList<Card> register) {
        //TODO
    }

}
