package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.rmi.RMIClientInterface;
import it.polimi.se2019.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerSkeletonInterface extends Remote {

	/**
	 * Register a new client that is connected to the server. This method is called remotely by the client.
	 * @param rmiClientInterface the RMI implementation of the client.
	 * @throws RemoteException
	 */
	void registerClient(RMIClientInterface rmiClientInterface) throws RemoteException;

	/**
	 * Receives a message from the client and handles it. This method is called remotely by the client.
	 * @param message the message received from the client.
	 * @throws RemoteException
	 */
	void receiveMessage(RMIClientInterface rmiClientInterface, Message message) throws RemoteException;
}
