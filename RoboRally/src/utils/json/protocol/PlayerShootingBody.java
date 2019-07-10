package utils.json.protocol;

import client.Client;
import utils.json.MessageDistributer;

/** This is the wrapper class for the message body of the 'PlayerSHooting' protocol JSON message.
 * @author Manuel Neumayer
 */
public class PlayerShootingBody implements ServerMessageAction<PlayerShootingBody> {

    // Is always empty
    public PlayerShootingBody() { }

    @Override
    public void triggerAction(Client client, Client.ClientReaderTask task, PlayerShootingBody bodyObject, MessageDistributer messageDistributer) {
        messageDistributer.handlePlayerShooting(client, task, bodyObject);
    }
}
