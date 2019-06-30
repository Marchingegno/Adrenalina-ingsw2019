package it.polimi.se2019.network.server;

import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.SingleTimer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the waiting room of the server.
 */
public class Lobby {

	private ArrayList<Match> matches = new ArrayList<>();
	private ArrayList<AbstractConnectionToClient> waitingRoom = new ArrayList<>();
	private long timeTimerStart;
	private SingleTimer singleTimer = new SingleTimer();


	/**
	 * Add a client to the lobby.
	 * If the waiting room has reached the minimum number of players a timer starts and when it ends the game starts.
	 * If the waiting room has reached the maximum number of players the game starts immediately.
	 * @param client the client to add to the lobby.
	 * @param nickname the nickname the client wants to use.
	 */
	public void registerClient(AbstractConnectionToClient client, String nickname) {
		if(isNicknameUsed(nickname)) {
			Match inMatch = getMatchOfDisconnectedClient(nickname);
			if(inMatch == null) {
				Utils.logInfo("\tNickname already used, asking a new nickname.");
				client.sendMessage(new Message(MessageType.NICKNAME, MessageSubtype.ERROR));
			} else {
				Utils.logInfo("\tNickname of a disconnected client. Reconnecting to the Match...");
				setNickname(client, nickname);
				inMatch.setParticipantAsReconnected(client);
			}
		} else {
			setNickname(client, nickname);
			waitingRoom.add(client);
			checkIfWaitingRoomIsReady(client);
		}
	}

	/**
	 * Removes the client from the waiting room if present.
	 * @param client the client to remove from the waiting room.
	 */
	public void removeWaitingClient(AbstractConnectionToClient client) {
		waitingRoom.remove(client);
		checkIfWaitingRoomIsReady(null);
	}

	/**
	 * Check if the client that is disconnected was a participant of a match.
	 * Suspend the client if the match started or dismantle the match if the match isn't started.
	 * @param client the client that lost the connection.
	 */
	public void clientDisconnectedFromMatch(AbstractConnectionToClient client) {
		Match match = getMatchOfClient(client);
		if(match != null) { // If the client was a participant of a match.
			if(match.isMatchStarted()) { // If client was a participant in an already started match report it to the Match.
				match.setParticipantAsDisconnected(client);
			} else { // The client was a participant of a match that isn't started yet. Dismantle the match.
				List<AbstractConnectionToClient> participantsOfTheDismantledMatch = match.getParticipants();
				participantsOfTheDismantledMatch.remove(client); // Remove the disconnected client from this list.

				// Remove the match from the matches list.
				matches.remove(match);

				// Add the participants of the dismantled match to the waiting room.
				for(AbstractConnectionToClient participant : participantsOfTheDismantledMatch) {
					waitingRoom.add(participant);
					checkIfWaitingRoomIsReady(participant);
				}
			}
		}
	}

	/**
	 * Returns the Match that the client is in, or null if it isn't in any Match.
	 * @param client the client.
	 * @return the match in which the client is playing.
	 */
	public Match getMatchOfClient(AbstractConnectionToClient client) {
		for(Match match : matches) {
			for(AbstractConnectionToClient clientInMatch : match.getParticipants()) {
				if(clientInMatch == client)
					return match;
			}
		}
		return null;
	}


	// ####################################
	// PRIVATE METHODS
	// ####################################

	/**
	 * Returns true if this nickname is already used by some other client.
	 * @param nickname the nickname to check for.
	 * @return true if the nickname is already used.
	 */
	private boolean isNicknameUsed(String nickname) {
		// Check in the waiting room list.
		if(waitingRoom.stream().anyMatch(client -> client.getNickname().equals(nickname)))
			return true;

		// Check in the started matches.
		for(Match match : matches) {
			for(AbstractConnectionToClient clientInMatch : match.getParticipants()) {
				if(clientInMatch.getNickname().equals(nickname))
					return true;
			}
		}

		return false;
	}

	/**
	 * Sets the nickname to a client,
	 * @param client the client.
	 * @param nickname the nickname to set to the client.
	 */
	private void setNickname(AbstractConnectionToClient client, String nickname) {
		Utils.logInfo("\tNickname of client \"" + client.hashCode() + "\" set to \"" + nickname + "\".");
		client.setNickname(nickname);
		client.sendMessage(new NicknameMessage(client.getNickname(), MessageSubtype.OK));
	}

