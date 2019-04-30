package it.polimi.se2019.network.server;

import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.ServerConfigParser;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.VirtualView;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Lobby {

	private HashMap<ConnectionToClientInterface, Match> playingClients;
	private HashMap<ConnectionToClientInterface, String> waitingRoom;
	private long timerDelayForMatchStart;
	private long timeTimerStart;
	private Timer timer;


	/**
	 * Creates the lobby with the specified delay for starting a match.
	 */
	public Lobby() {
		playingClients = new HashMap<>();
		waitingRoom = new HashMap<>();
		timerDelayForMatchStart = ServerConfigParser.getWaitingTimeInLobbyMs();
	}


	/**
	 * Add a client to the waiting room.
	 * If the waiting room has reached the minimum number of players a timer starts and when it ends the game starts.
	 * If the waiting room has reached the maximum number of players the game starts immediately.
	 * @param client the client to add to the waiting room.
	 * @param nickname the nickname of the client.
	 */
	public void addWaitingClient(ConnectionToClientInterface client, String nickname) {
		if(waitingRoom.containsValue(nickname)) {
			client.sendMessage(new Message(MessageType.NICKNAME, MessageSubtype.ERROR));
		} else {
			client.sendMessage(new NicknameMessage(nickname, MessageSubtype.OK));
			waitingRoom.put(client, nickname);
			checkIfWaitingRoomIsReady(client);
		}
	}

	/**
	 * Removes the client from the waiting room if present.
	 * @param client the client to remove from the waiting room.
	 */
	public void removeWaitingClient(ConnectionToClientInterface client) {
		waitingRoom.remove(client);
		checkIfWaitingRoomIsReady(null);
	}

	/**
	 * Check if the client that is disconnected was a participant of a match.
	 * Suspend the client if the match started or dismantle the match if the match isn't started.
	 * @param client the client that lost the connection.
	 */
	public void clientDisconnectedFromMatch(ConnectionToClientInterface client) {
		Match match = getMatchOfClient(client);
		if(match != null) { // The client was a participant of a match.
			if(match.isMatchStarted()) { // The client was a participant in an already started match. Forward the message to the virtual view.
				VirtualView virtualView = match.getVirtualViewOfClient(client);
				if(virtualView == null)
					Utils.logError("The lobby thinks the client is in a match but he actually isn't.", new IllegalStateException());
				else
					virtualView.onClientDisconnected();
			} else { // The client was a participant of a match that isn't started yet. Dismantle the match.
				Map<ConnectionToClientInterface, String> participants = match.getParticipants();
				participants.remove(client); // Remove the disconnected client from this map.

				// Remove all the participants of the match from the playingClients hash map.
				for(Map.Entry<ConnectionToClientInterface, String> participant : participants.entrySet())
					playingClients.remove(participant.getKey());

				// Add the participants of the dismantled match to the waiting room.
				for(Map.Entry<ConnectionToClientInterface, String> participant : participants.entrySet()) {
					waitingRoom.put(participant.getKey(), participant.getValue());
					checkIfWaitingRoomIsReady(participant.getKey());
				}
			}
		}
	}

	/**
	 * Returns the Match that the client is in, or null if it isn't in any Match.
	 * @param client the client.
	 * @return the match in which the client is playing.
	 */
	public Match getMatchOfClient(ConnectionToClientInterface client) {
		return playingClients.get(client);
	}

	/**
	 * Check if the waiting room has reached the minimum number of players (and schedule the timer for the match to start).
	 * Check if the waiting room has reached the maximum number of players (and start the match immediately).
	 */
	private void checkIfWaitingRoomIsReady(ConnectionToClientInterface newestClient) {
		// Send a message to all the clients in the waiting room.
		sendWaitingPlayersMessage();

		// Logic for starting the match or the timer.
		if(waitingRoom.size() == GameConstants.MAX_PLAYERS) {
			// Reached the maximum number of players. Cancel the timer and start the match.
			startMatchInWaitingRoom();
		} else if(waitingRoom.size() == GameConstants.MIN_PLAYERS) {
			// Reached the minimum number of players. Start a timer for starting the match if not already started.
			if(timer == null) {
				startTimerForMatchStart();
				sendTimerStartedMessage();
			}
		} else if(waitingRoom.size() < GameConstants.MIN_PLAYERS) {
			// If a timer has been started, cancels it since now the number of players is less than the minimum.
			if(timer != null) {
				cancelTimerForMatchStart();
				sendTimerCanceledMessage();
			}
		} else {
			// Number of players between the minimum and the maximum. Send to the new client the remaining time for starting the match.
			if(timer != null && newestClient != null) {
				long timePassedAfterTimerStart = System.currentTimeMillis() - timeTimerStart;
				long timeRemainingInTimer = timerDelayForMatchStart - timePassedAfterTimerStart;
				newestClient.sendMessage(new TimerForStartMessage(timeRemainingInTimer, MessageSubtype.INFO));
			}
		}
	}

	/**
	 * Start the match with the clients in the waiting room and cancels the timer.
	 */
	private void startMatchInWaitingRoom() {
		// Stop the timer if it is running, used for example when reaching the maximum number of players.
		if(timer != null)
			cancelTimerForMatchStart();

		// Start a new match.
		Match match = new Match(waitingRoom);
		for(ConnectionToClientInterface client : waitingRoom.keySet())
			playingClients.put(client, match);
		waitingRoom.clear();
		match.requestMatchConfig();
	}

	private void sendWaitingPlayersMessage() {
		if(waitingRoom.size() > 0) {
			// Create a String of all the nicknames of the players.
			StringBuilder stringBuilder = new StringBuilder();
			for (String nickname : waitingRoom.values()) {
				if(stringBuilder.length() != 0)
					stringBuilder.append(", ");
				stringBuilder.append(nickname);
			}

			// Send the message with all nicknames.
			for (ConnectionToClientInterface client : waitingRoom.keySet()) {
				client.sendMessage(new WaitingPlayersMessage(stringBuilder.toString()));
			}
		}
	}

	private void startTimerForMatchStart() {
		timeTimerStart = System.currentTimeMillis();
		final Lobby lobby = this;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Utils.logInfo("Timer ended. Starting the match...");
				lobby.startMatchInWaitingRoom();
			}
		}, timerDelayForMatchStart);
		Utils.logInfo("Scheduled a timer for starting the match of " + timerDelayForMatchStart + " milliseconds.");
	}

	private void cancelTimerForMatchStart() {
		timer.cancel();
		timer = null;
	}

	private void sendTimerStartedMessage() {
		for(ConnectionToClientInterface client : waitingRoom.keySet())
			client.sendMessage(new TimerForStartMessage(timerDelayForMatchStart, MessageSubtype.INFO));
	}

	private void sendTimerCanceledMessage() {
		for(ConnectionToClientInterface client : waitingRoom.keySet())
			client.sendMessage(new Message(MessageType.TIMER_FOR_START, MessageSubtype.ERROR));
	}

}
