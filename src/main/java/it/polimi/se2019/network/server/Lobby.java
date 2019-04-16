package it.polimi.se2019.network.server;

import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.network.message.NicknameMessage;
import it.polimi.se2019.utils.GameConstants;

import java.util.HashMap;

public class Lobby {

	private HashMap<ClientInterface, Match> playingClients;
	private HashMap<ClientInterface, String> waitingRoom;


	/**
	 * Create the lobby.
	 */
	public Lobby() {
		playingClients = new HashMap<>();
		waitingRoom = new HashMap<>();
	}


	/**
	 * Add a client to the waiting room.
	 * If the waiting room has reached the minimum number of players a timer starts and when it ends the game starts.
	 * If the waiting room has reached the maximum number of players the game starts immediately.
	 * @param client the client to add to the waiting room.
	 * @param nickname the nickname of the client.
	 */
	public void addWaitingClient(ClientInterface client, String nickname) {
		if(waitingRoom.containsValue(nickname)) {
			Server.asyncSendMessage(client, new Message(MessageType.NICKNAME, MessageSubtype.ERROR));
		} else {
			Server.asyncSendMessage(client, new NicknameMessage(nickname, MessageSubtype.OK));
			waitingRoom.put(client, nickname);
			checkIfWaitingRoomIsReady();
		}
		// TODO timer for waiting room
	}

	/**
	 * Check if the waiting room is ready to start, and if it is then it's started.
	 */
	private void checkIfWaitingRoomIsReady() {
		//if(waitingRoom.size() == GameConstants.MAX_PLAYERS) {
		if(waitingRoom.size() == GameConstants.MIN_PLAYERS) { // TODO using MIN_PLAYERS for easier testing
			Match match = new Match(waitingRoom);
			for(ClientInterface client : waitingRoom.keySet())
				playingClients.put(client, match);
			waitingRoom.clear();
			match.startMatch();
		}
	}
}
