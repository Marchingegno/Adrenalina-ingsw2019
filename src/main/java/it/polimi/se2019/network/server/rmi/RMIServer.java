package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.ClientMessageReceiverInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.ServerMessageReceiverInterface;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIServerSkeletonInterface {

	private transient ServerMessageReceiverInterface server;


	/**
	 * Create a new instance of a RMIServer and start it.
	 * @param server the server on which messages will be forwarded.
	 * @throws RemoteException
	 */
	public RMIServer(ServerMessageReceiverInterface server) throws RemoteException {
		super();
		this.server = server;
		startRMIServer();
	}


	/**
	 * Register a new client that is connected to the server. This method is called remotely by the client.
	 * @param client the RMI implementation of the client.
	 * @throws RemoteException
	 */
	@Override
	public void registerClient(ClientMessageReceiverInterface client) throws RemoteException {
		server.onClientRegistration(client);
	}

	/**
	 * Send a message to the Server. This method is called remotely by the client.
	 * @param message the message to send.
	 * @throws RemoteException
	 */
	@Override
	public void sendMessage(ClientMessageReceiverInterface client, Message message) throws RemoteException {
		server.onMessageReceived(client, message);
	}

	/**
	 * Start the RMI server and register it on the RMI registry,
	 * @throws RemoteException
	 */
	private void startRMIServer() throws RemoteException {
		// Register server.
		System.setProperty("java.rmi.server.hostname", "localhost");
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.rebind("Server", this);

		Utils.logInfo("RMI server is ready.");
	}
}
