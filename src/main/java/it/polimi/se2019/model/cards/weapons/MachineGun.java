package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.player.Player;

import java.util.List;

public class MachineGun extends OptionalEffect {

	public MachineGun(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}


	public void primaryFire() {
	}

	public void optionalEffect1(List<Player> targetPlayers, List<DamageAndMarks> damageAndMarksList) {
	}

	public void optionalEffect2(List<Player> targetPlayers, List<DamageAndMarks> damageAndMarksList) {
	}

}