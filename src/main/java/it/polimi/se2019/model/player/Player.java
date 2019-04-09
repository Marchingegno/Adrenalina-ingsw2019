package it.polimi.se2019.model.player;

import it.polimi.se2019.model.GameBoard;

import java.awt.Color;

public class Player {

	private String playerName;
	private int playerID;
	private Color playerColor;
	private PlayerBoard playerBoard;
	private Status status;
	private GameBoard gameboard;


	public Player(String playerName, int playerID, Color playerColor) {
		this.playerName = playerName;
		this.playerID = playerID;
		this.playerColor = playerColor;
		playerBoard = new PlayerBoard();
		status = new LowDamage();
	}

	public PlayerBoard getPlayerBoard() {
		return playerBoard;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getPlayerID() {
		return playerID;
	}

	public Color getPlayerColor() {
		return playerColor;
	}

	public void setStatus(Status newStatus) {
		status = newStatus;
	}

	public Status getStatus() {
		return status;
	}

	public void getAvailableActions() {
	}

	public void shoot() {
	}

	public void grab() {
	}

	public void reload() {
	}

	public void getDistance(Player target) {
	}

	public void isVisible(Player target) {
	}

	public void getAllVisiblePlayers() {
	}

}