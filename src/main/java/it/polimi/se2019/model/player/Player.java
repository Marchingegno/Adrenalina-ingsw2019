package it.polimi.se2019.model.player;

import it.polimi.se2019.model.Representable;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.player.damagestatus.DamageStatus;
import it.polimi.se2019.model.player.damagestatus.LowDamage;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.List;
import java.util.Observable;

/**
 * Class that implements the player.
 *
 * @author Desno365
 * @author MarcerAndrea
 * @author Marchingegno
 */
public class Player extends Observable implements Representable {

	private TurnStatus turnStatus;
	private boolean actionRequested; //If the player is already executing an action.
	private String playerName;
	private int playerID;
	private Color.CharacterColorType playerColor;
	private PlayerBoard playerBoard;
	private DamageStatus damageStatus;
	private PlayerRep playerRep;
	private int firingWeapon = -1; //The current weapon that the player is firing with.
	private int powerupInExecution = -1; // The current powerup that the player is using.


	public Player(String playerName, int playerID) {
		this.playerName = playerName;
		this.playerID = playerID;
		this.playerColor = Color.CharacterColorType.values()[playerID + 1]; // + 1 to avoid BLACK color
		playerBoard = new PlayerBoard();
		this.damageStatus = new LowDamage();
		this.turnStatus = TurnStatus.PRE_SPAWN;
		setChanged();
	}

	public boolean isActionRequested() {
		return actionRequested;
	}

	/**
	 * Returns the player's board.
	 * @return the player's board.
	 */
	public PlayerBoard getPlayerBoard() {
		return playerBoard;
	}

	/**
	 * Returns the player name.
	 * @return the player name.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Returns the player ID.
	 * @return the player ID.
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Returns the player color.
	 * @return the player color.
	 */
	public Color.CharacterColorType getPlayerColor() {
		return playerColor;
	}

	/**
	 * Returns the player damage status.
	 * @return the player damage status.
	 */
	public DamageStatus getDamageStatus() {
		return damageStatus;
	}

	/**
	 * Sets the player damage status.
	 * @param newDamageStatus damage status to set to the player.
	 */
	public void setDamageStatus(DamageStatus newDamageStatus) {
		damageStatus = newDamageStatus;
		Utils.logInfo("Player -> setdamageStatus(): " + playerName + "'s damage status set to " + newDamageStatus);
		setChanged();
	}

	/**
	 * Checks if the player has these ammo.
	 * @param ammoToCheck the list of requested ammo.
	 * @return if the player has as much ammo as the parameter.
	 */
	public boolean hasEnoughAmmo(List<AmmoType> ammoToCheck){
		return getPlayerBoard().hasEnoughAmmo(ammoToCheck);
	}

	/**
	 * Sets the action requested parameter.
	 * @param bool the parameter to set to the player.
	 */
	public void setActionRequested(boolean bool){
		this.actionRequested = bool;
	}

	/**
	 * Returns the turn status.
	 * @return the turn status.
	 */
	public TurnStatus getTurnStatus(){
		return this.turnStatus;
	}

	/**
	 * sets the player turn status.
	 * @param status turn status to set to the player.
	 */
	public void setTurnStatus(TurnStatus status){
		this.turnStatus = status;
		Utils.logInfo("Player -> setTurnStatus(): " + playerName + "'s turn status set to " + status);
		setChanged();
	}

	/**
	 * Returns the available actions of the player according to his damage status.
	 * @return the available actions of the player according to his damage status.
	 */
	public List<MacroAction> getAvailableActions() {
		return damageStatus.getAvailableMacroActions();
	}

	/**
	 * Resets the player after he is dead.
	 */
	public void resetAfterDeath() {
		playerBoard.resetBoardAfterDeath();
		setDamageStatus(new LowDamage());
		//turnStatus = TurnStatus.IDLE;
		setChanged();
	}

	/**
	 * Flips the player's board if he has no damage.
	 * @return true if the board gets flipped.
	 */
	public boolean flipIfNoDamage() {
		boolean temp = playerBoard.flipIfNoDamage();
		if (temp)
			setChanged();
		return temp;
	}

	public void reload(int indexOfWeaponToReload) {
		playerBoard.getWeaponCards().get(indexOfWeaponToReload).load();
		setChanged();
	}

	// ####################################
	// WEAPONS USE
	// ####################################

	public boolean isShootingWeapon() {
		return firingWeapon != -1;
	}

	public QuestionContainer initialWeaponActivation(int indexOfWeapon) {
		firingWeapon = indexOfWeapon;
//		return getFiringWeapon().initialQuestion();
		return getFiringWeapon().doActivationStep(0);
	}

	public QuestionContainer doWeaponStep(int choice) {
		return getFiringWeapon().doActivationStep(choice);
	}

	public void handleWeaponEnd() {
		this.firingWeapon = -1;
	}

	private WeaponCard getFiringWeapon(){
		if(firingWeapon == -1)
			throw new IllegalStateException("No weapon firing!");
		return getPlayerBoard().getWeaponCards().get(firingWeapon);
	}


	// ####################################
	// POWERUPS USE
	// ####################################

	public boolean isPowerupInExecution() {
		return powerupInExecution != -1;
	}

	public QuestionContainer initialPowerupActivation(int indexOfPowerup) {
		powerupInExecution = indexOfPowerup;
		return getPowerupInExecution().initialQuestion();
	}

	public QuestionContainer doPowerupStep(int choice) {
		return getPowerupInExecution().doActivationStep(choice);
	}

	public PowerupCard handlePowerupEnd() {
		if(powerupInExecution == -1)
			throw new IllegalStateException("No powerup in execution!");
		PowerupCard powerupCardToDiscard = getPowerupInExecution();
		getPlayerBoard().removePowerup(powerupInExecution);
		powerupInExecution = -1;
		return powerupCardToDiscard;
	}

	private PowerupCard getPowerupInExecution(){
		if(powerupInExecution == -1)
			throw new IllegalStateException("No powerup in execution!");
		return getPlayerBoard().getPowerupCards().get(powerupInExecution);
	}


	// ####################################
	// REPS
	// ####################################

	/**
	 * Updates the player's representation.
	 */
	public void updateRep() {
		if (playerBoard.hasChanged() || playerBoard.getAmmoContainer().hasChanged() || damageStatus.hasChanged())
			setChanged();

		if (hasChanged() || playerRep == null) {
			playerRep = new PlayerRep(this);
			playerBoard.setNotChanged();
			playerBoard.getAmmoContainer().setNotChanged();
			damageStatus.setNotChanged();
			if (Utils.DEBUG_REPS)
				Utils.logInfo("Player -> updateRep(): " + playerName + "'s representation has been updated");
		} else {
			if (Utils.DEBUG_REPS)
				Utils.logInfo("Player -> updateRep(): " + playerName + "'s representation is already up to date");
		}
	}

	/**
	 * Returns the player representation. if the player who is asking the representation is the same player
	 * the representation is complete, otherwise it is hidden.
	 * @param playerAsking name of the player who is asking the representation.
	 * @return the player's representation.
	 */
	public Representation getRep(String playerAsking) {
		return playerName.equals(playerAsking) ? playerRep : playerRep.getHiddenPlayerRep();
	}

	/**
	 * Returns the player's representation.
	 * @return the player's representation.
	 */
	@Override
	public Representation getRep() {
		return playerRep;
	}
}