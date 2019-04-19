package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.player.Player;

import java.util.List;

public final class Railgun extends AlternateFire {

	public Railgun(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}


	public List<Player> primaryFire() {
	}

	public List<Player> secondaryFire() {
	}


}