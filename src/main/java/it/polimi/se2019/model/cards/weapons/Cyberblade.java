package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Cyberblade extends OptionalEffectsWeapon {
	private ActionType currentActionType;


	public Cyberblade(String description, List<AmmoType> reloadPrice) {
		super("Cyberblade", description, reloadPrice, 0, 2, 1);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.OPTIONAL2_DAMAGE = 2;
		this.OPTIONAL2_MARKS = 0;
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(OPTIONAL2_DAMAGE, OPTIONAL2_MARKS));
	}

	public Cyberblade(String description){
		this(description, null);
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(isOptionalActive(1)&&getCurrentStep() == 2){
			return getActionTypeQnO();
		}
		if(getCurrentStep() == 3){
		}
		return null;
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return null;
	}

	@Override
	public void optional1Fire() {

	}

	@Override
	public void optional2Fire() {

	}

}