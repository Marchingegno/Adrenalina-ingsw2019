package it.polimi.se2019.model.player;

import it.polimi.se2019.model.cards.ammo.AmmoContainer;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.utils.exceptions.InventoryFullException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player board and all the players information.
 * @author Desno365
 */
public class PlayerBoard {

	private String playerName;
	private List<Player> damageBoard;
	private List<Player> marks;
	private int numberOfDeaths;
	private int points;
	private boolean isFlipped; //Indicates if the damageBoard is flipped to the frenzy side.
	private AmmoContainer ammoContainer;
	private List<PowerupCard> powerupCards;
	private List<WeaponCard> weaponCards;
	private boolean hasChanged;


	/**
	 * Create an empty PlayerBoard ready to be used.
	 */
	public PlayerBoard(String playerName) {
		damageBoard = new ArrayList<>();
		marks = new ArrayList<>();
		numberOfDeaths = 0;
		this.playerName = playerName;
		points = 0;
		ammoContainer = new AmmoContainer();
		powerupCards = new ArrayList<>();
		weaponCards = new ArrayList<>();
		isFlipped = false;
		hasChanged = true;
	}


	/**
	 * Add damageBoard to the player and add previous marks to the damageBoard.
	 * @param shootingPlayer the player making the damageBoard.
	 * @param amountOfDamage amount of damageBoard tokens to add to the player.
	 * @throws IllegalArgumentException if the amount of damageBoard is negative.
	 */
	public void addDamage(Player shootingPlayer, int amountOfDamage) {
		if(amountOfDamage < 0)
			throw new IllegalArgumentException("amountOfDamage cannot be negative.");
		if(amountOfDamage == 0)
			return;

		// Add damageBoard
		for (int i = 0; i < amountOfDamage; i++)
			if(damageBoard.size() < GameConstants.OVERKILL_DAMAGE)
				damageBoard.add(shootingPlayer);

		int i;
		for (i = marks.size() - 1; i >= 0; i--) {
			if (marks.get(i).getPlayerName().equals(shootingPlayer.getPlayerName()) && damageBoard.size() < GameConstants.OVERKILL_DAMAGE) {
				damageBoard.add(shootingPlayer);
				marks.remove(i);
			}
		}
		Utils.logInfo("PlayerBoard -> addDamage(): Recorded to the player board that " + shootingPlayer.getPlayerName() + " dealt " + amountOfDamage + " direct damage and " + i + " mark damage to " + playerName);
		setChanged();
	}

	/**
	 * Add marks to the player.
	 * @param shootingPlayer the player making the damageBoard.
	 * @param amountOfMarks amount of marks to add to the player.
	 * @throws IllegalArgumentException if the amount of marks is negative.
	 */
	public void addMarks(Player shootingPlayer, int amountOfMarks) {
		if(amountOfMarks < 0)
			throw new IllegalArgumentException("amountOfMarks cannot be negative or zero.");
		if(amountOfMarks == 0)
			return;

		for (int i = 0; i < amountOfMarks; i++)
			// check if the shooting player doesn't have the max number of marks on the target player.
			if(marks.stream().filter(player -> player == shootingPlayer).count() < GameConstants.MAX_MARKS_PER_PLAYER)
				marks.add(shootingPlayer);

		Utils.logInfo("PlayerBoard -> addMarks(): Added " + amountOfMarks + " marks");
		setChanged();
	}

	/**
	 * Returns a copy of the current damageBoard board of the player.
	 *
	 * @return a copy of the List of damages done to the player.
	 */
	public List<Player> getDamageBoard() {
		return new ArrayList<>(damageBoard);
	}

	public boolean hasEnoughAmmo(List<AmmoType> ammoToCheck) {
		if (ammoToCheck == null || ammoToCheck.isEmpty()) {
			return true;
		}
		List<AmmoType> priceToPay = new ArrayList<>(ammoToCheck);
		List<PowerupCard> powerups = new ArrayList<>(powerupCards);
		Utils.logInfo("PlayerBoard -> hasEnoughAmmo(): trying to pay " + ammoToCheck + " with " + powerupCards + " and ammo " + getAmmoContainer().getAmmo());

		for (PowerupCard powerup : powerupCards) {
			Utils.logInfo("PlayerBoard -> hasEnoughAmmo(): this powerups " + powerups + " can be used to pay " + priceToPay + " ?");
			for (int i = priceToPay.size() - 1; i >= 0; i--) {
				if (priceToPay.get(i).equals(powerup.getAssociatedAmmo())) {
					priceToPay.remove(i);
					powerups.remove(powerup);
					Utils.logInfo("PlayerBoard -> hasEnoughAmmo(): can pay with " + powerup + " so it has been discarded");
					break;
				}
			}
		}

		if (priceToPay.isEmpty())
			return true;

		return this.ammoContainer.hasEnoughAmmo(priceToPay);
	}


	public boolean isFrenzy() {
		//TODO: Implementare metodo dopo l'implementazione del DamageStatus
		return false;
	}

	/**
	 * Returns true if and only if the board is flipped.
	 *
	 * @return true if and only if the board is flipped.
	 */
	public boolean isFlipped() {
		return isFlipped;
	}

