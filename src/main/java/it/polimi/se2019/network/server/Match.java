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

	private int findPerfectNumberOfSkulls(int numberOfPlayers) {
		// TODO
		return GameConstants.MIN_SKULLS;
	}

	private String findPerfectMap(int numberOfPlayers) {
		// TODO
		return "MediumMap.txt";
	}
}
