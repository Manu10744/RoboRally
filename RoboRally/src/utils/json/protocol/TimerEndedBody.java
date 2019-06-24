package utils.json.protocol;

import client.Client;
import com.google.gson.annotations.Expose;
import utils.json.MessageDistributer;

import java.util.ArrayList;

/** This is the wrapper class for the message body of the 'TimerEnded' protocol JSON message.
 * @author Manuel Neumayer
 */
public class TimerEndedBody implements ServerMessageAction<TimerEndedBody> {
    @Expose
    private ArrayList<Integer> playerIDs;

    public TimerEndedBody(ArrayList<Integer> playerIDs) {
        this.playerIDs = playerIDs;
    }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, TimerEndedBody bodyObject) {
        MessageDistributer.handleTimerEnded(client, task, bodyObject);
    }

    public ArrayList<Integer> getPlayerIDs() {
        return playerIDs;
    }
}
