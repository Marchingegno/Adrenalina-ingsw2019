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
		server.startRMIServerAsynchronously();
		server.startSocketServerAsynchronously();
		server.closeServerIfRequestedAsynchronously();
	}

	/**
	 * Starts the RMI server.
	 * Doesn't stop the calling thread.
	 */
	private void startRMIServerAsynchronously() {
		try {
			rmiServer = new RMIServer(serverEventsListener);
		} catch (RemoteException e) {
			Utils.logError("Failed to start RMI server.", e);
		}
	}

	/**
	 * Starts the socket server.
	 * Doesn't stop the calling thread.
	 */
	private void startSocketServerAsynchronously() {
		try {
			socketServer = new SocketServer(serverEventsListener);
		} catch (IOException e) {
			Utils.logError("Failed to start Socket server.", e);
		}
	}

	/**
	 * Listens for a "close" command.
	 * Doesn't stop the calling thread.
	 */
	private void closeServerIfRequestedAsynchronously() {
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
