package it.polimi.se2019.network.server;

import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;

public class Server {

	public static void main(String[] args) {

		try {
			RMIServerInterface rmiServer = new RMIServer();
			rmiServer.startRMIServer();
		} catch (RemoteException e) {
			Utils.logError("Failed to start server.", e);
		}


		// TODO start socket server
	}
}
