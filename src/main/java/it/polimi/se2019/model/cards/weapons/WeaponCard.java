package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

/**
 * Abstract class that defines the structure of a weapon card. Every subtype of weapon must extend this.
 * The interaction with the player starts with chooseFireMode(), that asks the player whether or not does he want to use
 * alternate fire modes or optional effects. These choices are then collected into an array of booleans "flags" and
 * handleFire() is called; this method contains the logic for firing the weapon based on the collected flags, or delegates it.
 * It then proceeds to call adequate methods, such as primaryFire(), to build an array of target players, enhancing the damage
 * and/or the marks dealt with this shot, and finally it calls dealDamage that deals damage adequately to the player(s)
 * chosen as targets. It then reset() the weapon.
 * @author Marchingegno
 */
public abstract class WeaponCard extends Card {

	GameMap gameMap;
	boolean moveActive;
	int moveDistance;
	List<DamageAndMarks> standardDamagesAndMarks;
	private Player owner;
	private boolean loaded;
	private String weaponName;
	//private ArrayList<AmmoType> grabPrice; It can be deduced by reloadPrice minus the first occurrence.
	private final List<AmmoType> reloadPrice;

	/*
	//TODO: Merge constructor
	public WeaponCard(String weaponName, String description) {
		super(description);
		this.weaponName = weaponName;
	}
	*/

	public WeaponCard(String description, List<AmmoType> reloadPrice) {

		super(description);
		owner = null;
		loaded = TRUE;
		this.reloadPrice = reloadPrice;
	}

	/**
	 *Returns the owner of the weapon.
	 * @return the owner of the weapon.
	 */
	public Player getOwner(){
		return owner;
	}

	/**
	 * Returns the reload price of this weapon.
	 * @return Reload price of this weapon.
	 */
	public List<AmmoType> getReloadPrice() {
		return new ArrayList<>(reloadPrice);
	}

	/**
	 * Returns the grab price for this weapon. It consists of the reload price minus the first occurrence.
	 * @return grab price for this weapon.
	 */
	public List<AmmoType> getGrabPrice(){
		return new ArrayList<>(reloadPrice.subList(1, reloadPrice.size() - 1));
	}

	/**
	 * Returns whether or not the weapon is loaded.
	 * @return whether or not the weapon is loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Called by the controller when this weapon is grabbed by a player.
	 * @param grabbingPlayer the grabbing player.
	 */
	public void setOwner(Player grabbingPlayer){
		this.owner = grabbingPlayer;
	}


	/**
	 * Loads the weapon.
	 */
	public void load() {
		//TODO: Implement
		this.loaded = true;
	}

	/**
	 * Interacts with player, depending on the characteristic of the weapon. It then collects an array
	 * of flags which each weapon handles in their own way.
	 * Most likely will be Overridden.
	 */
	public void chooseFireMode(){
		Boolean[] flagsCollected;
		//Placeholder initialization
		flagsCollected = new Boolean[]{ false, false};

		//TODO: Interaction with the view, flags will be modified.

		handleFire(flagsCollected);
	}

	/**
	 * Handles interaction with flags array.
	 * @param flags which options the player has chosen.
	 */
	protected abstract void handleFire(Boolean[] flags);

	/**
	 * This method will be called by the damage-dealer methods of the weapons.
	 * The two arrays are ordered in such a way that the i-th playerToShoot will be dealt the i-th damageAndMark.
	 * This method does NOT deload the weapon.
	 * This method does check only the first array's size, so it can happen that playersToShoot is shorter than
	 * damagesAndMarks. This means that the player has not activated an optional effect.
	 * @param playersToShoot the array of players that will receive damage and/or marks.
	 * @param damagesAndMarks the damage and marks for each player. It doesn't use the standard attribute because the
	 *                        amount of damage or marks can be changed at run-time, depending on the choices of the
	 *                        player.
	 */
	protected void dealDamage(List<Player> playersToShoot, List<DamageAndMarks> damagesAndMarks){
		for (int i = 0; i < playersToShoot.size(); i++) {
			playersToShoot.get(i).getPlayerBoard().addDamage(owner, damagesAndMarks.get(i).getDamage());
			playersToShoot.get(i).getPlayerBoard().addDamage(owner, damagesAndMarks.get(i).getMarks());
		}
	}


	/**
	 * Primary method of firing of the weapon. It interacts with the view and collects targeted players for its mode.
	 */
	public abstract List<Player> primaryFire();

	public void getAvailableOptions(){
		Utils.logInfo(getDescription());
	}

	/**
	 * Get the targets of the primary mode of fire for this weapon.
	 * @return the targettable players.
	 */
	public abstract List<Player> getPrimaryTargets();

	/**
	 * It asks at the shooting player which target do he want to fire at.
	 * Sometimes there is no need to ask because, depending on the weapon, the target will be chosen automatically.
	 * @return list of players chosen by the shooting player.
	 */
	public List<Player> chooseTargets(){
		//TODO: Interaction with view.
		return getPrimaryTargets();
	}


	/**
	 * Deloads the weapon and reset eventually modified parameters.
	 */
	void reset(){
		moveActive = false;
		loaded = false;
	}

}