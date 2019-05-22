package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
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
		this.optional2Damage = 2;
		this.optional2Marks = 0;
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(optional2Damage, optional2Marks));
	}


	public Cyberblade(JsonObject parameters) {
		super(parameters);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.optional2Damage = parameters.get("optional2Damage").getAsInt();
		this.optional2Marks = parameters.get("optional2Marks").getAsInt();
		this.standardDamagesAndMarks.add(new DamageAndMarks(getPrimaryDamage(), getPrimaryMarks()));
		this.standardDamagesAndMarks.add(new DamageAndMarks(optional2Damage, optional2Marks));
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