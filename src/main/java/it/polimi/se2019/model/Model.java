package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.view.ViewInterface;

import java.util.ArrayList;
import java.util.Observable;

public class Model extends Observable {

	private ViewInterface view;
	private GameBoard gameBoard;


	public void initialize(String mapPath, ArrayList<String> playerNames, int skulls) {
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public void movePlayerTo(Player palyerToMove) {
	}

	public void doDamageAndAddMarksTo(Player playerShooting, Player playerDamages, int amountOfDamage, int numOfMarks) {
	}

	public void addMarks(Player playerShooting, Player playerDamages, int numOfMarks) {
	}

	public void doDamageTo(Player playerShooting, Player playerDamages, int amountOfDamage) {
	}

	public boolean isFrenzy() {
		return false;
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