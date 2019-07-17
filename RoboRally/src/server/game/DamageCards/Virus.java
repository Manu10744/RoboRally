package server.game.DamageCards;

import server.game.Card;
import server.game.Player;

import java.util.ArrayList;

/**
 * This class defines a kind of damage.
 *
 * @author Vincent Tafferner
 */
public class Virus extends server.game.Card {

    public Virus() {
        cardName = "Virus";
    }

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case all robots within a 6-tile radius take a Spam card. <br>
     * You play the top card of your programming deck.
     */

    @Override
    public void activateCard(Player player, ArrayList<Card> register) {
        //TODO
    }
}
