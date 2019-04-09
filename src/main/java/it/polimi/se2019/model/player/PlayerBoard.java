package it.polimi.se2019.model.player;

import it.polimi.se2019.model.cards.ammo.AmmoContainer;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.weapons.WeaponCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player board and all the players information.
 * @author Desno365
 */
public class PlayerBoard {

	public static final int DEATH_DAMAGE = 11;
	private static final int MAX_MARKS_PER_PLAYER = 3;
	private static final int MAX_WEAPON_CARDS_PER_PLAYER = 3;
	private static final int MAX_POWERUP_CARDS_PER_PLAYER = 3;

	private ArrayList<Player> damage;
	private ArrayList<Player> marks;
	private int numberOfDeaths;
	private int points;

	private AmmoContainer ammoContainer;
	private ArrayList<PowerupCard> powerupCards;
	private ArrayList<WeaponCard> weaponCards;


	/**
	 * Create an empty PlayerBoard ready to be used.
	 */
	public PlayerBoard() {
		damage = new ArrayList<>();
		marks = new ArrayList<>();
		numberOfDeaths = 0;
		points = 0;
		ammoContainer = new AmmoContainer();
		powerupCards = new ArrayList<>();
		weaponCards = new ArrayList<>();
	}


	/**
	 * Add damage to the player and add previous marks to the damage.
	 * @param shootingPlayer the player making the damage.
	 * @param amountOfDamage amount of damage tokens to add to the player.
	 * @throws IllegalArgumentException if the amount of damage to add is negative.
	 */
	public void addDamage(Player shootingPlayer, int amountOfDamage) {
		if(amountOfDamage < 0)
			throw new IllegalArgumentException("amountOfDamage cannot be negative.");

		// Add damage
		for (int i = 0; i < amountOfDamage; i++)
			damage.add(shootingPlayer);

		// Add marks
		for (int i = 0; i < MAX_MARKS_PER_PLAYER; i++) {
			int index = marks.indexOf(shootingPlayer);
			if(index == -1) {
				break;
			} else {
				damage.add(shootingPlayer);
				marks.remove(index);
			}
		}
	}

	/**
	 * Add marks to the player.
	 * @param shootingPlayer the player making the damage.
	 * @param amountOfMarks amount of marks to add to the player.
	 * @throws IllegalArgumentException if the amount of marks to add is negative.
	 */
	public void addMarks(Player shootingPlayer, int amountOfMarks) {
		if(amountOfMarks < 0)
			throw new IllegalArgumentException("amountOfMarks cannot be negative.");

		for (int i = 0; i < amountOfMarks; i++)
			// check if the shooting player doesn't have the max number of marks on the target player.
			if(marks.stream().filter(player -> player == shootingPlayer).count() < MAX_MARKS_PER_PLAYER)
				marks.add(shootingPlayer);
	}

	private void resetDamage() {
		damage = new ArrayList<>();
	}

	/**
	 * Returns true if the player is dead.
	 * @return true if the player is dead.
	 */
	public boolean isDead() {
		return damage.size() >= DEATH_DAMAGE;
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
	 * Process the death of the player and give points to other players that shot this player.
	 */
	public void score() {
		if(isDead()) {
			numberOfDeaths++;
			resetDamage();
			// TODO give score or maybe done by controller
		}
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
		if(weaponCards.size() >= MAX_WEAPON_CARDS_PER_PLAYER)
			throw new InventoryFullException("Cannot add another weapon card since the inventory is full. Use swapWeapon to change the weapon.");
		weaponCards.add(weaponToAdd);
	}

	/**
	 * Returns the weapon cards in player's inventory.
	 * @return the weapon cards in player's inventory.
	 */
	public List<WeaponCard> getWeaponCards() {
		List<WeaponCard> clone = new ArrayList<>(weaponCards.size());
		clone.addAll(weaponCards);
		return clone;
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
		if(powerupCards.size() >= MAX_POWERUP_CARDS_PER_PLAYER)
			throw new InventoryFullException("Cannot add another powerup card since the inventory is full.");
		powerupCards.add(powerupToAdd);
	}

	/**
	 * Return the powerup cards in player's inventory.
	 * @return the powerup cards in player's inventory.
	 */
	public List<PowerupCard> getPowerupCards() {
		List<PowerupCard> clone = new ArrayList<>(powerupCards.size());
		clone.addAll(powerupCards);
		return clone;
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

}

/**
 * Thrown when a new item hasn't be added because the inventory has reached the max capacity.
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