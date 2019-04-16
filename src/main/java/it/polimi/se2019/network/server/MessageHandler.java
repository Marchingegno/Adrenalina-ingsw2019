package it.polimi.se2019.network.server;

import it.polimi.se2019.network.client.ClientMessageReceiverInterface;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;

public class MessageHandler implements ServerMessageReceiverInterface {

	private static final int NICKNAME_MAX_LENGTH = 16;
	private static final int NICKNAME_MIN_LENGTH = 1;

	private ArrayList<ClientMessageReceiverInterface> clients;
	private Lobby lobby;


	public MessageHandler() {
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
		Server.asyncSendMessage(client, new Message(MessageType.NICKNAME, MessageSubtype.REQUEST));
	}

	/**
	 * Called when receiving a message from the client.
	 * @param message the message received.
	 */
	@Override
	public void onMessageReceived(ClientMessageReceiverInterface client, Message message) {
		Utils.logInfo("The server received a message of type: " + message.getMessageType() + ", and subtype: " + message.getMessageSubtype() + ".");

		// Don't process message of not registered clients.
		if(!clients.contains(client))
			return;

		// TODO based on the message send it to the controller (or remoteview?)

		switch (message.getMessageType()) {
			case NICKNAME:
				if (message.getMessageSubtype() == MessageSubtype.ANSWER)
					nicknameLogic(client, message);
				break;
			case GAME_CONFIG:
				Match match = lobby.getMatchOfClient(client);
				if(match != null && message.getMessageSubtype() == MessageSubtype.ANSWER)
					gameConfigLogic(client, message);
				break;

		}
	}

	private void nicknameLogic(ClientMessageReceiverInterface client, Message message) {
		// Remove spaces in the nickname and set max length.
		String nickname = ((NicknameMessage) message).getContent().replaceAll("\\s", "");
		int maxLength = (nickname.length() < NICKNAME_MAX_LENGTH) ? nickname.length() : NICKNAME_MAX_LENGTH;
		nickname = nickname.substring(0, maxLength);

		// Check if nickname is not valid (too short).
		if(nickname.length() >= NICKNAME_MIN_LENGTH) {
			// Add the client to the lobby, waiting for a match to start.
			lobby.addWaitingClient(client, nickname);
		} else {
			Server.asyncSendMessage(client, new Message(MessageType.NICKNAME, MessageSubtype.ERROR));
		}
	}

	/**
	 * Implement the logic to handle a GameConfigMessage.
	 * @param client the client that send the GameConfigMessage.
	 * @param message the message.
	 */
	private void gameConfigLogic(ClientMessageReceiverInterface client, Message message) {
		// TODO should interact with match
		
		numberOfAnswers++;
		GameConfigMessage gameConfigMessage = (GameConfigMessage) message;
		int skulls = gameConfigMessage.getSkulls();
		if(!skullsChosen.containsKey(client))
			skullsChosen.put(client, skulls);
		int mapIndex = gameConfigMessage.getMapIndex();
		if(!mapChoosen.containsKey(client))
			mapChoosen.put(client, mapIndex);
		if(numberOfAnswers >= numberOfPartecipants)
			startMatch();
	}

}
