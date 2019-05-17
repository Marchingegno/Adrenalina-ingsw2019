package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.utils.MacroActionBuilder;

import java.util.ArrayList;

import static it.polimi.se2019.utils.GameConstants.FRENZY_AFTER_NUMBER_OF_ACTION_PER_TURN;

public class FrenzyAfter extends DamageStatus {


	public FrenzyAfter(){
		MacroActionBuilder shootPeopleBuilder = new MacroActionBuilder();
		MacroActionBuilder grabStuffBuilder = new MacroActionBuilder();
		availableActions = new ArrayList<>();

		shootPeopleBuilder.setMovementDistance(2);
		shootPeopleBuilder.setReloadAction(true);
		shootPeopleBuilder.setShootAction(true);
		shootPeopleBuilder.setName("Shoot");
		availableActions.add(shootPeopleBuilder.build());

		grabStuffBuilder.setMovementDistance(3);
		grabStuffBuilder.setGrabAction(true);
		grabStuffBuilder.setName("Grab");
		availableActions.add(grabStuffBuilder.build());

		numberOfMacroActionsPerTurn = FRENZY_AFTER_NUMBER_OF_ACTION_PER_TURN;
		numberOfMacroActionsToPerform = FRENZY_AFTER_NUMBER_OF_ACTION_PER_TURN;
	}

}