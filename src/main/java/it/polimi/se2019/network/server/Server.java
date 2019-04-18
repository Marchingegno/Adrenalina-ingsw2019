package it.polimi.se2019.network.server;

import it.polimi.se2019.network.ConnectionInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.rmi.RMIServer;
import it.polimi.se2019.network.server.socket.SocketServer;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Starts the RMI server and the Socket server.
 * @author Desno365
 * @author MarcerAndrea
 */
public class Server {

	private ServerMessageHandler serverMessageHandler;
	private SocketServer socketServer;
	private RMIServer rmiServer;

	public static void main(String[] args) {
		Server server = new Server();
		server.startRMIServer();
		server.startSocketServer();
		server.closeServerIfRequested();
	}

	private Server() {
		serverMessageHandler = new ServerMessageHandler();
	}

	/**
	 * Send a message asynchronously.
	 * @param client the recipient of the message.
	 * @param message the message to send.
	 */
	public static void asyncSendMessage(ConnectionInterface client, Message message) {
		new Thread(() -> {
			try {
				client.processMessage(message);
			} catch (RemoteException e) {
				Utils.logError("Lost connection with the client.", e);
			}
		}, "CUSTOM: Server Message Sending").start();
	}

	/**
	 * Start the RMI server.
	 */
	public void startRMIServer() {
		try {
			rmiServer = new RMIServer(serverMessageHandler);
		} catch (RemoteException e) {
			Utils.logError("Failed to start RMI server.", e);
		}
	}

	/**
	 * Start the socket server.
	 */
	public void startSocketServer() {
		socketServer = new SocketServer(serverMessageHandler);
		socketServer.start();
	}

	/**
	 * Listen for a "close" command.
	 */
	public void closeServerIfRequested() {
		new Thread(() -> {
			String input = "";
			while (!input.equalsIgnoreCase("close")) {
				System.out.println("Type \"close\" to stop the server.");
				input = new Scanner(System.in).nextLine();
			}
			socketServer.close();
			rmiServer.close();
			System.exit(0);
		}, "CUSTOM: Input Listener").start();
	}

}
