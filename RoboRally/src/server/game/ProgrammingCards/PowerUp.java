package server.game.ProgrammingCards;

import server.game.Card;
import server.game.Player;
import server.game.Robot;

import java.util.ArrayList;

/**
 * This class implements the PowerUp card.
 *
 * @author Vincent Tafferner
 */
public class PowerUp extends server.game.Card {

    public PowerUp() {
        cardName = "PowerUp";
    }

    /**
     * Even though the name suggests a move, in this case the robot will activate a powerup instead of moving <br>
     * around on the map.
     * //TODO remove if not needed in final version.
     */

    @Override
    public void activateCard(Player player, ArrayList<Card> register) {
        int currentEnergy = player.getEnergy();
        player.setEnergy(currentEnergy + 1);
    }

}
