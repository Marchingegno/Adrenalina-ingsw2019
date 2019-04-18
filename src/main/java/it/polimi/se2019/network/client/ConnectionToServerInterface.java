package it.polimi.se2019.network.client;

import it.polimi.se2019.network.message.Message;

/**
 * Implemented by classes which want to connect to the server.
 */
public interface ConnectionToServerInterface {

	/**
	 * Sends a message to the server.
	 * @param message the message to send.
	 */
	void sendMessage(Message message);

}
