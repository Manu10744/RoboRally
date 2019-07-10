package utils.json.protocol;

import server.Server;
import utils.json.MessageDistributer;

/**
 * This interface is implemented by each class in {@link utils.json.protocol} that represents the message body of
 * a specific {@link JSONMessage}. It only demands a function to be implemented that contains all the logic that should
 * happen when a message with the respective message body was received by either the client or the server.
 *
 * @author Manuel Neumayer
 */
public interface ClientMessageAction<T> {
    void triggerAction(Server server, Server.ServerReaderTask task, T bodyObject, MessageDistributer messageDistributer);
}
