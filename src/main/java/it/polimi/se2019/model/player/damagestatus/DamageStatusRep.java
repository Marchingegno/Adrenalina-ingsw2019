package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.MacroAction;

import java.util.List;

/**
 * Representation of DamageStatus class.
 * @author Marchingegno
 */
public class DamageStatusRep extends Message {
	int numberOfActionsPerTurn; //number of actions that a player with this status can perform in a turn.
	int numberOfActionsPerformed; //actions that the player performed in this turn.
	List<MacroAction> availableActions;
	/*Note: availableActions contains the reference of the MacroActions. In the current state of the project, there is
	no need no clone them, since they are immutable.*/


	public DamageStatusRep(DamageStatus damageStatus) {
		super(MessageType.DAMAGE_STATUS_REP, MessageSubtype.INFO);
		this.availableActions = damageStatus.getAvailableActions(); //getAvailableActions already returns a copy of the array.
		this.numberOfActionsPerTurn = damageStatus.getNumberOfActionsPerTurn();
		this.numberOfActionsPerformed = damageStatus.getNumberOfActionsPerformed();
	}


	public int getNumberOfActionsPerTurn() {
		return numberOfActionsPerTurn;
	}

	public int getNumberOfActionsPerformed() {
		return numberOfActionsPerformed;
	}

	public List<MacroAction> getAvailableActions() {
		return availableActions;
	}

	public String printMacroActions(){
		StringBuilder myBuilder = new StringBuilder();

		for (MacroAction macroAction:availableActions) {
			myBuilder.append(macroAction.printAction());
			myBuilder.append("\n");
		}

		return myBuilder.toString();
	}
}


