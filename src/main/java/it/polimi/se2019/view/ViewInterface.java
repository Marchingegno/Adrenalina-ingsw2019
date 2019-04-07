package it.polimi.se2019.view;

public interface ViewInterface {

	void handleMove(int row, int column);

	void handleReload(int indexOfweaponToReload);

	void handleShoot(int indexOfWeapon, String playersToShoot);

	void handleSpawn(int indexOfPowerup);

	void showMessage(String message);

	void showGameBoard();

	int chooseWeapon();

	int chooseAction();

	int choosePowerup();

}