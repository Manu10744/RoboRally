package server.game.DamageCards;
import static utils.Parameter.VIRUS_CARDS_AMOUNT;

/**
 * This class defines a kind of damage.
 *
 * @author Vincent Tafferner
 */
public class Virus extends DamageCard {

    public Virus() {
        cardName = "Virus";
    }
    int cardAmount = VIRUS_CARDS_AMOUNT;

    /**
     * This is the method that activates the effect of a damage card. <br>
     * In this case all robots within a 6-tile radius take a Spam card. <br>
     * You play the top card of your programming deck.
     */
    public void activateCard() {
        //TODO
    }
}
