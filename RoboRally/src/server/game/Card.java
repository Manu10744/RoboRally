package server.game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class defines what a card shall be in the game. <br>
 * There are more specific cards defined in the classes that inherit from Card.
 *
 * @author Vincent Tafferner
 */
public abstract class Card {

    @Expose @SerializedName("card")
    public String cardName;

    public Card(){
        cardName = "Card";
    }

    /**
     * This method simply returns the name of the card.
     * @return cardName
     */
    public String getCardName() {
        return cardName;
    }

}
