package it.polimi.se2019.model.cards.weapons;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public final class FlameThrower extends AlternateFire {
	private final int PRIMARY_DAMAGE = 1;
	private final int PRIMARY_MARKS = 0;
	private final int SECONDARY_DAMAGE = 2;
	private final int SECONDARY_MARKS = 0;
	private ArrayList<DamageAndMarks> secondaryDamagesAndMarks;

	public FlameThrower(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		secondaryDamagesAndMarks = new ArrayList<>();
		secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));
		secondaryDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
	}


	public List<Player> primaryFire() {
	}

	public List<Player> secondaryFire() {

	}


}