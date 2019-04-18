package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.network.ConnectionInterface;
import it.polimi.se2019.network.message.GameConfigMessage;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Match {

	private HashMap<ConnectionInterface, String> participants;
	private int numberOfPartecipants;
	private Controller controller;

	// Game config attributes.
	private HashMap<ConnectionInterface, Integer> skullsChosen;
	private HashMap<ConnectionInterface, Integer> mapChoosen;
	private int numberOfAnswers = 0;


	/**
	 * Create a new match with the specified clients.
	 * @param participants a map that contains all the clients for this match and their nicknames.
	 */
	public Match(Map<ConnectionInterface, String> participants) {
		numberOfPartecipants = participants.size();
		if(numberOfPartecipants < GameConstants.MIN_PLAYERS || numberOfPartecipants > GameConstants.MAX_PLAYERS)
			throw new IllegalArgumentException("The number of participants for this match (" + numberOfPartecipants + ") is not valid.");
		this.participants = new HashMap<>(participants);

		this.skullsChosen = new HashMap<>();
		this.mapChoosen = new HashMap<>();
	}


	/**
	 * Send game config request messages to the clients, asking skulls and map type.
	 */
	public void requestMatchConfig() {
		for(ConnectionInterface client : participants.keySet())
			Server.asyncSendMessage(client, new Message(MessageType.GAME_CONFIG, MessageSubtype.REQUEST));
	}

	public void addConfigVote(ConnectionInterface client, int skulls, int mapIndex) {
		if(participants.containsKey(client)) { // Check if the participants is in the Match.
			if(!skullsChosen.containsKey(client))
				skullsChosen.put(client, skulls);

			if(!mapChoosen.containsKey(client))
				mapChoosen.put(client, mapIndex);

			numberOfAnswers++;
			if(numberOfAnswers >= numberOfPartecipants)
				startMatch();
		}
	}

	/**
	 * Start the match.
	 */
	private void startMatch() {
		// Find votes.
		int skulls = findVotedNumberOfSkulls();
		GameConstants.MapType mapType = findVotedMap();
		Utils.logInfo("Starting a new match with skulls: " + skulls + ", mapName: \"" + mapType.getMapName() + "\".");

		// Send game start message with the voted skulls and map.
		for(ConnectionInterface client : participants.keySet()) {
			GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.OK);
			gameConfigMessage.setSkulls(skulls);
			gameConfigMessage.setMapIndex(mapType.ordinal());
			Server.asyncSendMessage(client, gameConfigMessage);
		}

		// start the game.
		controller = new Controller(new ArrayList<>(participants.values()), skulls, mapType.getMapName());
		controller.startGame();
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
