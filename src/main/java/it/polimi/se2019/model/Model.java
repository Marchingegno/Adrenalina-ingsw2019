package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerBoard;
import it.polimi.se2019.view.ViewInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Model extends Observable {

	private ViewInterface view;
	private GameBoard gameBoard;


	public void initialize(String mapPath, List<String> playerNames, int startingSkulls) {
		gameBoard = new GameBoard(mapPath, playerNames, startingSkulls);
	}

	public void movePlayerTo(Player playerToMove) {
	}

	public void doDamageAndAddMarks(Player shootingPlayer, Player damagedPlayer, int amountOfDamage, int amountOfMarks) {
		doDamage(shootingPlayer, damagedPlayer, amountOfDamage);
		addMarks(shootingPlayer, damagedPlayer, amountOfMarks);
	}

	public void addMarks(Player shootingPlayer, Player damagedPlayer, int amountOfMarks) {
		damagedPlayer.getPlayerBoard().addMarks(shootingPlayer, amountOfMarks);
	}

	public void doDamage(Player shootingPlayer, Player damagedPlayer, int amountOfDamage) {
		PlayerBoard damagedPlayerBoard = damagedPlayer.getPlayerBoard();
		damagedPlayerBoard.addDamage(shootingPlayer, amountOfDamage);
		if(damagedPlayerBoard.isDead()) {
			gameBoard.addKillShot(shootingPlayer, damagedPlayerBoard.isOverkilled());
		}
	}

	public boolean areSkullsFinished() {
		return gameBoard.areSkullsFinished();
	}

	public void scoreDeadPlayers() {
		for (Player player : gameBoard.getPlayers()) {
			PlayerBoard playerBoard = player.getPlayerBoard();
			if (playerBoard.isDead())
			{

				playerBoard.resetBoardAfterDeath();
			}
		}
	}


	public void drawPowerupCard(Player player) {
	}

	public void drawWeaponCard(Player player, WeaponCard weapon) {
	}

	public ArrayList<Coordinates> getReachableCoordinates(Player player) {
		return null;
	}

	public void getPlayersCoordinates() {
	}

	public GameBoardRep getGameboardRep() {
		return null;
	}

	public void notifyMapChange() {
	}

	public void notifyPlayerChange() {
	}

	public void notifyGameboardChange() {
	}

}