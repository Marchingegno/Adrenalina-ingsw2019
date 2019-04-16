package it.polimi.se2019.network.server;

import it.polimi.se2019.network.ConnectionInterface;
import it.polimi.se2019.network.message.Message;

/**
 * When implemented represents what the server can do when receiving messages from the clients.
 */
public interface ServerMessageReceiverInterface {

	/**
	 * Implemented when willing to process client's registrations.
	 * @param client the client that is registering,
	 */
	void onClientRegistration(ConnectionInterface client);

	/**
	 * Implemented when willing to process client's messages.
	 * @param client the client that sent the message.
	 * @param message the message sent by the client.
	 */
	void onMessageReceived(ConnectionInterface client, Message message);
}
