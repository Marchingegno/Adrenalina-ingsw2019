package it.polimi.se2019.network.client;

import it.polimi.se2019.network.client.rmi.RMIClient;
import it.polimi.se2019.network.client.socket.ClientSocket;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.client.CLIView;
import it.polimi.se2019.view.client.GUIView;
import it.polimi.se2019.view.client.RemoteView;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


/**
 * Implements the Client.
 * @author Desno365
 * @author MarcerAndrea
 */
public class Client {

	private ConnectionToServerInterface clientMessageSender;
	private RemoteView view;


	public static void main(String[] args) {
		Client client;

		//TEMP
		Scanner scanner = new Scanner(System.in);

		System.out.println("You want CLI? [true/false]");
		boolean isCLI = Boolean.parseBoolean(scanner.nextLine()); // TODO if user requested CLI use CLI otherwise GUI
		RemoteView remoteView;
		if(isCLI)
			remoteView = new CLIView();
		else
			remoteView = new GUIView();
		client = new Client(remoteView);


		System.out.println("You want RMI? [true/false]");
		boolean isRMI = Boolean.parseBoolean(scanner.nextLine()); // TODO if user requested RMI start RMI otherwise socket
		if(isRMI)
			client.startConnectionWithRMI(remoteView);
		else
			client.startConnectionWithSocket(remoteView);
	}


	/**
	 * Create a new client and associate it with the view.
	 * @param view the view to be associated with the client.
	 */
	private Client(RemoteView view) {
		this.view = view;
	}


	/**
	 * Start a connection with the server, using RMI.
	 */
	public void startConnectionWithRMI(MessageReceiverInterface messageReceiver) {
		try {
			clientMessageSender = new RMIClient(messageReceiver);
			view.setConnectionToServer(clientMessageSender);
		} catch (RemoteException | NotBoundException e) {
			Utils.logError("Failed to connect to the server.", e);
			view.failedConnectionToServer();
		}
	}

	/**
	 * Start a connection with the server, using socket.
	 */
	public void startConnectionWithSocket(MessageReceiverInterface messageReceiver) {
		clientMessageSender = new ClientSocket(messageReceiver);
		view.setConnectionToServer(clientMessageSender);
	}

	public static void terminateClient() {
		System.exit(0);
	}
}
