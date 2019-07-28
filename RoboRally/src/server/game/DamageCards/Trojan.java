package server.game.DamageCards;

import server.game.Card;
import server.game.Card;
import server.game.Player;
import server.game.Robot;
import server.game.Tiles.Antenna;
import server.game.Tiles.Pit;
import server.game.Tiles.PushPanel;
import server.game.Tiles.Wall;

import java.util.Map;

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
    public void activateCard(Player player, Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap, Map<String, Robot> robotMap, Map<String, Antenna> antennaMap) {
        // Put the spam card back to the spam deck
        player.getDeckSpam().getDeck().add(this);

        Card topCard = player.getDeckDraw().getTopCard();

        // Remove the spam card and put the random programming card into the current register
        for (int i = 0; i < player.getDeckRegister().getDeck().size(); i++) {
            if (player.getDeckRegister().getDeck().get(i) == this) {
                // Remove so its not passed into the discard pile again
                player.getDeckRegister().getDeck().remove(this);
                player.getDeckRegister().getDeck().set(i, topCard);

                // Remove the taken card from the draw pile
                player.getDeckDraw().getDeck().remove(topCard);
            }
        }

        // Play the taken card
        player.getDeckDraw().getTopCard().activateCard(player, pitMap, wallMap, pushPanelMap, robotMap, antennaMap);

        // Take two spam cards
        for (int i = 0; i < 2; i++) {
            Card spamCard = player.getDeckSpam().getTopCard();
            player.getDeckDraw().getDeck().add(spamCard);

            player.getDeckSpam().getDeck().remove(spamCard);
        }
    }

    @Override
    public boolean isDamageCard() {
        return true;
    }
}
