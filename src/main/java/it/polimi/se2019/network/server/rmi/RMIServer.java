package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.client.rmi.RMIClientInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.ServerEventsListenerInterface;
import it.polimi.se2019.utils.ServerConfigParser;
import it.polimi.se2019.utils.Utils;

import java.io.Closeable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Implements the RMI server.
 * @author Desno365
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerSkeletonInterface, Closeable {

	private transient ServerEventsListenerInterface serverEventsListener;
	private transient Registry registry;
	private transient HashMap<RMIClientInterface, ServerClientRMI> connections = new HashMap<>();


	/**
	 * Creates a new instance of a RMIServer and start it.
	 * @param serverEventsListener the event listener to which all events are forwarded.
	 * @throws RemoteException
	 */
	public RMIServer(ServerEventsListenerInterface serverEventsListener) throws RemoteException {
		super();
		this.serverEventsListener = serverEventsListener;
		startRMIServer();
	}


	/**
	 * Registers a new client that is connected to the server. This method is called remotely by the client.
	 * @param rmiClientInterface the RMI implementation of the client.
	 * @throws RemoteException
	 */
	@Override
	public void registerClient(RMIClientInterface rmiClientInterface) throws RemoteException {
		ServerClientRMI newServerClientRMI = new ServerClientRMI(serverEventsListener, rmiClientInterface);
		connections.put(rmiClientInterface, newServerClientRMI);
		serverEventsListener.onClientConnection(newServerClientRMI);
		newServerClientRMI.startConnectionListener();
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
		new Thread(() -> serverEventsListener.onMessageReceived(serverClientRMI, message), "CUSTOM: RMI On Message Reception Processing").start();
	}

	/**
	 * Closes this stream and releases any system resources associated with it.
	 */
	@Override
	public void close() {
		try {
			if(registry != null) {
				registry.unbind("Server");
				UnicastRemoteObject.unexportObject(registry, true);
				Utils.logInfo("RMI server stopped.");
			}
		} catch (RemoteException | NotBoundException e) {
			Utils.logError("Error in RMIServer: close()", e);
		}
	}

	@Override
	public boolean equals(Object object) {
		return super.equals(object);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}


	/**
	 * Starts the RMI server and register it on the RMI registry,
	 * @throws RemoteException
	 */
	private void startRMIServer() throws RemoteException {
		// Register server.
		System.setProperty("java.rmi.server.hostname", "localhost");
		registry = LocateRegistry.createRegistry(ServerConfigParser.getRmiPort());
		registry.rebind("Server", this);

		Utils.logInfo("RMI server is ready.");
	}
}
