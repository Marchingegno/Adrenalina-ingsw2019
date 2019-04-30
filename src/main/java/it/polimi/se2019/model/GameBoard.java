package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.ammo.AmmoDeck;
import it.polimi.se2019.model.cards.powerups.PowerupDeck;
import it.polimi.se2019.model.cards.weapons.WeaponDeck;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerQueue;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends Representable {

	private ArrayList<Player> players;
	private int remainingSkulls;
	private ArrayList<KillShot> killShots;
	private ArrayList<Player> doubleKills;
	private WeaponDeck weaponDeck;
	private AmmoDeck ammoDeck;
	private PowerupDeck powerupDeck;
	private GameMap gameMap;
	private boolean killShotInThisTurn;
	private PlayerQueue playerQueue;
	private GameBoardRep gameBoardRep;


	public GameBoard(List<String> playerNames, int startingSkulls) {
		// initialize players
		players = new ArrayList<>(playerNames.size());
		int id = 0;

		for (String name : playerNames) {
			players.add(new Player(name, id, Color.CharacterColorType.values()[id + 1]));
			id++;
		}

		playerQueue = new PlayerQueue(players);

		// initialize skulls
		remainingSkulls = startingSkulls;

		// initialize GameBoard attributes
		killShots = new ArrayList<>();
		doubleKills = new ArrayList<>();
		weaponDeck = new WeaponDeck();
		ammoDeck = new AmmoDeck();
		powerupDeck = new PowerupDeck();
		killShotInThisTurn = false;
		setChanged();
	}

	PlayerQueue getPlayerQueue() {
		return playerQueue;
	}

	public void nextPlayerTurn() {
		playerQueue.moveFirstToLast();
		setChanged();
	}

	public Player getCurrentPlayer() {
		return playerQueue.peekFirst();
	}

	public ArrayList<Player> getPlayers() {
		return new ArrayList<>(players);
	}

	public boolean areSkullsFinished() {
		return remainingSkulls <= 0;
	}

	public int getRemainingSkulls() {
		return remainingSkulls;
	}

	private void addDoubleKill(Player shootingPlayer) {
		doubleKills.add(shootingPlayer);
		setChanged();
	}

	/**
	 * This method award points in the killshot track. It checks whether or not the killshot is an overkill, and
	 * decreases the number of skulls in the gameBoard.
	 *
	 * @param shootingPlayer
	 * @param overkill
	 */
	public void addKillShot(Player shootingPlayer, boolean overkill) {
		killShots.add(new KillShot(shootingPlayer, overkill));
		if (shootingPlayer == getCurrentPlayer()) {
			if (killShotInThisTurn)
				addDoubleKill(shootingPlayer);
			else if (!areSkullsFinished()) {
				remainingSkulls--;
			}
			killShotInThisTurn = true;
		}
		setChanged();
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

	//I don't like it
	public void setGameMap(GameMap gameMap) {
		this.gameMap = gameMap;
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

	public void currentPlayerGrabs(int index) {
		Coordinates playerCoordinates = gameMap.getPlayersCoordinates().get(getCurrentPlayer());
		//gameMap.getSquare(playerCoordinates).grabCard(getCurrentPlayer(), index);
	}

	public void updateRep() {
		if (gameBoardRep == null || hasChanged()) {
			gameBoardRep = new GameBoardRep(this);
			Utils.logInfo("Game board rep updated");
		}

	}

	public Representation getRep() {
		return gameBoardRep;
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