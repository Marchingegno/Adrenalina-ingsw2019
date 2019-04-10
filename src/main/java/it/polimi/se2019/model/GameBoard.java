package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.ammo.AmmoDeck;
import it.polimi.se2019.model.cards.powerups.PowerupDeck;
import it.polimi.se2019.model.cards.weapons.WeaponDeck;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoard {

	private ArrayList<Player> players;
	private int remainingSkulls;
	private ArrayList<KillShot> killShots;
	private ArrayList<Player> doubleKills;
	private WeaponDeck weaponDeck;
	private AmmoDeck ammoDeck;
	private PowerupDeck powerupDeck;
	private int currentPlayerIndex;
	private GameMap gameMap;
	private boolean killShotInThisTurn;


	public GameBoard(String mapPath, List<String> playerNames, int startingSkulls) {
		// initialize players
		players = new ArrayList<>(playerNames.size());
		int id = 0;
		for (String name : playerNames) {
			players.add(new Player(name, id, Color.BLACK)); //  TODO Color?
			id++;
		}
		currentPlayerIndex = 0;

		// initialize skulls
		remainingSkulls = startingSkulls;

		// initialize map
		gameMap = new GameMap(mapPath);

		// initialize GameBoard attributes
		killShots = new ArrayList<>();
		doubleKills = new ArrayList<>();
		weaponDeck = new WeaponDeck();
		ammoDeck = new AmmoDeck();
		powerupDeck = new PowerupDeck();
		killShotInThisTurn = false;
	}


	public List<Player> getPlayers() {
		return new ArrayList<>(players);
	}

	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	public boolean areSkullsFinished() {
		return remainingSkulls <= 0;
	}

	public int getRemainingSkulls() {
		return remainingSkulls;
	}

	private void addDoubleKill(Player shootingPlayer) {
		doubleKills.add(shootingPlayer);
	}

	public void addKillShot(Player shootingPlayer, boolean overkill) {
		killShots.add(new KillShot(shootingPlayer, overkill));
		if(shootingPlayer == getCurrentPlayer()) {
			if(killShotInThisTurn)
				addDoubleKill(shootingPlayer);
			else
				killShotInThisTurn = true;
		}
	}

	public List<KillShot> getKillShots() {
		return new ArrayList<>(killShots);
	}

	public List<Player> getDoubleKills() {
		return new ArrayList<>(doubleKills);
	}

	public GameMap getGameMap() {
		return gameMap;
	}

	public void nextPlayerTurn() {
		killShotInThisTurn = false;
		currentPlayerIndex++;
		if(currentPlayerIndex >= players.size())
			currentPlayerIndex = 0;
	}

	public WeaponDeck getWeaponDeck() {
		return weaponDeck;
	}

	public PowerupDeck getPowerupDeck() {
		return powerupDeck;
	}

	public AmmoDeck getAmmoDeck() {
		return ammoDeck;
	}

}

class KillShot {

	private Player player;
	private boolean overkill;


	public KillShot(Player shootingPlayer, boolean overkill) {
		player = shootingPlayer;
		this.overkill = overkill;
	}


	public Player getPlayer() {
		return player;
	}

	public boolean isOverkill() {
		return overkill;
	}
}