package src.modelserver.game;

import src.modelserver.game.Player;
import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * This class handles the Game itself. <br>
 * It starts it and keeps track of the players.
 *
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 */
public class Game {
    private ArrayList<Player> players;
    private ArrayList<Player> activePlayers;

    /**
     * This method starts a game
     */
    public void startGame() {

    }

    /**
     * This method is responsible for choosing the player who starts the game
     */
    public void chooseFirstPlayer() {

    }

    /**
     * This method is responsible for choosing the player whose turn is next
     */
    public void chooseNextplayer() {

    }

    /**
     * This method is responsible for ending a game
     */
    public void endGame() {

    }

    /**
     * Getter for the array list the players are stored in
     * @return players The players of the game
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    /**
     * Getter for the array list the active players are stored in
     * @return activePlayers The activePlayers of a game round
     */
    public ArrayList<Player> getActivePlayers() {
        return this.activePlayers;
    }

}
