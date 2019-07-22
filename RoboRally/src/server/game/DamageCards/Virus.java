package server.game.DamageCards;

import server.game.Player;
import server.game.Tiles.Pit;
import server.game.Tiles.PushPanel;
import server.game.Tiles.Wall;

import java.util.Map;

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
    public void activateCard(Player player, Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap) {
        //TODO
    }
}
