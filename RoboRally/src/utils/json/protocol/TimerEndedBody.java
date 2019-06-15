package utils.json.protocol;

import java.util.ArrayList;

public class TimerEndedBody {
    private ArrayList<Integer> playerIDs;

    public TimerEndedBody(ArrayList<Integer> playerIDs) {
        this.playerIDs = playerIDs;
    }

    public ArrayList<Integer> getPlayerIDs() {
        return playerIDs;
    }
}
