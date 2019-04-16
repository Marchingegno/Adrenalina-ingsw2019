package it.polimi.se2019.network.server;

import it.polimi.se2019.network.client.ClientMessageReceiverInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.rmi.RMIServer;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;

public class Server {

	private MessageHandler messageHandler;

	public static void main(String[] args) {
		Server server = new Server();
		server.startRMIServer();
		server.startSocketServer();
	}


	private Server() {
		messageHandler = new MessageHandler();
	}


	/**
	 * Send a message asynchronously.
	 * @param client the recipient of the message.
	 * @param message the message to send.
	 */
	public static void asyncSendMessage(ClientMessageReceiverInterface client, Message message) {
		new Thread(() -> {
			try {
				client.processMessage(message);
			} catch (RemoteException e) {
				Utils.logError("Lost connection with the client.", e);
			}
		}).start();
	}

	/**
	 * Start the RMI server.
	 */
	public void startRMIServer() {
		try {
			new RMIServer(messageHandler);
		} catch (RemoteException e) {
			Utils.logError("Failed to start RMI server.", e);
		}
	}

	/**
	 * Start the socket server.
	 */
	public void startSocketServer() {
		// TODO start socket server and give him the messageHandler
	}

}
