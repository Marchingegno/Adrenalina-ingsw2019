package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.List;

public class VortexCannon extends OptionalEffect {

	public VortexCannon(String description, List<AmmoType> reloadPrice) {
		super("Vortex Cannon", description, reloadPrice);
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