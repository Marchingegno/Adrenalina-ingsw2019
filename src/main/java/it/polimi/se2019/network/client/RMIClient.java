package it.polimi.se2019.network.client;

import it.polimi.se2019.network.Message;
import it.polimi.se2019.network.server.RMIServerInterface;
import it.polimi.se2019.utils.Utils;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient implements RMIClientInterface {

	@Override
	public void startRMIClient() throws RemoteException, NotBoundException {
		// Create RMI client.
		RMIClientInterface client = new RMIClient();

		// Get Server remote object.
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);
		RMIServerInterface server = (RMIServerInterface) registry.lookup("Server");

		// Register client to the server.
		server.registerClient(client);

		// Send a message
		server.sendMessage(new Message("Test"));

		Utils.logInfo("Chat Remote Object is ready:");
	}
}
