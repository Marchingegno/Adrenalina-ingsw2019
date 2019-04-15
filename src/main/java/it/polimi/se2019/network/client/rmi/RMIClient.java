package it.polimi.se2019.network.client.rmi;

import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.client.ConnectionInterface;
import it.polimi.se2019.network.server.rmi.RMIServerInterface;
import it.polimi.se2019.utils.Utils;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient implements ConnectionInterface {

	private RMIServerInterface rmiServer;
	private ClientInterface stub;

	public void startRMIClient(ClientInterface client) throws RemoteException, NotBoundException {
		// Get Server remote object.
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);
		rmiServer = (RMIServerInterface) registry.lookup("Server");

		// Create stub from client.
		stub = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);

		Utils.logInfo("Client remote object is ready.");
	}

	@Override
	public void registerClient() throws RemoteException {
		rmiServer.registerClient(stub); // Register client's stub to the server.
	}

	@Override
	public void sendMessage(Message message) throws RemoteException {
		rmiServer.sendMessage(message); // Send message to the server.
	}
}
