package it.polimi.se2019.network.server;

import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.VirtualView;

import java.util.ArrayList;

/**
 * Listen to events produced by a client connected to the server:
 * - Connection started: processed in onClientConnection;
 * - Connection lost: processed in onConnectionLost;
 * - Message received: processed in onMessageReceived.
 */
public class ServerEventsListener implements ServerEventsListenerInterface {

	private static final int NICKNAME_MAX_LENGTH = 16;
	private static final int NICKNAME_MIN_LENGTH = 1;

	private Lobby lobby = new Lobby();
	private ArrayList<AbstractConnectionToClient> connectedClients = new ArrayList<>();


	/**
	 * Called when the client start the connection with the server.
	 * @param client the client starting the connection.
	 */
	@Override
	public synchronized void onClientConnection(AbstractConnectionToClient client) {
		connectedClients.add(client);
		Utils.logInfo("ServerEventsListener -> onClientConnection(): started connection with client \"" + client.hashCode() + "\". There are " + connectedClients.size() + " clients registered.");
		client.sendMessage(new Message(MessageType.NICKNAME, MessageSubtype.REQUEST));
	}

	/**
	 * Called when the client lose the connection with the server.
	 * @param client the client that lost the connection.
	 */
	@Override
	public synchronized void onConnectionLost(AbstractConnectionToClient client) {
		// Remove client from the connected clients list.
		connectedClients.remove(client);
		Utils.logInfo("ServerEventsListener -> onConnectionLost(): lost connection with client \"" + client.hashCode() + "\". There are " + connectedClients.size() + " clients registered.");

		// Remove the client from the waiting room if present.
		lobby.removeWaitingClient(client);

		// Report the disconnection of the client to the match.
		lobby.clientDisconnectedFromMatch(client);
	}

	/**
	 * Called when receiving a message from the client.
	 * This method will process messages of type GAME_CONFIG or NICKNAME. Other messages are forwarded to the VirtualView.
	 * @param message the message received.
	 */
	@Override
	public synchronized void onMessageReceived(AbstractConnectionToClient client, Message message) {
		// Discard messages of not registered clients.
		if(!connectedClients.contains(client))
			return;

		lobby.dismantleFinishedMatches();

		Utils.logInfo("ServerEventsListener -> onMessageReceived(): received a message from \"" + client.hashCode() + "\" of type: " + message.getMessageType() + ", and subtype: " + message.getMessageSubtype() + ".");

		if(client.isNicknameSet()) {
			if(message.getMessageType() == MessageType.GAME_CONFIG && message.getMessageSubtype() == MessageSubtype.ANSWER)
				gameConfigLogic(client, message);
			else
				forwardMessageToVirtualView(client, message);
		} else {
			if(message.getMessageType() == MessageType.NICKNAME && message.getMessageSubtype() == MessageSubtype.ANSWER)
				nicknameLogic(client, message);
		}
	}

	/**
	 * Closes all the connections with the clients.
	 */
	public void closeAllConnections() {
		for(AbstractConnectionToClient client : connectedClients) {
			client.closeConnectionWithClient();
		}
	}


	/**
	 * Implement the logic to handle a NicknameMessage.
	 * @param client the client that sent the NicknameMessage.
	 * @param message the message.
	 */
	private void nicknameLogic(AbstractConnectionToClient client, Message message) {
		String nickname = optimizeNickname(((NicknameMessage) message).getContent());
		if(isNicknameValid(nickname)) {
			lobby.registerClient(client, nickname); // Add the client to the lobby, waiting for a match to start.
		} else {
			Utils.logInfo("\tNickname not valid, asking a new nickname.");
			client.sendMessage(new Message(MessageType.NICKNAME, MessageSubtype.ERROR));
		}
	}

	/**
	 * Returns the nickname with spaces removed and cut at max length if bigger.
	 * @param nickname the nickname to optimize.
	 * @return the optimized nickname.
	 */
	private String optimizeNickname(String nickname) {
		nickname = nickname.replaceAll("\\s", "");
		int maxLength = (nickname.length() < NICKNAME_MAX_LENGTH) ? nickname.length() : NICKNAME_MAX_LENGTH;
		return nickname.substring(0, maxLength);
	}

	/**
	 * Check if nickname is valid (not too short).
	 * @param nickname the nickname to check.
	 * @return true if valid.
	 */
	private boolean isNicknameValid(String nickname) {
		return nickname.length() >= NICKNAME_MIN_LENGTH;
	}

	/**
	 * Implement the logic to handle a GameConfigMessage.
	 * @param client the client that sent the GameConfigMessage.
	 * @param message the message.
	 */
	private void gameConfigLogic(AbstractConnectionToClient client, Message message) {
		Match match = lobby.getMatchOfClient(client);
		if(match != null) {
			GameConfigMessage gameConfigMessage = (GameConfigMessage) message;
			match.addConfigVote(client, gameConfigMessage.getSkulls(), gameConfigMessage.getMapIndex());
		}
	}

	/**
	 * Forwards the message of the client to its VirtualView (if present).
	 * @param client the client that sent the message.
	 * @param message the message.
	 */
	private void forwardMessageToVirtualView(AbstractConnectionToClient client, Message message) {
		Match match = lobby.getMatchOfClient(client);
		if(match != null) { // If the client is in a match.
			VirtualView virtualView = match.getVirtualViewOfClient(client);
			if(virtualView != null) {
				Utils.logInfo("\tForwarding the message to the VirtualView.");
				virtualView.onMessageReceived(message);
			}
		} else {
			Utils.logError("Client has no match", new NullPointerException());
		}
	}

}
