package it.polimi.se2019.network.server;

import it.polimi.se2019.network.message.Message;

public interface ServerEventsListenerInterface {

	/**
	 * Called when the client start the connection with the server.
	 * @param client the client starting the connection.
	 */
	void onClientConnection(AbstractConnectionToClient client);

	/**
	 * Called when the client lose the connection with the server.
	 * @param client the client that lost the connection.
	 */
	void onConnectionLost(AbstractConnectionToClient client);

	/**
	 * Called when receiving a message from the client.
	 * This method will process messages of type GAME_CONFIG or NICKNAME. Other messages are forwarded to the VirtualView.
	 * @param client the client that sent the message.
	 * @param message the message received.
	 */
	void onMessageReceived(AbstractConnectionToClient client, Message message);
}
