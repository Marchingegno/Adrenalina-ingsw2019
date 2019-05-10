package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.network.message.GameConfigMessage;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.SingleTimer;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Match {

	private final int numberOfParticipants;
	private final ArrayList<AbstractConnectionToClient> participants;
	private HashMap<AbstractConnectionToClient, VirtualView> virtualViews = new HashMap<>();
	private boolean matchStarted = false;
	private SingleTimer singleTimer = new SingleTimer();

	// Game config attributes.
	private HashMap<AbstractConnectionToClient, Integer> skullsChosen = new HashMap<>();
	private HashMap<AbstractConnectionToClient, Integer> mapChosen = new HashMap<>();
	private int numberOfAnswers = 0;


	/**
	 * Create a new match with the specified clients.
	 * @param participants a map that contains all the clients for this match and their nicknames.
	 */
	public Match(List<AbstractConnectionToClient> participants) {
		numberOfParticipants = participants.size();
		if(numberOfParticipants < GameConstants.MIN_PLAYERS || numberOfParticipants > GameConstants.MAX_PLAYERS)
			throw new IllegalArgumentException("The number of participants for this match (" + numberOfParticipants + ") is not valid.");
		this.participants = new ArrayList<>(participants);
	}


	/**
	 * Send game config request messages to the clients, asking skulls and map type.
	 */
	public void requestMatchConfig() {
		if(isMatchStarted())
			return;

		for(AbstractConnectionToClient client : participants)
			client.sendMessage(new Message(MessageType.GAME_CONFIG, MessageSubtype.REQUEST));

		singleTimer.start(this::startMatch, (Utils.getServerConfig()).getTurnTimeLimitMs());
	}

	public void addConfigVote(AbstractConnectionToClient client, int skulls, int mapIndex) {
		if(isMatchStarted()) {
			Utils.logInfo("\tMatch already started, GameConfigMessage ignored.");
			return;
		}

		if(participants.contains(client) && !skullsChosen.containsKey(client) && !mapChosen.containsKey(client)) { // Check if the client is in the Match and if he didn't already vote.
			Utils.logInfo("\tAdding game config vote with skulls " + skulls + ", map index " + mapIndex + ".");

			skullsChosen.put(client, skulls);
			mapChosen.put(client, mapIndex);

			numberOfAnswers++;
			if(numberOfAnswers >= numberOfParticipants) {
				Utils.logInfo("\t\tAll participants sent their votes. Starting the game.");
				startMatch();
			}
		}
	}

	/**
	 * Returns a list with all the participants of this match.
	 * @return a list with all the participants of this match.
	 */
	public List<AbstractConnectionToClient> getParticipants() {
		return new ArrayList<>(participants);
	}

	/**
	 * Returns the VirtualView associated to the client, or null if this match doesn't have the VirtualView of the client.
	 * @param client the client.
	 * @return the VirtualView associated to the client.
	 */
	public VirtualView getVirtualViewOfClient(AbstractConnectionToClient client) {
		return virtualViews.get(client);
	}

	/**
	 * Returns true if the match started.
	 * @return true if the match started.
	 */
	public boolean isMatchStarted() {
		return matchStarted;
	}

	/**
	 * Start the match.
	 */
	private void startMatch() {
		singleTimer.cancel();

		// Find votes.
		int skulls = findVotedNumberOfSkulls();
		GameConstants.MapType mapType = findVotedMap();
		Utils.logInfo("Starting a new game with skulls: " + skulls + ", mapName: \"" + mapType.getMapName() + "\".");

		// Send messages with votes.
		sendVotesResultMessages(skulls, mapType);

		// Create virtualViews.
		for (AbstractConnectionToClient client : participants) {
			Utils.logInfo("Added Virtual View to " + client.getNickname());
			VirtualView virtualView =  new VirtualView(client);
			virtualViews.put(client, virtualView);
		}

		// Create Controller.
		Controller controller = new Controller(mapType, virtualViews.values(), skulls);

		// Start the game.
		controller.startGame();
		matchStarted = true;
	}

	/**
	 * Calculates the average of voted skulls.
	 * @return the average of voted skulls.
	 */
	private int findVotedNumberOfSkulls() {
		// If there are no votes gives a default value.
		if(skullsChosen.size() == 0)
			return GameConstants.MIN_SKULLS;

		// Calculates the average.
		float average = 0f;
		for(Integer votedSkulls : skullsChosen.values())
			average += votedSkulls;
		return Math.round(average / skullsChosen.values().size());
	}

	/**
	 * Find the most voted map, if votes are tied it chooses the map with the smaller ordinal.
	 * @return the map type of the most voted map.
	 */
	private GameConstants.MapType findVotedMap() {
		// Create array of votes.
		int[] votes = new int[GameConstants.MapType.values().length];

		// Initialize array with all zeros.
		for (int i = 0; i < votes.length; i++)
			votes[i] = 0;

		// Add votes.
		for(Integer votedMap : mapChosen.values())
			votes[votedMap]++;

		// search for max.
		int indexOfMax = 0;
		for(int i = 1; i < votes.length; i++) {
			if (votes[i] > votes[indexOfMax])
				indexOfMax = i;
		}

		// Return corresponding map.
		return GameConstants.MapType.values()[indexOfMax];
	}

	/**
	 * Send a message with the result of the poll.
	 * @param skulls average number of skulls voted.
	 * @param mapType most voted map type.
	 */
	private void sendVotesResultMessages(int skulls, GameConstants.MapType mapType) {
		for(AbstractConnectionToClient client : participants) {
			GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.OK);
			gameConfigMessage.setSkulls(skulls);
			gameConfigMessage.setMapIndex(mapType.ordinal());
			client.sendMessage(gameConfigMessage);
		}
	}

}
