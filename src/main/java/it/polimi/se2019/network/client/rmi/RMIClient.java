package it.polimi.se2019.network.client.rmi;

import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.client.ClientMessageReceiverInterface;
import it.polimi.se2019.network.client.ClientMessageSenderInterface;
import it.polimi.se2019.network.server.rmi.RMIServer;
import it.polimi.se2019.network.server.rmi.RMIServerSkeletonInterface;
import it.polimi.se2019.utils.Utils;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient implements ClientMessageSenderInterface {

	private RMIServerSkeletonInterface rmiServerSkeleton;
	private ClientMessageReceiverInterface stub;


	/**
	 * Create a new instance of a RMI client and start the connection with the server.
	 * @param client the client on which messages will be forwarded.
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public RMIClient(ClientMessageReceiverInterface client) throws RemoteException, NotBoundException {
		// Get Server remote object.
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);
		rmiServerSkeleton = (RMIServerSkeletonInterface) registry.lookup("Server");

		// Create stub from client.
		stub = (ClientMessageReceiverInterface) UnicastRemoteObject.exportObject(client, 0);

		Utils.logInfo("Client remote object is ready.");
	}


	/**
	 * Register the client on the server.
	 */
	@Override
	public void registerClient() throws RemoteException {
		rmiServerSkeleton.registerClient(stub); // Register client's stub to the server.
	}

	/**
	 * Send a message to the server.
	 * @param message the message to send.
	 * @throws RemoteException
	 */
	@Override
	public void sendMessage(Message message) throws RemoteException {
		rmiServerSkeleton.sendMessage(stub, message); // Send message to the server.
	}
}