	/**
	 * Returns a copy of the current marks of the player.
	 * @return a copy of the the List of marks done to the player.
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
		Utils.logInfo("PlayerBoard -> addPoints(): Added " + pointsToAdd + " points");
		setChanged();
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
	 * @throws UnsupportedOperationException when the player is not dead and this method is called.
	 */
	public void resetBoardAfterDeath() {
		if (!isDead())
			throw new UnsupportedOperationException("The player is not dead");

		numberOfDeaths++;

		//resets the damage board
		damageBoard = new ArrayList<>();
		Utils.logInfo("PlayerBoard -> resetBoardAfterDeath(): Damage board has been reset after death, now the player has " + numberOfDeaths + " deaths");
		setChanged();
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
		Utils.logInfo("PlayerBoard -> addWeapon(): Added to the player " + weaponToAdd.getCardName());
		setChanged();
	}

	/**
	 * Returns a copy of the list of weapon cards in player's inventory.
	 * @return a copy of the list of weapon cards in player's inventory.
	 */
	public List<WeaponCard> getWeaponCards() {
		return new ArrayList<>(weaponCards);
	}

	/**
	 * Returns the number of weapons.
	 * @return the number of weapons.
	 */
	public int numOfWeapons(){return weaponCards.size();
	}

	/**
	 * Removes the weapon in the specified index.
	 * @param indexOfTheWeapon index of the weapon to remove.
	 * @return the weapon card removed.
	 * @throws IllegalArgumentException when the index is out of the list of weapons.
	 */
	public WeaponCard removeWeapon(int indexOfTheWeapon) {
		if (indexOfTheWeapon < 0 || indexOfTheWeapon > numOfWeapons())
			throw new IllegalArgumentException("Illegal index");
		Utils.logInfo("PlayerBoard -> removeWeapon(): Removing " + weaponCards.get(indexOfTheWeapon));
		return weaponCards.remove(indexOfTheWeapon);
	}

	/**
	 * Remove and returns the weaponCardToDrop from the inventory while adding weaponToGrab.
	 * @param weaponToGrab weapon card to add to the inventory.
	 * @param indexOfTheWeaponCardToDrop index of the weapon card to remove from the inventory.
	 * @return the weapon card to drop.
	 * @throws IllegalArgumentException if the weapon to remove is not present in the player's inventory.
	 */
	public WeaponCard swapWeapon(WeaponCard weaponToGrab, int indexOfTheWeaponCardToDrop) {
		if (indexOfTheWeaponCardToDrop < 0 || indexOfTheWeaponCardToDrop > numOfWeapons())
			throw new IllegalArgumentException("weaponCardToDrop is not owned by the player.");
		WeaponCard weaponToDrop = weaponCards.remove(indexOfTheWeaponCardToDrop);
		addWeapon(weaponToGrab);
		Utils.logInfo("PlayerBoard -> swapWeapon(): Swaped " + weaponToDrop.getCardName() + " with " + weaponToGrab.getCardName());
		setChanged();
		return weaponToDrop;
	}

	/**
	 * Add the powerup card to the player's inventory.
	 * @param powerupToAdd powerup card to add to the player inventory.
	 * @throws InventoryFullException if the inventory is already full.
	 */
	public void addPowerup(PowerupCard powerupToAdd) {
		if(powerupCards.size() >= GameConstants.MAX_POWERUP_CARDS_PER_PLAYER) {
			Utils.logError("Cannot add another powerup card since the inventory is full.", new InventoryFullException(""));
		}
		powerupCards.add(powerupToAdd);
		Utils.logInfo("PlayerBoard -> addPowerup(): Added to the player " + powerupToAdd.getCardName());
		setChanged();
	}

	/**
	 * Returns a copy of the powerup cards in player's inventory.
	 * @return a copy of the powerup cards in player's inventory.
	 */
	public List<PowerupCard> getPowerupCards() {
		return new ArrayList<>(powerupCards);
	}

	/**
	 * Remove the powerup card form the player's inventory.
	 * @param indexOfPowerCardToRemove index of the powerup card to remove from the inventory.
	 * @return the removed powerup that should be added to the discarded deck.
	 */
	public PowerupCard removePowerup(int indexOfPowerCardToRemove) {
		Utils.logInfo("PlayerBoard -> removePowerup(): Removing " + powerupCards.get(indexOfPowerCardToRemove).getCardName() + " from the playerBoard");
		PowerupCard powerupCard = powerupCards.get(indexOfPowerCardToRemove);
		powerupCards.remove(indexOfPowerCardToRemove);
		setChanged();
		return powerupCard;
	}

	/**
	 * Returns the AmmoContainer associated with this player.
	 * @return the AmmoContainer associated with this player.
	 */
	public AmmoContainer getAmmoContainer() {
		return ammoContainer;
	}

	/**
	 * Set isFlipped to true if the player has no damage on the damageBoard.
	 * @return isFlipped.
	 */
	public boolean flipIfNoDamage() {
		if (damageBoard.isEmpty()) {
			isFlipped = true;
			setChanged();
			Utils.logInfo("PlayerBoard -> flipIfNoDamage(): Board has been flipped");
		}
		return isFlipped;
	}

	/**
	 * Sets the square as changed.
	 */
	public void setChanged() {
		hasChanged = true;
	}

	/**
	 * Sets the square as not changed.
	 */
	public void setNotChanged() {
		hasChanged = false;
	}

	/**
	 * Returns true if and only if the player board has changed.
	 *
	 * @return true if and only if the player board has changed.
	 */
	public boolean hasChanged() {
		return hasChanged;
	}
}


