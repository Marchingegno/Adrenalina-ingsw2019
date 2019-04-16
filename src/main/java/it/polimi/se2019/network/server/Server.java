package it.polimi.se2019.network.server;

import it.polimi.se2019.network.message.*;
import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.server.rmi.RMIServer;
import it.polimi.se2019.network.server.rmi.RMIServerInterface;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server implements ServerReceiverInterface {

	private static final int NICKNAME_MAX_LENGTH = 16;

	private RMIServerInterface rmiServer;
	private ArrayList<ClientInterface> clients;
	private HashMap<ClientInterface, String> clientNickanames;


	public static void main(String[] args) {
		Server server = new Server();
		server.startRMIServer();
		server.startSocketServer();
	}


	private Server() {
		clients = new ArrayList<>();
		clientNickanames = new HashMap<>();
	}


	/**
	 * Called when the client is registering himself on the server.
	 * @param client the implementation of the client.
	 */
	@Override
	public void onRegisterClient(ClientInterface client) {
		// TODO wait for other clients and start the game when ready
		clients.add(client);
		Utils.logInfo("Registered new client.");
		asyncSendMessage(client, new Message(MessageType.NICKNAME, MessageSubtype.REQUEST));
	}

	/**
	 * Called when receiving a message from the client.
	 * @param message the message received.
	 */
	@Override
	public void onReceiveMessage(ClientInterface client, Message message) {
		// TODO based on the message send it to the controller (or remoteview?)

		Utils.logInfo("Received message of type: " + message.getMessageType() + ", and subtype: " + message.getMessageSubtype() + ".");
		switch (message.getMessageType()) {
			case NICKNAME:
				if(message.getMessageSubtype() == MessageSubtype.ANSWER) {
					String nickname = ((NicknameMessage) message).getContent().replaceAll("\\s","");
					int maxLength = (nickname.length() < NICKNAME_MAX_LENGTH) ? nickname.length() : NICKNAME_MAX_LENGTH;
					nickname = nickname.substring(0, maxLength);
					clientNickanames.put(client, nickname); // TODO check if nickname isn't already used
					asyncSendMessage(client, new NicknameMessage(nickname, MessageSubtype.OK));
				}
				break;

			default:
				break;
		}
	}

	/**
	 * Send a message asynchronously.
	 * @param client the recipient of the message.
	 * @param message the message to send.
	 */
	private void asyncSendMessage(ClientInterface client, Message message) {
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
			rmiServer = new RMIServer(this);
		} catch (RemoteException e) {
			Utils.logError("Failed to start RMI server.", e);
		}
	}

	/**
	 * Start the socket server.
	 */
	public void startSocketServer() {
		// TODO
	}

}
