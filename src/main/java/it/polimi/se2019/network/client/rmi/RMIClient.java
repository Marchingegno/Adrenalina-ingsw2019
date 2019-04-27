package it.polimi.se2019.network.client.rmi;

import it.polimi.se2019.network.client.ConnectionToServerInterface;
import it.polimi.se2019.network.client.MessageReceiverInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.rmi.RMIServerSkeletonInterface;
import it.polimi.se2019.utils.Utils;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class is the bridge between the server and the client.
 * The server calls methods of this class in order to communicate with the client.
 * And the client does the same in order to communicate with the server.
 * @author Desno365
 */
public class RMIClient implements ConnectionToServerInterface, RMIClientInterface {

	private RMIServerSkeletonInterface rmiServerSkeleton;
	private MessageReceiverInterface messageReceiver;
	private RMIClientInterface stub;


	/**
	 * Creates a new instance of a RMIClient and starts the connection with the server.
	 * @param messageReceiver the interface on which messages will be forwarded.
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public RMIClient(MessageReceiverInterface messageReceiver) throws RemoteException, NotBoundException {
		this.messageReceiver = messageReceiver;

		// Get Server remote object.
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);
		rmiServerSkeleton = (RMIServerSkeletonInterface) registry.lookup("Server");

		// Create stub from client.
		stub = (RMIClientInterface) UnicastRemoteObject.exportObject(this, 0);

		// Register client's stub to the server.
		rmiServerSkeleton.registerClient(stub);

		Utils.logInfo("Client remote object is ready.");
	}

	/**
	 * Sends a message to the server.
	 * @param message the message to send.
	 */
	@Override
	public void sendMessage(Message message) {
		try {
			rmiServerSkeleton.receiveMessage(stub, message);
		} catch (RemoteException e) {
			Utils.logInfo("Lost connection with the server.");
			messageReceiver.lostConnection();
		}
	}

	/**
	 * Called by the RMI server in order to send a message.
	 * @param message the message sent by the server.
	 * @throws RemoteException
	 */
	@Override
	public void receiveMessage(Message message) throws RemoteException {
		messageReceiver.processMessage(message);
	}
}
