package it.polimi.se2019.network.server;

import it.polimi.se2019.network.client.ClientMessageReceiverInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.network.message.NicknameMessage;
import it.polimi.se2019.network.server.rmi.RMIServer;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Server implements ServerMessageReceiverInterface {

	private static final int NICKNAME_MAX_LENGTH = 16;
	private static final int NICKNAME_MIN_LENGTH = 1;

	private ArrayList<ClientMessageReceiverInterface> clients;
	private Lobby lobby;


	public static void main(String[] args) {
		Server server = new Server();
		server.startRMIServer();
		server.startSocketServer();
	}


	private Server() {
		clients = new ArrayList<>();
		lobby = new Lobby();
	}


	/**
	 * Called when the client is registering himself on the server.
	 * @param client the implementation of the client.
	 */
	@Override
	public void onClientRegistration(ClientMessageReceiverInterface client) {
		clients.add(client);
		Utils.logInfo("Registered new client.");
		asyncSendMessage(client, new Message(MessageType.NICKNAME, MessageSubtype.REQUEST));
	}

	/**
	 * Called when receiving a message from the client.
	 * @param message the message received.
	 */
	@Override
	public void onMessageReceived(ClientMessageReceiverInterface client, Message message) {
		// TODO based on the message send it to the controller (or remoteview?)

		Utils.logInfo("The server received a message of type: " + message.getMessageType() + ", and subtype: " + message.getMessageSubtype() + ".");
		if (message.getMessageType() == MessageType.NICKNAME) {
			if (message.getMessageSubtype() == MessageSubtype.ANSWER) {
				// Remove spaces in the nickname and set max length.
				String nickname = ((NicknameMessage) message).getContent().replaceAll("\\s", "");
				int maxLength = (nickname.length() < NICKNAME_MAX_LENGTH) ? nickname.length() : NICKNAME_MAX_LENGTH;
				nickname = nickname.substring(0, maxLength);

				if(nickname.length() >= NICKNAME_MIN_LENGTH) {
					// Add the client to the lobby, waiting for a match to start.
					lobby.addWaitingClient(client, nickname);
				} else {
					// Nickname not valid (too short).
					Server.asyncSendMessage(client, new Message(MessageType.NICKNAME, MessageSubtype.ERROR));
				}
			}
		} else {
			Utils.logInfo("Message forwarded to the lobby.");
			lobby.onMessageReceived(client, message); // Forward the message to the Match class.
		}
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
			new RMIServer(this);
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
