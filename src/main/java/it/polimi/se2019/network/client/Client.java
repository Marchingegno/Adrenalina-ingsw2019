package it.polimi.se2019.network.client;

import it.polimi.se2019.network.client.rmi.RMIClient;
import it.polimi.se2019.network.client.socket.ClientSocket;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.client.CLIView;
import it.polimi.se2019.view.client.GUIView;
import it.polimi.se2019.view.client.RemoteView;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;


/**
 * Implements the Client.
 * @author Desno365
 * @author MarcerAndrea
 */
public class Client {

	public static void main(String[] args) {
		// Start with CLI and ask if the user wants to use CLI or GUI.
		CLIView cliView = new CLIView();
		boolean isGUI = cliView.askForGUI();

		// Start GUI if requested.
		RemoteView remoteView;
		if(isGUI)
			remoteView = new GUIView();
		else
			remoteView = cliView;

		// Ask which connection to use and start it.
		remoteView.askForConnectionAndStartIt();
	}


	/**
	 * Start a connection with the server, using RMI.
	 */
	public static void startConnectionWithRMI(RemoteView remoteView) {
		try {
			ConnectionToServerInterface clientMessageSender = new RMIClient(remoteView);
			remoteView.setConnectionToServer(clientMessageSender);
		} catch (RemoteException | NotBoundException e) {
			Utils.logError("Failed to connect to the server.", e);
			remoteView.failedConnectionToServer();
		}
	}

	/**
	 * Start a connection with the server, using socket.
	 */
	public static void startConnectionWithSocket(RemoteView remoteView) {
		ConnectionToServerInterface clientMessageSender = new ClientSocket(remoteView);
		remoteView.setConnectionToServer(clientMessageSender);
	}

	/**
	 * Terminate the client.
	 */
	public static void terminateClient() {
		System.exit(0);
	}
}
