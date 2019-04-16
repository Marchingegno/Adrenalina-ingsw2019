package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.GameConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Match {

	private HashMap<ClientInterface, String> participants;
	private int numberOfPartecipants;
	private Controller controller;


	/**
	 * Create a new match with the specified clients.
	 * @param participants a map that contains all the clients for this match and their nicknames.
	 */
	public Match(Map<ClientInterface, String> participants) {
		if(participants.size() < GameConstants.MIN_PLAYERS || participants.size() > GameConstants.MAX_PLAYERS)
			throw new IllegalArgumentException("The number of participants for this match (" + participants.size() + ") is not valid.");
		this.participants = new HashMap<>(participants);
		this.numberOfPartecipants = participants.size();
	}


	/**
	 * Start the match.
	 */
	public void startMatch() {
		controller = new Controller(new ArrayList<>(participants.values()), findPerfectNumberOfSkulls(numberOfPartecipants), findPerfectMap(numberOfPartecipants));
		controller.startGame();
		for(ClientInterface client : participants.keySet())
			Server.asyncSendMessage(client, new Message(MessageType.MATCH_START, MessageSubtype.OK));
	}

	/**
	 * Find the number of skulls randomly, using a gaussian with mean based on the number of players: less players means less skulls, more players means more skulls.
	 * @param numberOfPlayers the number of players.
	 * @return the "perfect" number of skulls.
	 */
	private int findPerfectNumberOfSkulls(int numberOfPlayers) {
		// Assign the mean of the gaussian based on the number of players
		int mean;
		if(numberOfPlayers == GameConstants.MIN_PLAYERS)
			mean = GameConstants.MIN_SKULLS;
		else if(numberOfPlayers == GameConstants.MAX_PLAYERS)
			mean = GameConstants.MAX_SKULLS;
		else
			mean = (GameConstants.MAX_SKULLS + GameConstants.MIN_SKULLS) / 2;

		// Get the random number of skulls on the gaussian using the mean previously found.
		int skulls = (int) (new Random().nextGaussian() + mean);
		if(skulls < GameConstants.MIN_SKULLS)
			skulls = GameConstants.MIN_SKULLS;
		if(skulls > GameConstants.MAX_SKULLS)
			skulls = GameConstants.MAX_SKULLS;
		return skulls;
	}

	private String findPerfectMap(int numberOfPlayers) {
		if(numberOfPlayers == GameConstants.MIN_PLAYERS)
			return "SmallMap.txt";
		if(numberOfPlayers == GameConstants.MAX_PLAYERS)
			return "BigMap.txt";
		return "MediumMap.txt";
	}
}
