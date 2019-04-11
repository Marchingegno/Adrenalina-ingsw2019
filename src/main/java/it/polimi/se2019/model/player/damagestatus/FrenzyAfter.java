package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.MacroActionBuilder;

import java.util.ArrayList;

import static it.polimi.se2019.utils.GameConstants.FRENZY_AFTER_NUMBER_OF_ACTION_PER_TURN;

public class FrenzyAfter implements DamageStatus {
	public final int numberOfActions = FRENZY_AFTER_NUMBER_OF_ACTION_PER_TURN;
	private ArrayList<MacroAction> availableActions;

	public FrenzyAfter(){
		MacroActionBuilder shootPeopleBuilder = new MacroActionBuilder();
		MacroActionBuilder grabStuffBuilder = new MacroActionBuilder();

		shootPeopleBuilder.setMovementDistance(2);
		shootPeopleBuilder.setReloadAction(true);
		shootPeopleBuilder.setShootAction(true);
		availableActions.add(shootPeopleBuilder.build());

		grabStuffBuilder.setMovementDistance(3);
		grabStuffBuilder.setGrabAction(true);
		availableActions.add(grabStuffBuilder.build());
	}


	@Override
	public void getAvailableActions() {
	}

	@Override
	public void doAction() {
	}

}