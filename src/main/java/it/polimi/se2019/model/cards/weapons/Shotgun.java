package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Shotgun extends AlternateFire {

	public Shotgun(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}


	@Override
	Pair handlePrimaryFire(int choice) {
		return null;
	}

	@Override
	Pair handleSecondaryFire(int choice) {
		return null;
	}

	public void primaryFire() {
	}

	public void secondaryFire() {
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