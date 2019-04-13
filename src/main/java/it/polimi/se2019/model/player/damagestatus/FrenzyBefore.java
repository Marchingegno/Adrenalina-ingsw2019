package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.MacroActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class FrenzyBefore implements DamageStatus {
	private ArrayList<MacroAction> availableActions;

	public FrenzyBefore(){
		MacroActionBuilder shootPeopleBuilder = new MacroActionBuilder();
		MacroActionBuilder runAroundBuilder = new MacroActionBuilder();
		MacroActionBuilder grabStuffBuilder = new MacroActionBuilder();
		availableActions = new ArrayList<>();

		shootPeopleBuilder.setMovementDistance(1);
		shootPeopleBuilder.setReloadAction(true);
		shootPeopleBuilder.setShootAction(true);
		availableActions.add(shootPeopleBuilder.build());

		runAroundBuilder.setMovementDistance(4);
		availableActions.add(runAroundBuilder.build());

		grabStuffBuilder.setMovementDistance(2);
		grabStuffBuilder.setGrabAction(true);
		availableActions.add(grabStuffBuilder.build());
	}

	@Override
	public List<MacroAction> getAvailableActions() {
		return new ArrayList<>(availableActions);
	}

	@Override
	public void doAction() {
	}

}