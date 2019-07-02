package it.polimi.se2019.network.client;

import it.polimi.se2019.network.message.Message;

public interface MessageReceiverInterface {

	/**
	 * Receive and process the message sent by the server both by socket or by RMI.
	 *
	 * @param message the message received.
	 */
	void processMessage(Message message);

	/**
	 * Called when the connection with the server (by socket or by RMI) has failed.
	 */
	void failedConnection();

	/**
	 * Called when the connection with the server (by socket or by RMI) has been lost.
	 */
	void lostConnection();
}
