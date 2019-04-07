package it.polimi.se2019.view;

import it.polimi.se2019.model.ModelView;

public class RemoteView implements ViewInterface {

	private ModelView modelView;


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

}