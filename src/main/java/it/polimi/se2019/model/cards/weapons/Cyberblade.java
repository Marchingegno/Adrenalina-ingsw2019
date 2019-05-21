package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Cyberblade extends OptionalEffectsWeapon {
	private currentAction


	public Cyberblade(String description, List<AmmoType> reloadPrice) {
		super("Cyberblade", description, reloadPrice);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.PRIMARY_DAMAGE = 2;
		this.PRIMARY_MARKS = 0;
		this.OPTIONAL2_DAMAGE = 2;
		this.OPTIONAL2_MARKS = 0;
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(OPTIONAL2_DAMAGE, OPTIONAL2_MARKS));
		this.MOVE_DISTANCE = 1;
	}

	public Cyberblade(String description){
		this(description, null);
	}



	@Override
	QuestionContainer handlePrimaryFire(int choice) {
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