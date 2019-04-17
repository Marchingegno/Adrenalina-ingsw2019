package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public final class Cyberblade extends OptionalEffect {
	private final int PRIMARY_DAMAGE = 2;
	private final int PRIMARY_MARKS = 0;
	private final int OPTIONAL2_DAMAGE = 2;
	private final int OPTIONAL2_MARKS = 0;


	public Cyberblade(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(OPTIONAL2_DAMAGE, OPTIONAL2_MARKS));

	}

	@Override
	public void getAvailableOptions() {
		super.getAvailableOptions();

	}

	@Override
	public void optionalEffect1() {
	}

	@Override
	public void primaryFire(List<Player> playersToShoot) {
		//If there are two player in the list of targets, it means that the secondary
		//effect has been activated.
		dealDamage(playersToShoot, this.standardDamagesAndMarks);

		}

	}
}