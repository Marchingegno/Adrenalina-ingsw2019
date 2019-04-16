package it.polimi.se2019.network.client;

import it.polimi.se2019.network.message.Message;

import java.rmi.RemoteException;

/**
 * Represents the possible interactions of the client with the server, both with RMI or socket.
 * Basically this class represents what the client can do with the server.
 */
public interface ClientMessageSenderInterface {

	/**
	 * Register the client on the server.
	 */
	void registerClient() throws RemoteException;

	/**
	 * Send a message to the server.
	 * @param message the message to send.
	 * @throws RemoteException
	 */
	void sendMessage(Message message) throws RemoteException;
}
