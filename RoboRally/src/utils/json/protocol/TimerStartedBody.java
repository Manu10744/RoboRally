package utils.json.protocol;

import client.Client;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'TimerStarted' protocol JSON message.
 * @author Manuel Neumayer
 */
public class TimerStartedBody implements ServerMessageAction<TimerStartedBody> {
    // Is always empty
    public TimerStartedBody() { }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, TimerStartedBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handleTimerStarted(client, task, bodyObject);
    }
}
