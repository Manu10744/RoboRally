package server.game.DamageCards;

import server.game.Card;
import server.game.Player;

import java.util.ArrayList;

/**
 * Spam cards are the simplest form of damage a player can receive. <br>
 * A description of the cards effect can be found at the activateDamage method.
 *
 * @author Vincent Tafferner
 */
public class Spam extends server.game.Card {

    public Spam() {
        cardName = "Spam";
    }

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case the player has to play the top card of his programming deck.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, ArrayList<Card> register) {
        //TODO
    }

}
