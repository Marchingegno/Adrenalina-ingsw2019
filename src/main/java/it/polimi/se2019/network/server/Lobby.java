package it.polimi.se2019.network.server;

import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.ServerConfigParser;
import it.polimi.se2019.utils.SingleTimer;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.VirtualView;

import java.util.*;

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
			Utils.logInfo("\tNickname already used, asking a new nickname.");
			client.sendMessage(new Message(MessageType.NICKNAME, MessageSubtype.ERROR));
		} else {
			Utils.logInfo("\tNickname of client \"" + client.hashCode() + "\" set to \"" + nickname + "\".");
			client.setNickname(nickname);
			client.sendMessage(new NicknameMessage(client.getNickname(), MessageSubtype.OK));
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
			if(match.isMatchStarted()) { // If client was a participant in an already started match forward the message to the VirtualView.
				VirtualView virtualView = match.getVirtualViewOfClient(client);
				if(virtualView == null)
					Utils.logError("The VirtualView should always be set if the match is started.", new IllegalStateException());
				else
					virtualView.onClientDisconnected();
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

	/**
	 * Returns true if this nickname is already used by some other client.
	 * @param nickname the nickname to check for.
	 * @return true if the nickname is already used.
	 */
	private boolean isNicknameUsed(String nickname) {
		// Check in the waiting room list.
		for(AbstractConnectionToClient client : waitingRoom) {
			if(client.getNickname().equals(nickname))
				return true;
		}

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
			if(Utils.BYPASS){
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
				long timeRemainingInTimer = ServerConfigParser.getWaitingTimeInLobbyMs() - timePassedAfterTimerStart;
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

	private void sendWaitingPlayersMessages() {
		if(!waitingRoom.isEmpty()) {
			// Create a String of all the nicknames of the players.
			StringBuilder stringBuilder = new StringBuilder();
			for (AbstractConnectionToClient client : waitingRoom) {
				if(stringBuilder.length() != 0)
					stringBuilder.append(", ");
				stringBuilder.append(client.getNickname());
			}

			// Send the message with all nicknames.
			waitingRoom.forEach(client -> client.sendMessage(new WaitingPlayersMessage(stringBuilder.toString())));
		}
	}

	private void startTimerForMatchStart() {
		timeTimerStart = System.currentTimeMillis();
		final Lobby lobby = this;
		singleTimer.start(() -> {
			Utils.logInfo("Timer ended => Starting the match...");
			lobby.startMatchInWaitingRoom();
		}, ServerConfigParser.getWaitingTimeInLobbyMs());
	}

	private void sendTimerStartedMessages() {
		waitingRoom.forEach(client -> client.sendMessage(new TimerForStartMessage(ServerConfigParser.getWaitingTimeInLobbyMs(), MessageSubtype.INFO)));
	}

	private void sendTimerCanceledMessages() {
		waitingRoom.forEach(client -> client.sendMessage(new Message(MessageType.TIMER_FOR_START, MessageSubtype.ERROR)));
	}

}
