package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

public abstract class WeaponCard extends Card {

	List<DamageAndMarks> standardDamagesAndMarks;
	private Player owner;
	private boolean loaded;
	//private ArrayList<AmmoType> grabPrice; It can be deduced by reloadPrice minus the first occurrence.
	private List<AmmoType> reloadPrice;


	public WeaponCard(String description, List<AmmoType> reloadPrice) {

		super(description);
		owner = null;
		loaded = TRUE;
		this.reloadPrice = reloadPrice;
	}

	/**
	 * This method will be called by the damage-dealer methods of the weapons. (ex: primaryFire, alternateFire)
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

	public Player getOwner(){
		return owner;
	}

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


	public void load() {
		this.loaded = true;
	}

	public abstract void primaryFire(List<Player> playersToShoot);

	public void getAvailableOptions(){
		Utils.logInfo(getDescription());
	}




	public ArrayList<Player> getPrimaryTargets() {
		return null;
	}

}