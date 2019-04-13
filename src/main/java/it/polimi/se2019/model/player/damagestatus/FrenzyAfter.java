package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.MacroActionBuilder;

import java.util.ArrayList;
import java.util.List;

public class FrenzyAfter implements DamageStatus {
	private ArrayList<MacroAction> availableActions;

	public FrenzyAfter(){
		MacroActionBuilder shootPeopleBuilder = new MacroActionBuilder();
		MacroActionBuilder grabStuffBuilder = new MacroActionBuilder();
		availableActions = new ArrayList<>();

		shootPeopleBuilder.setMovementDistance(2);
		shootPeopleBuilder.setReloadAction(true);
		shootPeopleBuilder.setShootAction(true);
		availableActions.add(shootPeopleBuilder.build());

		grabStuffBuilder.setMovementDistance(3);
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

	@Override
	public int getNumberOfActions() {
		return GameConstants.FRENZY_AFTER_NUMBER_OF_ACTION_PER_TURN;
	}

}