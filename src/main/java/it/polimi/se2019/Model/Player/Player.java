package it.polimi.se2019.Model.Player;

import it.polimi.se2019.Model.Gameboard;

import java.util.*;

/**
 *
 */
public class Player {

	/**
	 * Default constructor
	 */
	public Player() {
	}

	/**
	 *
	 */
	private String playerName;

	/**
	 *
	 */
	private int playerID;

	/**
	 *
	 */
	private Gameboard gameboard;

	/**
	 *
	 */
	private PlayerBoard playerBoard;

	/**
	 *
	 */
	private Status status;

	/**
	 * @param playerName
	 * @param playerID
	 */
	public void Player(String playerName, int playerID) {
		// TODO implement here
	}

	/**
	 *
	 */
	public void getAvailableActions() {
		// TODO implement here
	}

	/**
	 *
	 */
	public void shoot() {
		// TODO implement here
	}

	/**
	 *
	 */
	public void grab() {
		// TODO implement here
	}

	/**
	 *
	 */
	public void reload() {
		// TODO implement here
	}

	/**
	 * @param target
	 */
	public void getDistance(Player target) {
		// TODO implement here
	}

	/**
	 * @param target
	 */
	public void isVisible(Player target) {
		// TODO implement here
	}

	/**
	 *
	 */
	public void getAllVisiblePlayers() {
		// TODO implement here
	}

	/**
	 * @param newStatus
	 */
	public void setStatus(Status newStatus) {
		// TODO implement here
	}

	/**
	 * @return
	 */
	public Status getStatus() {
		// TODO implement here
		return null;
	}

}