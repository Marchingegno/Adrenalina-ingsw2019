package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.List;

public class PlasmaGun extends OptionalEffect {

	public PlasmaGun(String description, List<AmmoType> reloadPrice) {
		super("Plasma Gun", description, reloadPrice);
	}


	public void primaryFire() {
	}

	@Override
	Pair handlePrimaryFire(int choice) {
		return null;
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return null;
	}

	@Override
	public void optionalEffect1() {

	}

	@Override
	public void optionalEffect2() {

	}


}