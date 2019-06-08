package utils.instructions;

public class ServerGameInstruction {

    public enum ServerGameInstructionType {
        //ServerGameInstruction
        PLAYER_ADDED, //Server confirms player_name and player_figure
        PLAYER_STATUS, // Server informs all other players of the status of the new player
        GAME_STARTED, // Server sends maps to clients
        CARD_PLAYED, // Server notifies all other players of the played card
        CURRENT_PLAYER, // Server informs all of the player that is to move
        ACTIVE_PHASE, // Server informs all about the current game phase
        STARTING_POINT_TAKEN, // Server confirms starting point and informs other players of it
        YOUR_CARDS, // Server informs player of her or his hand
        NOT_YOUR_CARD, // Server informs other players of the number of cards another has
        SHUFFLE_CODING, // If not enough cards are on the discarded pile it ha sto be reshuffled
        TIMER_STARTED, // Server starts timer if someone has a full register
        TIMER_ENDED, // Server informs all clients that time for choosing programming cards has run out; player IDs of too slow players is saved
        CARDS_YOU_GOT_NOW, // Server fills empty registers after time ran out
        CURRENT_CARDS, // Server informs all palyers of the cards in the current register
        MOVEMENT, // Server informs other players of a move made (just walking, no turning)
        DRAW_DAMAGE, // Server informs player of damage suffered in round; the damage will be handed out in fixed bundles, no individual damage is given
        REBOOT, // Server informs all player if another one has to reboot
        PLAYER_TURNING, // Server informs all clients if a player turns (left, right)
        ENERGY, // Server informs client of new energy level and its reason for changing
        CHECKPOINT_REACHED, // Server informs all palyers if a player reached a checkpoint
        GAME_FINISHED // Server informs palyers if a player has won
    }
}
