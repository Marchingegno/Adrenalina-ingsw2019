package it.polimi.se2019.model.player;

import it.polimi.se2019.model.GameBoard;
import it.polimi.se2019.model.player.damagestatus.DamageStatus;
import it.polimi.se2019.model.player.damagestatus.LowDamage;
import it.polimi.se2019.utils.MacroAction;

import java.awt.Color;
import java.util.ArrayList;

public class Player {

	private String playerName;
	private int playerID;
	private Color playerColor;
	private PlayerBoard playerBoard;
	private DamageStatus damageStatus;
	private GameBoard gameBoard;


	public Player(String playerName, int playerID, Color playerColor) {
		this.playerName = playerName;
		this.playerID = playerID;
		this.playerColor = playerColor;
		playerBoard = new PlayerBoard();
		damageStatus = new LowDamage();
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

	public void setDamageStatus(DamageStatus newDamageStatus) {
		damageStatus = newDamageStatus;
	}

	public DamageStatus getDamageStatus() {
		return damageStatus;
	}

	public ArrayList<MacroAction> getAvailableActions() {
		return damageStatus.getAvailableActions();
	}

	public void resetAfterDeath(){
		playerBoard.resetBoardAfterDeath();
		setDamageStatus(new LowDamage());
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