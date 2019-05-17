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
	private boolean hasChanged = true;

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
		setChanged();
	}

	public void setCurrentMacroActionIndex(int currentActionIndex) {
		this.currentActionIndex = currentActionIndex;
		setChanged();
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
		setChanged();
	}

	/**
	 * Sets the square as changed.
	 */
	public void setChanged() {
		hasChanged = true;
	}

	/**
	 * Sets the square as not changed.
	 */
	public void setNotChanged() {
		hasChanged = false;
	}

	/**
	 * Returns true if and only if the player board has changed.
	 *
	 * @return true if and only if the player board has changed.
	 */
	public boolean hasChanged() {
		return hasChanged;
	}

	@Override
	public Representation getRep() {
		if (damageStatusRep == null)
			damageStatusRep = new DamageStatusRep(this);
		return damageStatusRep;
	}
}