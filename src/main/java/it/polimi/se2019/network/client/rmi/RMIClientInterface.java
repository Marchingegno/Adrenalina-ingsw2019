package it.polimi.se2019.network.client.rmi;

import it.polimi.se2019.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote interface used by the RMI server to communicate with the client.
 */
public interface RMIClientInterface extends Remote {

	/**
	 * Called by the RMI server in order to send a message.
	 * @param message the message sent by the server.
	 * @throws RemoteException
	 */
	void receiveMessage(Message message) throws RemoteException;

	void connectionListenerSubject() throws RemoteException, InterruptedException;
}
