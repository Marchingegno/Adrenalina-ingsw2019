package it.polimi.se2019.view;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.ModelRep;
import it.polimi.se2019.utils.MacroAction;
import it.polimi.se2019.utils.Utils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class VirtualView implements ViewInterface, Observer {

	private ModelRep modelRep;

	public VirtualView(Model model){
		model.addObserver(this);
	}

	public void displayPossibleActions(List<MacroAction> possibleActions){
		for (MacroAction macroAction: possibleActions) {
			Utils.logInfo(macroAction.toString());
		}
	}

	@Override
	public void handleMove(int row, int column) {
	}

	@Override
	public void handleReload(int indexOfweaponToReload) {
	}

	@Override
	public void handleShoot(int indexOfWeapon, String playersToShoot) {
	}

	@Override
	public void handleSpawn(int indexOfPowerup) {
	}

	@Override
	public void showMessage(String message) {
	}

	@Override
	public void showGameBoard() {
	}

	@Override
	public int chooseWeapon() {
		return 0;
	}

	@Override
	public int chooseAction() {
		return 0;
	}

	@Override
	public int choosePowerup() {
		return 0;
	}

	@Override
	public void update(Observable observable, Object arg) {
		((Model) observable).getModelRep();
	}
}