	/**
	 * Returns null if this nickname is not of a disconnected client,
	 * Returns the match of the client if it is a disconnected participant.
	 * @param nickname the nickname to check for.
	 * @return the match of the client if it is a disconnected participant, or null if it isn't.
	 */
	private Match getMatchOfDisconnectedClient(String nickname) {
		for(Match match : matches) {
			for(AbstractConnectionToClient disconnectedClient : match.getDisconnectedParticipants()) {
				if(disconnectedClient.getNickname().equals(nickname))
					return match;
			}
		}
		return null;
	}

	/**
	 * Should be called after adding or removing a client to the waiting room.
	 * Check if the waiting room has reached the minimum number of players (and schedule the timer for the match to start).
	 * Check if the waiting room has reached the maximum number of players (and start the match immediately).
	 * @param newestClient the latest added client. Can be null if not called after adding a client.
	 */
	private void checkIfWaitingRoomIsReady(AbstractConnectionToClient newestClient) {
		// Send a message to all the clients in the waiting room.
		sendWaitingPlayersMessages();

		// Logic for starting the match or the timer.
		if(waitingRoom.size() == GameConstants.MAX_PLAYERS) {
			// Reached the maximum number of players. Cancel the timer and start the match.
			startMatchInWaitingRoom();
		} else if(waitingRoom.size() == GameConstants.MIN_PLAYERS) {
			if(Utils.DEBUG_BYPASS_CONFIGURATION){
				startMatchInWaitingRoom();
				return;
			}
			// Reached the minimum number of players. Start a timer for starting the match if not already started.
			if(!singleTimer.isRunning()) {
				startTimerForMatchStart();
				sendTimerStartedMessages();
			}
		} else if(waitingRoom.size() < GameConstants.MIN_PLAYERS) {
			// If a timer has been started, cancels it since now the number of players is less than the minimum.
			if(singleTimer.isRunning()) {
				singleTimer.cancel();
				sendTimerCanceledMessages();
			}
		} else {
			// Number of players between the minimum and the maximum. Send to the new client the remaining time for starting the match.
			if(singleTimer.isRunning() && newestClient != null) {
				long timePassedAfterTimerStart = System.currentTimeMillis() - timeTimerStart;
				long timeRemainingInTimer = Utils.getServerConfig().getWaitingTimeInLobbyMs() - timePassedAfterTimerStart;
				newestClient.sendMessage(new TimerForStartMessage(timeRemainingInTimer, MessageSubtype.INFO));
			}
		}
	}

	/**
	 * Start the match with the clients in the waiting room and cancels the timer.
	 */
	private void startMatchInWaitingRoom() {
		// Stop the timer if it is running, used when reaching the maximum number of players.
		singleTimer.cancel();

		// Start a new match.
		Match match = new Match(waitingRoom);
		matches.add(match);
		waitingRoom.clear();
		match.requestMatchConfig();
	}

	/**
	 * Sends a message to all the players in the waiting room with the list of players in the waiting room.
	 */
	private void sendWaitingPlayersMessages() {
		if(!waitingRoom.isEmpty()) {
			// Create a list of player names.
			List<String> playersNames = waitingRoom.stream()
					.map(AbstractConnectionToClient::getNickname)
					.collect(Collectors.toList());

			// Send the message with all nicknames.
			waitingRoom.forEach(client -> client.sendMessage(new WaitingPlayersMessage(playersNames)));
		}
	}

	/**
	 * Starts the timer that make the match start.
	 */
	private void startTimerForMatchStart() {
		timeTimerStart = System.currentTimeMillis();
		final Lobby lobby = this;
		singleTimer.start(() -> {
			Utils.logInfo("Timer ended => Starting the match...");
			lobby.startMatchInWaitingRoom();
		}, Utils.getServerConfig().getWaitingTimeInLobbyMs());
	}

	/**
	 * Sends a message to all the players in the waiting room, informing them about the start of the timer.
	 */
	private void sendTimerStartedMessages() {
		waitingRoom.forEach(client -> client.sendMessage(new TimerForStartMessage(Utils.getServerConfig().getWaitingTimeInLobbyMs(), MessageSubtype.INFO)));
	}

	/**
	 * Sends a message to all the players in the waiting room, informing them about the cancellation of the timer.
	 */
	private void sendTimerCanceledMessages() {
		waitingRoom.forEach(client -> client.sendMessage(new Message(MessageType.TIMER_FOR_START, MessageSubtype.ERROR)));
	}

}
