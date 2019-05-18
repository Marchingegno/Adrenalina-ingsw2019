package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.List;

public class Railgun extends AlternateFire {

	public Railgun(String description, List<AmmoType> reloadPrice) {
		super("Railgun", description, reloadPrice);
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		return null;
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

	public void secondaryFire() {
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return null;
	}


}