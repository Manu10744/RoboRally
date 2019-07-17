package server.game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This class defines what a card shall be in the game. <br>
 * There are more specific cards defined in the classes that inherit from Card.
 *
 * @author Vincent Tafferner
 */
public abstract class Card {

    @Expose @SerializedName("card")
    public String cardName;

    //This is the constructor :D
    public Card(){
        cardName = "Card";
    }

    /**
     * This is the method that is called, when the Card's effect is activated. <br>
     * It will be overwritten in each subclass.
     */
    public abstract void activateCard(Robot robot, ArrayList<Card> register);

    /**
     * This method simply returns the name of the card.
     * @return cardName
     */
    public String getCardName() {
        return cardName;
    }

}
