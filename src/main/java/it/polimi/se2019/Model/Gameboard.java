package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.ammo.AmmoDeck;
import it.polimi.se2019.model.cards.powerups.PowerupDeck;
import it.polimi.se2019.model.cards.weapons.WeaponDeck;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;

public class GameBoard {

	private int skulls;
	private ArrayList<Player> killShots;
	private ArrayList<Player> doubleKill;
	private WeaponDeck weaponDeck;
	private AmmoDeck ammoDeck;
	private PowerupDeck powerupDeck;
	private Player currentPlayer;
	private Player players;
	private GameMap gameMap;


	public void Gameboard(String mapPath, ArrayList<String> playerNames, int skulls) {
	}


	public ArrayList<Player> getPlayers() {
		return null;
	}

	public void isFrenzyTriggered() {
	}

	public void addDoubleKill(Player player) {
	}

	public void addKill(Player player, boolean overkill) {
	}

	public void nextPlayerTurn() {
	}

	public WeaponDeck getWeaponDeck() {
		return null;
	}

	public PowerupDeck getPowerupDeck() {
		return null;
	}

	public AmmoDeck getAmmoDeck() {
		return null;
	}

}