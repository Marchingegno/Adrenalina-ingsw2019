package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.utils.MacroAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of DamageStatus class.
 * @author Marchingegno
 */
public class DamageStatusRep implements Representation {
	private int numberOfMacroActionsPerTurn; // Number of actions that a player with this status can perform in a turn.
	private int numberOfMacroActionsToPerform; // Actions that the player still has to perform in this turn.
	private List<String> macroActionString;
	private List<String> macroActionNames;
	/*Note: availableActions contains the reference of the MacroActions. In the current state of the project, there is
	no need no clone them, since they are immutable.*/


	public DamageStatusRep(DamageStatus damageStatus) {
		this.macroActionString = new ArrayList<>();
		this.macroActionNames = new ArrayList<>();
		for (MacroAction macroAction : damageStatus.availableActions) {
			macroActionString.add(macroAction.getMacroActionString());
			macroActionNames.add(macroAction.getName());
		}
		this.numberOfMacroActionsPerTurn = damageStatus.getNumberOfMacroActionsPerTurn();
		this.numberOfMacroActionsToPerform = damageStatus.getNumberOfMacroActionsToPerform();
	}


	public int getNumberOfMacroActionsPerTurn() {
		return numberOfMacroActionsPerTurn;
	}

	public int getNumberOfMacroActionsToPerform() {
		return numberOfMacroActionsToPerform;
	}

	public int numOfMacroActions() {
		return macroActionString.size();
	}

	public String getMacroActionString(int indexOfTheMacroAction) {
		return macroActionString.get(indexOfTheMacroAction);
	}

	public String getMacroActionName(int indexOfTheMacroAction) {
		return macroActionNames.get(indexOfTheMacroAction);
	}
}


