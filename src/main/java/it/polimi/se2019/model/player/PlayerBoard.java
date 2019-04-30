package it.polimi.se2019.model.player;

import it.polimi.se2019.model.cards.ammo.AmmoContainer;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.utils.GameConstants;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Represents the player board and all the players information.
 * @author Desno365
 */
public class PlayerBoard {

	private ArrayList<Player> damageBoard;
	private ArrayList<Player> marks;
	private int numberOfDeaths;
	private int points;
	private boolean isFlipped; //Indicates if the damageBoard is flipped to the frenzy side.
	private AmmoContainer ammoContainer;
	private ArrayList<PowerupCard> powerupCards;
	private ArrayList<WeaponCard> weaponCards;


	/**
	 * Create an empty PlayerBoard ready to be used.
	 */
	public PlayerBoard() {
		damageBoard = new ArrayList<>();
		marks = new ArrayList<>();
		numberOfDeaths = 0;
		points = 0;
		ammoContainer = new AmmoContainer();
		powerupCards = new ArrayList<>();
		weaponCards = new ArrayList<>();
		isFlipped = FALSE;
	}


	/**
	 * Add damageBoard to the player and add previous marks to the damageBoard.
	 * @param shootingPlayer the player making the damageBoard.
	 * @param amountOfDamage amount of damageBoard tokens to add to the player.
	 * @throws IllegalArgumentException if the amount of damageBoard is not positive.
	 */
	public void addDamage(Player shootingPlayer, int amountOfDamage) {
		if(amountOfDamage <= 0)
			throw new IllegalArgumentException("amountOfDamage cannot be negative or zero.");

		// Add damageBoard
		for (int i = 0; i < amountOfDamage; i++)
			if(damageBoard.size() < GameConstants.OVERKILL_DAMAGE)
				damageBoard.add(shootingPlayer);

		// Add marks
		for (int i = 0; i < GameConstants.MAX_MARKS_PER_PLAYER; i++) {
			int index = marks.indexOf(shootingPlayer);
			if(index == -1) {
				break;
			} else {
				damageBoard.add(shootingPlayer);
				marks.remove(index);
			}
		}
	}

	/**
	 * Returns the current damageBoard board of the player.
	 * @return the List of damages done to the player.
	 */
	public List<Player> getDamageBoard() {
		return new ArrayList<>(damageBoard);
	}


	public boolean isFrenzy(){
		//TODO: Implementare metodo dopo l'implementazione del DamageStatus
		return false;
	}

	public boolean isFlipped() {
		return isFlipped;
	}

	/**
	 * Add marks to the player.
	 * @param shootingPlayer the player making the damageBoard.
	 * @param amountOfMarks amount of marks to add to the player.
	 * @throws IllegalArgumentException if the amount of marks is not positive.
	 */
	public void addMarks(Player shootingPlayer, int amountOfMarks) {
		if(amountOfMarks <= 0)
			throw new IllegalArgumentException("amountOfMarks cannot be negative or zero.");

		for (int i = 0; i < amountOfMarks; i++)
			// check if the shooting player doesn't have the max number of marks on the target player.
			if(marks.stream().filter(player -> player == shootingPlayer).count() < GameConstants.MAX_MARKS_PER_PLAYER)
				marks.add(shootingPlayer);
	}

	/**
	 * Returns the current marks of the player.
	 * @return the List of marks done to the player.
	 */
	public List<Player> getMarks() {
		return new ArrayList<>(marks);
	}

	/**
	 * Returns true if the player is dead.
	 * @return true if the player is dead.
	 */
	public boolean isDead() {
		return damageBoard.size() >= GameConstants.DEATH_DAMAGE;
	}

	/**
	 * Returns true if the player is overkilled.
	 * @return true if the player is overkilled.
	 */
	public boolean isOverkilled() {
		return damageBoard.size() >= GameConstants.OVERKILL_DAMAGE;
	}

	/**
	 * Add points to the player.
	 * @param pointsToAdd amount of points to add to the player.
	 * @throws IllegalArgumentException if the amount of points to add is negative.
	 */
	public void addPoints(int pointsToAdd) {
		if(pointsToAdd < 0)
			throw new IllegalArgumentException("pointsToAdd cannot be negative.");

		points += pointsToAdd;
	}

