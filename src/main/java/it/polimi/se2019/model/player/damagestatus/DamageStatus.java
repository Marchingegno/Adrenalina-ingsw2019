package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.model.Representable;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.utils.ActionType;
import it.polimi.se2019.utils.MacroAction;

import java.util.List;

/**
 * This class represents the status of the player that decides which actions it can take.
 * @author Marchingegno
 */
public abstract class DamageStatus implements Representable {
	int numberOfActionsPerTurn; //number of actions that a player with this status can perform in a turn.
	int numberOfActionsPerformed; //actions that the player performed in this turn.
	private int currentActionIndex; //Action currently in execution.
	private DamageStatusRep damageStatusRep;
	List<MacroAction> availableActions;

	public boolean hasMacroActionLeft(){
		return (numberOfActionsPerformed > 0);
	}

	public List<MacroAction> getAvailableMacroActions(){
		return availableActions;
	}

	public MacroAction getCurrentMacroAction() {
		return availableActions.get(currentActionIndex);
	}

	int getNumberOfMacroActionsPerTurn() {
		return numberOfActionsPerTurn;
	}

	int getNumberOfMacroActionsPerformed() {
		return numberOfActionsPerformed;
	}

	public void decreaseMacroActionsToPerform(){
		if(numberOfActionsPerformed == 0)
			throw new IllegalStateException("numberOfActionsPerformed is already zero!");

		numberOfActionsPerformed--;
	}

	public void setCurrentMacroActionIndex(int currentActionIndex) {
		this.currentActionIndex = currentActionIndex;
	}

	public int getCurrentMacroActionIndex() {
		return currentActionIndex;
	}

	public ActionType getNextActionToExecute(){
		return availableActions.get(currentActionIndex).getNextActionToExecute();
	}

	/**
	 * This method will be called at the start of a turn if there is no need to setChanged the status of the player.
	 */
	public void refillMacroActions()
	{
		numberOfActionsPerformed = numberOfActionsPerTurn;
	}

	@Override
	public Representation getRep() {
		if (damageStatusRep == null)
			damageStatusRep = new DamageStatusRep(this);
		return damageStatusRep;
	}
}