package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.network.message.GameConfigMessage;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.VirtualView;

import java.util.*;

public class Match {

	private HashMap<ConnectionToClientInterface, String> participants;
	private HashMap<ConnectionToClientInterface, VirtualView> virtualViews = new HashMap<>();
	private int numberOfParticipants;
	private Controller controller;
	private boolean matchStarted = false;

	// Game config attributes.
	private HashMap<ConnectionToClientInterface, Integer> skullsChosen = new HashMap<>();
	private HashMap<ConnectionToClientInterface, Integer> mapChoosen = new HashMap<>();
	private int numberOfAnswers = 0;


	/**
	 * Create a new match with the specified clients.
	 * @param participants a map that contains all the clients for this match and their nicknames.
	 */
	public Match(Map<ConnectionToClientInterface, String> participants) {
		numberOfParticipants = participants.size();
		if(numberOfParticipants < GameConstants.MIN_PLAYERS || numberOfParticipants > GameConstants.MAX_PLAYERS)
			throw new IllegalArgumentException("The number of participants for this match (" + numberOfParticipants + ") is not valid.");
		this.participants = new HashMap<>(participants);
	}


	/**
	 * Send game config request messages to the clients, asking skulls and map type.
	 */
	public void requestMatchConfig() {
		for(ConnectionToClientInterface client : participants.keySet())
			client.sendMessage(new Message(MessageType.GAME_CONFIG, MessageSubtype.REQUEST));
	}

	// TODO start the match also after a timer and not wait every answer?
	public void addConfigVote(ConnectionToClientInterface client, int skulls, int mapIndex) {
		if(participants.containsKey(client)) { // Check if the client is in the Match.
			if(!skullsChosen.containsKey(client))
				skullsChosen.put(client, skulls);

			if(!mapChoosen.containsKey(client))
				mapChoosen.put(client, mapIndex);

			numberOfAnswers++;
			if(numberOfAnswers >= numberOfParticipants)
				startMatch();
		}
	}

	/**
	 * Returns a list with all the participants of this match.
	 * @return a list with all the participants of this match.
	 */
	public Map<ConnectionToClientInterface, String> getParticipants() {
		return new HashMap<>(participants);
	}

	/**
	 * Returns the VirtualView associated to the client, or null if this match doesn't have the VirtualView of the client.
	 * @param client the client.
	 * @return the VirtualView associated to the client.
	 */
	public VirtualView getVirtualViewOfClient(ConnectionToClientInterface client) {
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
		matchStarted = true;

		// Find votes.
		int skulls = findVotedNumberOfSkulls();
		GameConstants.MapType mapType = findVotedMap();
		Utils.logInfo("Starting a new match with skulls: " + skulls + ", mapName: \"" + mapType.getMapName() + "\".");

		// start the game.
		controller = new Controller(new ArrayList<>(participants.values()), skulls, mapType.getMapName());

		for (Map.Entry<ConnectionToClientInterface, String> client : participants.entrySet()) {
			Utils.logInfo("Added Virtual View to " + client.getValue());
			virtualViews.put(client.getKey(), new VirtualView(controller, client.getKey(), client.getValue()));
		}

		controller.getModel().updateReps();

		// TODO no need for a timer here
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// Send game start message with the voted skulls and map.
				for(ConnectionToClientInterface client : participants.keySet()) {
					GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.OK);
					gameConfigMessage.setSkulls(skulls);
					gameConfigMessage.setMapIndex(mapType.ordinal());
					client.sendMessage(gameConfigMessage);
				}

				Utils.logInfo("Timer ended. Starting the match...");
			}
		}, 5000L);
	}

	/**
	 * Calculates the average of voted skulls.
	 * @return the average of voted skulls.
	 */
	private int findVotedNumberOfSkulls() {
		float average = 0f;
		for(Integer votedSkulls : skullsChosen.values())
			average += votedSkulls;
		return  Math.round(average / skullsChosen.values().size());
	}

	/**
	 * Find the most voted map, if votes are tied it chooses the smaller map.
	 * @return the map name of the most voted map.
	 */
	private GameConstants.MapType findVotedMap() {
		// Create array of votes.
		int[] votes = new int[GameConstants.MapType.values().length];

		// Initialize array with all zeros.
		for (int i = 0; i < votes.length; i++)
			votes[i] = 0;

		// Add votes.
		for(Integer votedMap : mapChoosen.values())
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

}
