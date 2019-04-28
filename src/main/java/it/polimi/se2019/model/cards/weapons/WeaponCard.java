package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class WeaponCard extends Card {

	private boolean loaded;
	private String weaponName;
	private ArrayList<AmmoType> grabPrice;
	private ArrayList<AmmoType> reloadPrice;

	public WeaponCard(String weaponName, String description) {
		super(description);
		this.weaponName = weaponName;
	}

	// TODO remove
	public WeaponCard(String description) {
		super(description);
		this.weaponName = "TODO";
	}


	public abstract void shoot();

	protected abstract void primaryFire();

	public boolean isLoaded() {
		return loaded;
	}

	public void load() {
	}

	public void getAvailableOptions() {
	}

	public List<Player> getTargettablePlayers() {
		return null;
	}

	public List<AmmoType> getGrabPrice() {
		return grabPrice;
	}

	public List<AmmoType> getReloadPrice() {
		return reloadPrice;
	}

	public String getWeaponName() {
		return weaponName;
	}
}