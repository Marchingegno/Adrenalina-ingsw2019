package it.polimi.se2019.network.server;

import it.polimi.se2019.network.Message;
import it.polimi.se2019.network.client.RMIClientInterface;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface{

	public RMIServer() throws RemoteException {
		super();
	}

	@Override
	public void registerClient(RMIClientInterface client) throws RemoteException {

	}

	@Override
	public void sendMessage(Message message) throws RemoteException {

	}

	@Override
	public void startRMIServer() throws RemoteException {
		// Create RMI server.
		RMIServerInterface serverImplementation = new RMIServer();

		// Register server.
		System.setProperty("java.rmi.server.hostname", "localhost");
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.rebind("Server", serverImplementation);

		Utils.logInfo("RMI server is ready.");
	}
}
