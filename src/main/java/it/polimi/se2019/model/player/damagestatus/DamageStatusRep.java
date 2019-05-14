package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.utils.MacroAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of DamageStatus class.
 * @author Marchingegno
 */
public class DamageStatusRep extends Representation {
	private int numberOfActionsPerTurn; //number of actions that a player with this status can perform in a turn.
	private int numberOfActionsPerformed; //actions that the player performed in this turn.
	private List<String> macroActionString;
	/*Note: availableActions contains the reference of the MacroActions. In the current state of the project, there is
	no need no clone them, since they are immutable.*/


	public DamageStatusRep(DamageStatus damageStatus) {
		this.macroActionString = new ArrayList<>();
		for (MacroAction macroAction : damageStatus.availableActions) {
			macroActionString.add(macroAction.getMacroActionString());
		}
		this.numberOfActionsPerTurn = damageStatus.getNumberOfActionsPerTurn();
		this.numberOfActionsPerformed = damageStatus.getNumberOfActionsPerformed();
	}


	public int getNumberOfActionsPerTurn() {
		return numberOfActionsPerTurn;
	}

	public int getNumberOfActionsPerformed() {
		return numberOfActionsPerformed;
	}

	public List<String> getMacroActionStrings() {
		return macroActionString;
	}
}


