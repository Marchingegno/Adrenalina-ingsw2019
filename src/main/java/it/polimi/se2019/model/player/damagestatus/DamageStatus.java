package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.utils.ActionType;
import it.polimi.se2019.utils.MacroAction;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the status of the player that decides which actions it can take.
 * @author Marchingegno
 */
public abstract class DamageStatus {
	int numberOfActionsPerTurn; //number of actions that a player with this status can perform in a turn.
	int numberOfActionsToPerform; //actions that the player performed in this turn.
	private int currentActionIndex; //Action currently in execution.
	List<MacroAction> availableActions;


	public List<MacroAction> getAvailableActions(){
		return new ArrayList<>(availableActions);
	}

	int getNumberOfActionsPerTurn() {
		return numberOfActionsPerTurn;
	}

	int getNumberOfActionsToPerform() {
		return numberOfActionsToPerform;
	}

	public void decreaseActionsToPerform(){
		if(numberOfActionsToPerform == 0)
			throw new IllegalStateException("numberOfActionsToPerform is already zero!");

		numberOfActionsToPerform--;
	}

	public void setCurrentActionIndex(int currentActionIndex) {
		this.currentActionIndex = currentActionIndex;
	}

	public int getCurrentActionIndex() {
		return currentActionIndex;
	}

	public ActionType executeAction(){
		return availableActions.get(currentActionIndex).execute();
	}

	/**
	 * This method will be called at the start of a turn if there is no need to setChanged the status of the player.
	 */
	public void refillActions()
	{
		numberOfActionsToPerform = numberOfActionsPerTurn;
	}

}