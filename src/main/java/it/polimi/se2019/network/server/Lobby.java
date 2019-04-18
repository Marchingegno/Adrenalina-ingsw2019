package it.polimi.se2019.network.server;

import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Lobby {

	private HashMap<ClientInterface, Match> playingClients;
	private HashMap<ClientInterface, String> waitingRoom;
	private long timerDelayForMatchStart;
	private Timer timer;


	/**
	 * Creates the lobby with the specified delay for starting a match.
	 * @param timerDelayForMatchStart the delay for starting a match in millisecodns.
	 */
	public Lobby(long timerDelayForMatchStart) {
		playingClients = new HashMap<>();
		waitingRoom = new HashMap<>();
		this.timerDelayForMatchStart = timerDelayForMatchStart;
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
	}


	/**
	 * Returns the Match that the client is in, or null if it isn't in any Match.
	 * @param client the client.
	 * @return the match in which the client is playing.
	 */
	public Match getMatchOfClient(ClientInterface client) {
		return playingClients.get(client);
	}

	/**
	 * Check if the waiting room has reached the minimum number of players (and schedule the timer for the match to start).
	 * Check if the waiting room has reached the maximum number of players (and start the match immediately).
	 */
	private void checkIfWaitingRoomIsReady() {
		// Send a message to all the clients in the waiting room.
		sendWaitingPlayersMessage();

		// Logic for starting the match or the timer.
		if(waitingRoom.size() == GameConstants.MAX_PLAYERS) {
			// Reached the maximum number of players. Cancel the timer and start the match.
			timer.cancel();
			startMatchInWaitingRoom();
		} else if(waitingRoom.size() == GameConstants.MIN_PLAYERS) {
			// Reached the minimum number of players. Start a timer for starting the match.
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
			sendTimerStartedMessage();
		} else if(waitingRoom.size() < GameConstants.MIN_PLAYERS) {
			// If a timer has been started, cancels it since now the number of players is less than the minimum.
			if(timer != null) {
				timer.cancel();
				sendTimerCanceledMessage();
			}
		}
	}

	/**
	 * Start the match with the clients in the waiting room.
	 */
	private void startMatchInWaitingRoom() {
		Match match = new Match(waitingRoom);
		for(ClientInterface client : waitingRoom.keySet())
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
			for (ClientInterface client : waitingRoom.keySet()) {
				Server.asyncSendMessage(client, new WaitingPlayersMessage(stringBuilder.toString()));
			}
		}
	}

	private void sendTimerStartedMessage() {
		for(ClientInterface client : waitingRoom.keySet())
			Server.asyncSendMessage(client, new TimerForStartMessage(timerDelayForMatchStart, MessageSubtype.INFO));
	}

	private void sendTimerCanceledMessage() {
		for(ClientInterface client : waitingRoom.keySet())
			Server.asyncSendMessage(client, new Message(MessageType.TIMER_FOR_START, MessageSubtype.ERROR));
	}

}
