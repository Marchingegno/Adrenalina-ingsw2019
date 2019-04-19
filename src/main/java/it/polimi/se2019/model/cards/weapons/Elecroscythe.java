package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

import java.util.List;

public final class Elecroscythe extends AlternateFire {

	public Elecroscythe(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}


	public List<Player> primaryFire() {
		//Deal damage to everyone on your square
		return getPrimaryTargets();

	}

	public List<Player> secondaryFire() {
		return getSecondaryTargets();
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return null;
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return null;
	}
}