	/**
	 * Returns the current points of the player.
	 * @return the points of the player.
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Reset the PlayerBoard after the player death and increments its deaths.
	 */
	public void resetBoardAfterDeath() {
		if(isDead()) {
			numberOfDeaths++;
			resetDamageBoard();
		}
	}

	private void resetDamageBoard() {
		damageBoard = new ArrayList<>();
	}

	/**
	 * Returns the number of deaths for the player.
	 * @return the number of deaths for the player.
	 */
	public int getNumberOfDeaths() {
		return numberOfDeaths;
	}

	/**
	 * Add the weapon card to the player inventory.
	 * @param weaponToAdd weapon card to add to the player inventory.
	 * @throws InventoryFullException if the inventory is already full.
	 */
	public void addWeapon(WeaponCard weaponToAdd) {
		if(weaponCards.size() >= GameConstants.MAX_WEAPON_CARDS_PER_PLAYER)
			throw new InventoryFullException("Cannot add another weapon card since the inventory is full. Use swapWeapon to setChanged the weapon.");
		weaponCards.add(weaponToAdd);
	}

	/**
	 * Returns the weapon cards in player's inventory.
	 * @return the weapon cards in player's inventory.
	 * @deprecated
	 */
	public List<WeaponCard> getWeaponCards() {
		return new ArrayList<>(weaponCards);
	}

	public int numOfWeapons(){return weaponCards.size();}

	public WeaponCard removeWeapon(int indexOfTheWeapon){
		return weaponCards.remove(indexOfTheWeapon);
	}

	/**
	 * Remove weaponCardToDrop from the inventory while adding weaponToGrab.
	 * @param weaponToGrab weapon card to add to the inventory.
	 * @param weaponCardToDrop weapon card to remove from the inventory.
	 * @throws IllegalArgumentException if the weapon to remove is not present in the player's inventory.
	 */
	public void swapWeapon(WeaponCard weaponToGrab, WeaponCard weaponCardToDrop) {
		int index = weaponCards.indexOf(weaponCardToDrop);
		if(index < 0)
			throw new IllegalArgumentException("weaponCardToDrop is not owned by the player.");
		weaponCards.set(index, weaponToGrab);
	}

	/**
	 * Add the powerup card to the player's inventory.
	 * @param powerupToAdd powerup card to add to the player inventory.
	 * @throws InventoryFullException if the inventory is already full.
	 */
	public void addPowerup(PowerupCard powerupToAdd) {
		powerupCards.add(powerupToAdd);
		if(powerupCards.size() >= GameConstants.MAX_POWERUP_CARDS_PER_PLAYER)
			throw new InventoryFullException("Cannot add another powerup card since the inventory is full.");
	}

	/**
	 * Return the powerup cards in player's inventory.
	 * @return the powerup cards in player's inventory.
	 */
	public List<PowerupCard> getPowerupCards() {
		return new ArrayList<>(powerupCards);
	}

	/**
	 * Remove the powerup card form the player's inventory.
	 * @param powerupCardToRemove powerup card to remove from the inventory.
	 */
	public void removePowerup(PowerupCard powerupCardToRemove) {
		powerupCards.remove(powerupCardToRemove);
	}

	/**
	 * Returns the AmmoContainer associated with this player.
	 * @return the AmmoContainer associated with this player.
	 */
	public AmmoContainer getAmmoContainer() {
		return ammoContainer;
	}

	/**
	 * Set isFlipped to TRUE if the player has no damage on the damageBoard.
	 * @return isFlipped.
	 */
	public boolean flipIfNoDamage(){
		if(damageBoard.size() == 0) {
			isFlipped = TRUE;
		}
		return isFlipped;
	}
}


/**
 * Thrown when a new item hasn't be added because the inventory has reached the max capacity.
 * @author Desno365
 */
class InventoryFullException extends RuntimeException {

	/**
	 * Constructs an InventoryFullException with the specified detail message.
	 * @param message the detail message.
	 */
	public InventoryFullException(String message) {
		super(message);
	}
}