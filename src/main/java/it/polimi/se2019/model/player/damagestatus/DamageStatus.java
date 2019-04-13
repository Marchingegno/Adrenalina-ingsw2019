package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.utils.MacroAction;

import java.util.List;

/**
 * This class represents the status of the player that decides which actions it can take.
 * @author Marchingegno
 */
public interface DamageStatus {

	List<MacroAction> getAvailableActions();

	void doAction();

}