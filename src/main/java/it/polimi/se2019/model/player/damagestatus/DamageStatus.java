package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.utils.MacroAction;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the status of the player that decides which actions it can take.
 * @author Marchingegno
 */
public abstract class DamageStatus {
	int numberOfActionsPerTurn; //number of actions that a player with this status can perform in a turn.
	int numberOfActionsPerformed; //actions that the player performed in this turn.
	ArrayList<MacroAction> availableActions;


	public List<MacroAction> getAvailableActions(){
		return new ArrayList<>(availableActions);
	}

	abstract void doAction();

	public int getNumberOfActionsPerTurn() {
		return numberOfActionsPerTurn;
	}

	public int getNumberOfActionsPerformed() {
		return numberOfActionsPerformed;
	}

	/**
	 * This method will be called at the start of a turn if there is no need to setChanged the status of the player.
	 */
	public void refillActions()
	{
		numberOfActionsPerformed = numberOfActionsPerTurn;
	}

}