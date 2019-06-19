package modelserver.game;

import modelserver.Server;
import modelserver.game.Player;
import java.lang.reflect.Array;
import java.util.ArrayList;

import modelserver.game.*;

/**
 * This class handles the Game itself. <br>
 * It starts it and keeps track of the players.
 *
 * @author Ivan Dovecar
 * @author Vincent Tafferner
 * @author Jessica Gerlach
 */
public class Game {
    private ArrayList<Player> players;
    private ArrayList<Player> activePlayers;
    private Server server;
    private boolean isFinished;


    public Game(Server server) {
        this.server = server;
        isFinished = false;
    }

    /**
     * This method starts a game
     * @author Ivan Dovecar
     */
    public void startGame(ArrayList<Player> players) {
        //Create Players:
        this.players = new ArrayList<>(players);
        activePlayers = new ArrayList<>(this.players);

        System.out.println("Game has started");

        //TODO
        //Here the different Phases should be called in a smart order.


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
