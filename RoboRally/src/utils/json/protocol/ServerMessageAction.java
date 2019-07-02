package utils.json.protocol;

import client.Client;

/**
 * This interface is implemented by each class in {@link utils.json.protocol} that represents the message body of
 * a specific {@link JSONMessage}. It only demands a function to be implemented that contains all the logic that should
 * happen when a message with the respective message body was received by either the client or the server.
 *
 * @author Manuel Neumayer
 */
public interface ServerMessageAction<T> {
    void triggerAction(Client client, Client.ClientReaderTask task, T bodyObject);
}
