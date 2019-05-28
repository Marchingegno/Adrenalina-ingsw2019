package it.polimi.se2019.view;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.List;

public interface ViewInterface {

	String getNickname();

	/**
	 * Ask the user an action to perform.
	 * @param activablePowerups must be true if the player has an activable powerup in his inventory.
	 * @param activableWeapons must be true if the player has a loaded weapon in his inventory.
	 */
	void askAction(boolean activablePowerups, boolean activableWeapons);

	void askGrabWeapon(List<Integer> indexesOfTheGrabbableWeapons);

	void askSwapWeapon(List<Integer> indexesOfTheGrabbableWeapons);

	void askMove(List<Coordinates> reachableCoordinates);

	void askShoot(List<Integer> shootableWeapons);

	void askReload(List<Integer> loadableWeapons);

	void askSpawn();

	void askWeaponChoice(QuestionContainer questionContainer);

	void askPowerupActivation(List<Integer> activablePowerups);

	void askPowerupChoice(QuestionContainer questionContainer);

	void askEnd(boolean activablePowerups);

	void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate);

	void updateGameMapRep(GameMapRep gameMapRepToUpdate);

	void updatePlayerRep(PlayerRep playerRepToUpdate);

	void askToPay(List<AmmoType> priceToPay);
}