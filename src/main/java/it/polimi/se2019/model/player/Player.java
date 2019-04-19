package it.polimi.se2019.model.player;

import it.polimi.se2019.model.player.damagestatus.DamageStatus;
import it.polimi.se2019.model.player.damagestatus.LowDamage;
import it.polimi.se2019.utils.MacroAction;

import java.awt.*;
import java.util.List;
import java.util.Observable;

public class Player extends Observable {

	private String playerName;
	private int playerID;
	private Color playerColor;
	private PlayerBoard playerBoard;
	private DamageStatus damageStatus;


	public Player(String playerName, int playerID, Color playerColor) {
		this.playerName = playerName;
		this.playerID = playerID;
		this.playerColor = playerColor;
		playerBoard = new PlayerBoard();
		damageStatus = new LowDamage();
	}

	public Player(String playerName, int playerID) {
		this.playerName = playerName;
		this.playerID = playerID;
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
		setChanged();
	}

	public DamageStatus getDamageStatus() {
		return damageStatus;
	}

	public List<MacroAction> getAvailableActions() {
		return damageStatus.getAvailableActions();
	}

	public void resetAfterDeath(){
		playerBoard.resetBoardAfterDeath();
		setDamageStatus(new LowDamage());
		setChanged();
	}

	/**
	 * This method should flip the playerBoard to the frenzy side.
	 */
	public boolean flipIfNoDamage(){
		return playerBoard.flipIfNoDamage();
	}

	public void shoot() {
		setChanged();
	}

	public void grab() {
		setChanged();
	}

	public void reload() {
		setChanged();
	}

	public void getDistance(Player target) {
	}

	public void isVisible(Player target) {
	}

	public void getAllVisiblePlayers() {
	}
}