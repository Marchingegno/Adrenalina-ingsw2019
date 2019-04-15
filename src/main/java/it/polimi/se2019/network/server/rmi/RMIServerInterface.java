package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.client.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Contains all the methods called remotely by the client.
 */
public interface RMIServerInterface extends Remote {

	/**
	 * Register a new client that is connected to the server.
	 * @param client the RMI implementation of the client.
	 * @throws RemoteException
	 */
	void registerClient(ClientInterface client) throws RemoteException;

	/**
	 * Send a message to the Server.
	 * @param message the message to send.
	 * @throws RemoteException
	 */
	void sendMessage(ClientInterface client, Message message) throws RemoteException;

}
