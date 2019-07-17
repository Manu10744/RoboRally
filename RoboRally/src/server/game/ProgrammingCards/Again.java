package server.game.ProgrammingCards;

import server.game.Card;
import server.game.Player;

import java.util.ArrayList;

/**
 * This class implements the Again card.
 *
 * @author Vincent Tafferner
 */
public class Again extends server.game.Card {

    public Again() {
        cardName = "Again";
    }

    /**
     * This move will repeat the move that was performed in the previous register. <br>
     * If there was no previous move the robot shall do nothing.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, ArrayList<Card> register) {
    //TODO
    }

}
