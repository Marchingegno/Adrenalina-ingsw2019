package it.polimi.se2019.network.server;

import it.polimi.se2019.network.ConnectionInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.rmi.RMIServer;
import it.polimi.se2019.network.server.socket.SocketServer;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;

/**
 * Starts the RMI server and the Socket server.
 * @author Desno365
 * @author MarcerAndrea
 */
public class Server {

	private ServerMessageHandler serverMessageHandler;

	public static void main(String[] args) {
		Server server = new Server();
		server.startRMIServer();
		server.startSocketServer();
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
			new RMIServer(serverMessageHandler);
		} catch (RemoteException e) {
			Utils.logError("Failed to start RMI server.", e);
		}
	}

	/**
	 * Start the socket server.
	 */
	public void startSocketServer() {
		(new SocketServer(serverMessageHandler)).start();
	}

}
