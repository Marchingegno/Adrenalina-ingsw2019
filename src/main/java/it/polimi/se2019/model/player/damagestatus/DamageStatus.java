package it.polimi.se2019.model.player.damagestatus;

import it.polimi.se2019.utils.MacroAction;

import java.util.ArrayList;

/**
 * This class represents the status of the player that decides which actions it can take.
 * @author Marchingegno
 */
public interface DamageStatus {

	ArrayList<MacroAction> getAvailableActions();

	void doAction();

}