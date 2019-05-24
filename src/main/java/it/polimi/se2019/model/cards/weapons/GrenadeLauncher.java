package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonObject;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.List;

public class GrenadeLauncher extends OptionalEffectsWeapon {

	public GrenadeLauncher(JsonObject parameters) {
		super(parameters);
	}


	public void primaryFire() {
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
	public void optional1Fire() {

	}

	@Override
	public void optional2Fire() {

	}

	@Override
	public boolean canBeActivated() {
		return true;
	}
}