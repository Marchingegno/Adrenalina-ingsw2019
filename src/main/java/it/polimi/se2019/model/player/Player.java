package it.polimi.se2019.model.player;

import it.polimi.se2019.model.Representable;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.player.damagestatus.DamageStatus;
import it.polimi.se2019.model.player.damagestatus.LowDamage;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.Utils;

import java.util.List;

public class Player extends Representable {

	private TurnStatus turnStatus;
	private String playerName;
	private int playerID;
	private Color.CharacterColorType playerColor;
	private PlayerBoard playerBoard;
	private DamageStatus damageStatus;
	private PlayerRep playerRep;


	public Player(String playerName, int playerID) {
		this.playerName = playerName;
		this.playerID = playerID;
		this.playerColor = Color.CharacterColorType.values()[playerID + 1]; // + 1 to avoid BLACK color
		playerBoard = new PlayerBoard();
		this.damageStatus = new LowDamage();
		this.turnStatus = TurnStatus.PRE_SPAWN;
		setChanged();
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

	public Color.CharacterColorType getPlayerColor() {
		return playerColor;
	}

	public DamageStatus getDamageStatus() {
		return damageStatus;
	}

	public void setDamageStatus(DamageStatus newDamageStatus) {
		damageStatus = newDamageStatus;
		setChanged();
	}

	public TurnStatus getTurnStatus(){
		return this.turnStatus;
	}

	public void setTurnStatus(TurnStatus status){
		this.turnStatus = turnStatus;
	}

	public List<MacroAction> getAvailableActions() {
		return damageStatus.getAvailableActions();
	}

	public void resetAfterDeath() {
		playerBoard.resetBoardAfterDeath();
		setDamageStatus(new LowDamage());
		turnStatus = TurnStatus.SPAWN;
		setChanged();
	}

	/**
	 * This method should flip the playerBoard to the frenzy side.
	 */
	public boolean flipIfNoDamage() {
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

	public void updateRep() {
		if (playerRep == null || hasChanged()) {
			playerRep = new PlayerRep(this);
			Utils.logInfo("Player Rep of " + playerName + " updated");
		}

	}

	public Representation getRep(String playerAsking) {
		return playerName.equals(playerAsking) ? getRep() : playerRep.getHiddenPlayerRep();
	}

	@Override
	public Representation getRep() {
		return playerRep;
	}
}