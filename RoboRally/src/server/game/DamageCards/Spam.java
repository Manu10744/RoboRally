package server.game.DamageCards;

import server.game.Card;
import server.game.Player;
import server.game.Tiles.Pit;
import server.game.Tiles.PushPanel;
import server.game.Tiles.Wall;

import java.util.Map;

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
    public void activateCard(Player player, Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap) {
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
        player.getDeckDraw().getTopCard().activateCard(player, pitMap, wallMap, pushPanelMap);
    }

    @Override
    public boolean isDamageCard() {
        return true;
    }
}
