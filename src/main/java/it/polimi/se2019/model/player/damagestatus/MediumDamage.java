package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.utils.MacroActionBuilder;

import java.util.ArrayList;

import static it.polimi.se2019.utils.GameConstants.NUMBER_OF_ACTIONS_PER_TURN;

public class MediumDamage extends DamageStatus {
	public static final int numberOfActions = NUMBER_OF_ACTIONS_PER_TURN;

	public MediumDamage(){
		MacroActionBuilder runAroundBuilder = new MacroActionBuilder();
		MacroActionBuilder grabStuffBuilder = new MacroActionBuilder();
		MacroActionBuilder shootPeopleBuilder = new MacroActionBuilder();
		availableActions = new ArrayList<>();

		runAroundBuilder.setMovementDistance(3);
		runAroundBuilder.setName("Move");
		availableActions.add(runAroundBuilder.build());

		grabStuffBuilder.setMovementDistance(2);
		grabStuffBuilder.setGrabAction(true);
		grabStuffBuilder.setName("Grab");
		availableActions.add(grabStuffBuilder.build());

		shootPeopleBuilder.setShootAction(true);
		shootPeopleBuilder.setName("Shoot");
		availableActions.add(shootPeopleBuilder.build());

		numberOfMacroActionsPerTurn = NUMBER_OF_ACTIONS_PER_TURN;
		numberOfMacroActionsToPerform = numberOfMacroActionsPerTurn;
	}

	@Override
	public String toString() {
		return "MEDIUM DAMAGE";
	}

}