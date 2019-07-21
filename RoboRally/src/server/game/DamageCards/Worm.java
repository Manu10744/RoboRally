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
public class Worm extends server.game.Card {

    public Worm() {
        cardName = "Worm";
    }

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case the robot has to reboot.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap) {
        //TODO
    }
}
