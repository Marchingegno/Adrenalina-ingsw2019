package it.polimi.se2019.network.client;

import it.polimi.se2019.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents the methods that can be called by the server.
 * Basically this class represents what the server can do with the client.
 */
public interface ClientMessageReceiverInterface extends Remote {

	/**
	 * Receive and process the message sent by the server both by socket or by RMI.
	 * @param message the message received.
	 */
	void processMessage(Message message) throws RemoteException;
}
