package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.rmi.RMIClientInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.ServerMessageHandler;
import it.polimi.se2019.utils.Utils;

import java.io.Closeable;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Implements the RMI server
 * @author Desno365
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerSkeletonInterface, Closeable {

	private transient ServerMessageHandler serverMessageHandler;
	private transient Registry registry;
	private transient HashMap<RMIClientInterface, ServerClientRMI> connections;

	/**
	 * Create a new instance of a RMIServer and start it.
	 * @param serverMessageHandler the MessageHandler to which all messages are sent
	 * @throws RemoteException
	 */
	public RMIServer(ServerMessageHandler serverMessageHandler) throws RemoteException {
		super();
		this.serverMessageHandler = serverMessageHandler;
		connections = new HashMap<>();
		startRMIServer();
	}

	/**
	 * Register a new client that is connected to the server. This method is called remotely by the client.
	 * @param rmiClientInterface the RMI implementation of the client.
	 * @throws RemoteException
	 */
	@Override
	public void registerClient(RMIClientInterface rmiClientInterface) throws RemoteException {
		ServerClientRMI newServerClientRMI = new ServerClientRMI(serverMessageHandler, rmiClientInterface);
		connections.put(rmiClientInterface, newServerClientRMI);
		serverMessageHandler.onClientRegistration(newServerClientRMI);
	}

	/**
	 * Receives a message from the client and handles it. This method is called remotely by the client.
	 * @param message the message received from the client.
	 * @throws RemoteException
	 */
	@Override
	public void receiveMessage(RMIClientInterface rmiClientInterface, Message message) throws RemoteException {
		final ServerClientRMI serverClientRMI = connections.get(rmiClientInterface);

		// Process the message in a thread so the thread of the client isn't put in wait.
		new Thread(() -> {
			serverMessageHandler.onMessageReceived(serverClientRMI, message);
		}, "CUSTOM: RMI Message Reception").start();
	}

	/**
	 * Closes this stream and releases any system resources associated with it.
	 */
	@Override
	public void close() {
		try {
			if(registry != null)
				UnicastRemoteObject.unexportObject(registry, true);
		} catch (NoSuchObjectException e) {
			Utils.logError("Error in RMIServer: close()", e);
		}
	}

	/**
	 * Start the RMI server and register it on the RMI registry,
	 * @throws RemoteException
	 */
	private void startRMIServer() throws RemoteException {
		// Register server.
		System.setProperty("java.rmi.server.hostname", "localhost");
		registry = LocateRegistry.createRegistry(1099);
		registry.rebind("Server", this);

		Utils.logInfo("RMI server is ready.");
	}
}
