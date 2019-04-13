package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.MacroActionBuilder;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2019.utils.GameConstants.NUMBER_OF_ACTIONS_PER_TURN;

public class LowDamage implements DamageStatus {
	public final int numberOfActions = NUMBER_OF_ACTIONS_PER_TURN;
	private ArrayList<MacroAction> availableActions;

	public LowDamage(){
		MacroActionBuilder runAroundBuilder = new MacroActionBuilder();
		MacroActionBuilder grabStuffBuilder = new MacroActionBuilder();
		MacroActionBuilder shootPeopleBuilder = new MacroActionBuilder();
		availableActions = new ArrayList<>();

		runAroundBuilder.setMovementDistance(3);
		availableActions.add(runAroundBuilder.build());

		grabStuffBuilder.setMovementDistance(1);
		grabStuffBuilder.setGrabAction(true);
		availableActions.add(grabStuffBuilder.build());

		shootPeopleBuilder.setShootAction(true);
		availableActions.add(shootPeopleBuilder.build());
	}

	@Override
	public List<MacroAction> getAvailableActions() {
		return new ArrayList<>(availableActions);
	}

	@Override
	public void doAction() {
	}

}