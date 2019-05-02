package it.polimi.se2019.network.server;

import it.polimi.se2019.network.server.rmi.RMIServer;
import it.polimi.se2019.network.server.socket.SocketServer;
import it.polimi.se2019.utils.Utils;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Starts the RMI server and the Socket server.
 * @author Desno365
 * @author MarcerAndrea
 */
public class Server {

	private ServerEventsListenerInterface serverEventsListener = new ServerEventsListener();
	private SocketServer socketServer;
	private RMIServer rmiServer;

	public static void main(String[] args) {
		Server server = new Server();
		server.startRMIServer();
		server.startSocketServer();
		server.closeServerIfRequested();
	}

	/**
	 * Start the RMI server.
	 */
	private void startRMIServer() {
		try {
			rmiServer = new RMIServer(serverEventsListener);
		} catch (RemoteException e) {
			Utils.logError("Failed to start RMI server.", e);
		}
	}

	/**
	 * Start the socket server.
	 */
	private void startSocketServer() {
		try {
			socketServer = new SocketServer(serverEventsListener);
		} catch (IOException e) {
			Utils.logError("Failed to start Socket server.", e);
		}
	}

	/**
	 * Listen for a "close" command.
	 */
	private void closeServerIfRequested() {
		new Thread(() -> {
			String input = "";
			while (!input.equalsIgnoreCase("close")) {
				Utils.printLine("Type \"close\" to stop the server.");
				input = new Scanner(System.in).nextLine();
			}
			socketServer.close();
			rmiServer.close();
			System.exit(0);
		}, "CUSTOM: Input Listener").start();
	}

}
