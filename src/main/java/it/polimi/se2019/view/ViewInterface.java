package it.polimi.se2019.view;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gameboard.GameBoardRep;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMapRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.PlayerRepPosition;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.List;

/**
 * Interface of the View class. It has the methods to ask the user a question and to record the answer.
 * It also contains the methods to update the Representations of the main elements of the game.
 * @author Marchingegno
 */
public interface ViewInterface {

	String getNickname();

	/**
	 * Ask the user an action to perform.
	 *
	 * @param activablePowerups must be true if the player has an activable powerup in his inventory.
	 * @param activableWeapons  must be true if the player has a loaded weapon in his inventory.
	 */
	void askAction(boolean activablePowerups, boolean activableWeapons);

	/**
	 * Asks the user which weapon to grab.
	 *
	 * @param indexesOfTheGrabbableWeapons the weapons that the player can grab.
	 */
	void askGrabWeapon(List<Integer> indexesOfTheGrabbableWeapons);

	/**
	 * If the player has already 3 weapons and tries to grab another one, this asks which weapon the user would like
	 * to leave behind.
	 *
	 * @param indexesOfTheGrabbableWeapons the weapons that the player can grab.
	 */
	void askSwapWeapon(List<Integer> indexesOfTheGrabbableWeapons);

	/**
	 * Asks the user where would he like to move.
	 *
	 * @param reachableCoordinates the list of coordinates available to choose from.
	 */
	void askMove(List<Coordinates> reachableCoordinates);

	/**
	 * Asks the user which weapon to shoot with.
	 *
	 * @param shootableWeapons the weapons the player can shoot with.
	 */
	void askShoot(List<Integer> shootableWeapons);

	/**
	 * Asks the user which weapon to reload.
	 *
	 * @param loadableWeapons the weapons to choose from.
	 */
	void askReload(List<Integer> loadableWeapons);

	/**
	 * Asks the user to discard a powerup card in order to spawn.
	 */
	void askSpawn();

	/**
	 * Asks the user a Question Container from the weapon he is shooting with, to advance the shooting process.
	 *
	 * @param questionContainer the Question Container from the weapon.
	 */
	void askWeaponChoice(QuestionContainer questionContainer);

	/**
	 * Asks the user which powerup to activate.
	 *
	 * @param activablePowerups the list of powerup to choose from.
	 */
	void askPowerupActivation(List<Integer> activablePowerups);

	/**
	 * Asks if the user would like to activate an on-damage powerup, such as Tagback Grenade.
	 *
	 * @param activablePowerups the list of powerup to choose from.
	 * @param shootingPlayer    the enemy that hit the player.
	 */
	void askOnDamagePowerupActivation(List<Integer> activablePowerups, String shootingPlayer);

	/**
	 * Asks the user a Question Container from the powerup he is activating.
	 *
	 * @param questionContainer the Question Container from the powerup.
	 */
	void askPowerupChoice(QuestionContainer questionContainer);

	/**
	 * Asks the user the end game question.
	 *
	 * @param activablePowerups the list of powerup he can use.
	 */
	void askEnd(boolean activablePowerups);

	/**
	 * Asks the user to pay for an action he is executing.
	 *
	 * @param priceToPay            the price that the user will pay.
	 * @param canAffordAlsoWithAmmo whether the user can use only ammos to pay the price.
	 */
	void askToPay(List<AmmoType> priceToPay, boolean canAffordAlsoWithAmmo);

	/**
	 * Displays the end of the game and the leaderboard.
	 *
	 * @param finalPlayersInfo the leaderboard.
	 */
	void endOfGame(List<PlayerRepPosition> finalPlayersInfo);

	/**
	 * Updates the representation of the GameBoard.
	 *
	 * @param gameBoardRepToUpdate the representation of the GameBoard.
	 */
	void updateGameBoardRep(GameBoardRep gameBoardRepToUpdate);

	/**
	 * Updates the representation of the GameMap.
	 *
	 * @param gameMapRepToUpdate the representation of the GameMap.
	 */
	void updateGameMapRep(GameMapRep gameMapRepToUpdate);

	/**
	 * Updates the representation of a player..
	 *
	 * @param playerRepToUpdate the representation of the player.
	 */
	void updatePlayerRep(PlayerRep playerRepToUpdate);
}