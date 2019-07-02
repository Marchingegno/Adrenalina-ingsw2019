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
	private int numberOfMacroActionsPerTurn; // Number of actions that a player with this status can perform in a turn.
	private int numberOfMacroActionsToPerform; // Actions that the player still has to perform in this turn.
	private int currentActionIndex; //Action currently in execution.
	List<MacroAction> availableActions;
	private boolean hasChanged = true;

	public boolean hasMacroActionLeft(){
		return (numberOfMacroActionsToPerform > 0);
	}

	public List<MacroAction> getAvailableMacroActions(){
		return availableActions;
	}

	public MacroAction getCurrentMacroAction() {
		return availableActions.get(currentActionIndex);
	}

	int getNumberOfMacroActionsPerTurn() {
		return numberOfMacroActionsPerTurn;
	}

	int getNumberOfMacroActionsToPerform() {
		return numberOfMacroActionsToPerform;
	}

	public void decreaseMacroActionsToPerform(){
		if(numberOfMacroActionsToPerform == 0)
			throw new IllegalStateException("numberOfMacroActionsToPerform is already zero!");

		numberOfMacroActionsToPerform--;
		setChanged();
	}

	public void setCurrentMacroActionIndex(int currentActionIndex) {
		this.currentActionIndex = currentActionIndex;
		setChanged();
	}

	public int getCurrentMacroActionIndex() {
		return currentActionIndex;
	}

	public ActionType getNextActionToExecuteAndAdvance() {
		return availableActions.get(currentActionIndex).getNextActionToExecuteAndAdvance();
	}

	/**
	 * This method will be called at the start of a turn if there is no need to setChanged the status of the player.
	 */
	public void refillMacroActions()
	{
		numberOfMacroActionsToPerform = numberOfMacroActionsPerTurn;
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
		return new DamageStatusRep(this);
	}

	public abstract boolean isFrenzy();

	void setNumberOfMacroActionsPerTurn(int numberOfMacroActionsPerTurn) {
		this.numberOfMacroActionsPerTurn = numberOfMacroActionsPerTurn;
	}

	void setNumberOfMacroActionsToPerform(int numberOfMacroActionsToPerform) {
		this.numberOfMacroActionsToPerform = numberOfMacroActionsToPerform;
	}
}