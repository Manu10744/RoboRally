package server.game;

import server.game.DamageCards.DamageCard;
import server.game.ProgrammingCards.*;
import server.game.GamePhases.*;
import server.game.decks.*;

import java.util.ArrayList;
import java.util.Collections;

import static utils.Parameter.*;

/**
 * This class is responsible for handling the Decks in the game. <br>
 * For this purpose it has all the methods that transform the decks. <br>
 * The decks are created in their own classes.
 *
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 */
public class Deck {

    private static ArrayList<Card> deckDraw;
    private static ArrayList<Card> deckDiscard;
    private static ArrayList<Card> deckHand;
    private static ArrayList<Card> deckRegister;

    private static ArrayList<Card> deckSpam;
    private static ArrayList<Card> deckVirus;
    private static ArrayList<Card> deckWorm;
    private static ArrayList<Card> trojanHorse;

    private static Card topCard;

    /**
     * This method shuffles the deck.
     */
    public static void shuffleDeck(ArrayList<Card> Deck) {
        Collections.shuffle(Deck);
    }

    /**
     * This method is used for drawing cards from the deck. <br>
     * It draws Cards until the maximum amount of allowed hand cards is reached.
     */
    public static void drawCard() {
        for (int i = 0; i < HAND_CARDS_AMOUNT; i++) {

            // Each time this checks if there are still enaugh Cards left in the deckDraw.
            if (deckEmpty(deckDraw)) {
                addDiscardToDraw();
                shuffleDeck(deckDraw);
            }

            // The first Card of deckDraw is added to the deckHand.
            deckHand.add(getTopCard(deckDraw));
            removeTopCard(deckDraw);
        }
    }

    /**
     * This method can add a Card from the deckHand to the deckRegister.
     * //TODO let the player choose a card.
     */
    public static void addRegister(){
        if (deckRegister.size() >= REGISTER_CARDS_AMOUNT) {
            //isFinishedProgramming();
            System.out.println("The Register is full!");
        }else {
            deckRegister.add(new Card());
            deckHand.remove(new Card());
        }
    }

    /**
     * This method clears the deckRegister after they have been played.
     */
    public static void clearRegister(){
        clearDeck(deckRegister);
    }

    /**
     * This method knows the top card of any deck.
     */
    public static Card getTopCard(ArrayList<Card> Deck) {
        topCard = Deck.get(0);
        return topCard;
    }

    /**
     * This method removes the Top Card of a deck.
     */
    public static void removeTopCard(ArrayList<Card> Deck) {
        Deck.remove(0);
    }

    /**
     * This method clears a Deck. For example you could clear the deckDiscard after you've added it to the deckDraw.
     */
    public static void clearDeck(ArrayList<Card> Deck) {
        Deck.clear();
    }

    /**
     * This method is needed, if the deckDraw gets to small.
     */
    public static void addDiscardToDraw() {
        deckDraw.addAll(deckDiscard);
        clearDeck(deckDiscard);
    }

    /**
     * This method is used to add the deckHand to the deckDiscard.
     */
    public static void addHandToDiscard() {
        deckDiscard.addAll(deckHand);
        clearDeck(deckHand);
    }

    /**
     * This method can check if a Deck has Cards left in it.
     */
    public static boolean deckEmpty(ArrayList<Card> Deck){
        return Deck.isEmpty();
    }

    /**
     * This method draws a Damage Card and adds it to the deckDiscard.
     */
    public static void drawDamageCard(ArrayList<Card> DamageDeck, ArrayList<Card> DiscardDeck, Card DamageCard){
        if (!deckEmpty(DamageDeck)) {
            DiscardDeck.add(DamageCard);
            removeTopCard(DamageDeck);
        }
    }

    /**
     * This method fills the registers with Cards from the deckDraw, if the timer ended. <br>
     * It is necessary to ask for all indices specifically because its possible, that the register at index 0 is empty <br>
     * while the others are already filled. <br>
     * The try catch is necessary because its not possible to look if an index is null as with normal arrays. <br>
     * This is because get() throws an exception instead of null.
     */
    public static void fillRegisters() {

        // If the index at 0 is empty the first Card of deckDraw will be put there
        try {
            deckRegister.get(0);
        } catch (IndexOutOfBoundsException e ) {

            // In the case that the deckDraw has no Cards left in it, it will get new Cards from the deckDiscard.
            if (deckEmpty(deckDraw)) {
                addDiscardToDraw();
                shuffleDeck(deckDraw);
            }

            deckRegister.add(0, getTopCard(deckDraw));
            removeTopCard(deckDraw);
        }

        // If the index at 1 is empty the first Card of deckDraw will be put there
        try {
            deckRegister.get(1);
        } catch (IndexOutOfBoundsException e ) {

            // In the case that the deckDraw has no Cards left in it, it will get new Cards from the deckDiscard.
            if (deckEmpty(deckDraw)) {
                addDiscardToDraw();
                shuffleDeck(deckDraw);
            }

            deckRegister.add(1, getTopCard(deckDraw));
            removeTopCard(deckDraw);
        }

        // If the index at 2 is empty the first Card of deckDraw will be put there
        try {
            deckRegister.get(2);
        } catch (IndexOutOfBoundsException e ) {

            // In the case that the deckDraw has no Cards left in it, it will get new Cards from the deckDiscard.
            if (deckEmpty(deckDraw)) {
                addDiscardToDraw();
                shuffleDeck(deckDraw);
            }

            deckRegister.add(2, getTopCard(deckDraw));
            removeTopCard(deckDraw);
        }

        // If the index at 3 is empty the first Card of deckDraw will be put there
        try {
            deckRegister.get(3);
        } catch (IndexOutOfBoundsException e ) {

            // In the case that the deckDraw has no Cards left in it, it will get new Cards from the deckDiscard.
            if (deckEmpty(deckDraw)) {
                addDiscardToDraw();
                shuffleDeck(deckDraw);
            }

            deckRegister.add(3, getTopCard(deckDraw));
            removeTopCard(deckDraw);
        }

        // If the index at 4 is empty the first Card of deckDraw will be put there
        try {
            deckRegister.get(4);
        } catch (IndexOutOfBoundsException e ) {

            // In the case that the deckDraw has no Cards left in it, it will get new Cards from the deckDiscard.
            if (deckEmpty(deckDraw)) {
                addDiscardToDraw();
                shuffleDeck(deckDraw);
            }

            deckRegister.add(4, getTopCard(deckDraw));
            removeTopCard(deckDraw);
        }
    }
}
