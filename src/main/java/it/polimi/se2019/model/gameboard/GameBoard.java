package it.polimi.se2019.model.gameboard;

import it.polimi.se2019.model.Representable;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.ammo.AmmoDeck;
import it.polimi.se2019.model.cards.powerups.PowerupDeck;
import it.polimi.se2019.model.cards.weapons.WeaponDeck;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerQueue;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.model.player.damagestatus.HighDamage;
import it.polimi.se2019.model.player.damagestatus.LowDamage;
import it.polimi.se2019.model.player.damagestatus.MediumDamage;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Class that implements the game board.
 * @author Marchingegno
 * @author MarcerAndrea
 * @author Desno365
 */
public class GameBoard extends Observable implements Representable {

	private boolean frenzyStarted;
	private List<Player> players;
	private int remainingSkulls;
	private List<KillShot> killShots;
	private List<Player> doubleKills;
	private WeaponDeck weaponDeck;
	private AmmoDeck ammoDeck;
	private PowerupDeck powerupDeck;
	private GameMap gameMap;
	private boolean killShotInThisTurn;
	private PlayerQueue playerQueue;
	private GameBoardRep gameBoardRep;


	public GameBoard(String mapName, List<String> playerNames, int startingSkulls) {

		// initialize GameBoard attributes
		remainingSkulls = startingSkulls;
		killShots = new ArrayList<>();
		doubleKills = new ArrayList<>();
		weaponDeck = new WeaponDeck();
		ammoDeck = new AmmoDeck();
		powerupDeck = new PowerupDeck();
		killShotInThisTurn = false;
		frenzyStarted = false;

		players = new ArrayList<>(playerNames.size());
		int id = 0;
		for (String name : playerNames) {
			players.add(new Player(name, id));
			id++;
		}
		players.forEach(player -> player.getPlayerBoard().addPowerup(powerupDeck.drawCard()));
		playerQueue = new PlayerQueue(players);
		gameMap = new GameMap(mapName, players, this);

		setChanged();
	}

	/**
	 * Returns the player queue.
	 * @return the player queue.
	 */
	public PlayerQueue getPlayerQueue() {
		return playerQueue;
	}

	/**
	 * The current player is set as last of the turn queue.
	 */
	public void nextPlayerTurn() {
		playerQueue.peekFirst().setTurnStatus(TurnStatus.IDLE);
		playerQueue.moveFirstToLast();
		//playerQueue.peekFirst().setTurnStatus(TurnStatus.YOUR_TURN);
		setChanged();
	}

	/**
	 * Returns true if and only if the frenzy is started.
	 * @return true if and only if the frenzy is started.
	 */
	public boolean isFrenzyStarted() {
		return frenzyStarted;
	}

	/**
	 * Starts Frenzy.
	 */
	public void startFrenzy(){
		this.frenzyStarted = true;
		setChanged();
		Utils.logInfo("GameBoard -> startFrenzy(): Starting frenzy");
	}

	/**
	 * Returns the reference of the current player.
	 * @return the reference of the current player.
	 */
	public Player getCurrentPlayer() {
		return playerQueue.peekFirst();
	}

	/**
	 * Returns a copy of the list of all the players.
	 * @return a copy of the list of all the players.
	 */
	public List<Player> getPlayers() {
		//return new ArrayList<>(players);
		return players;
	}

	/**
	 * Returns true if and only if there are no skulls on the game board.
	 * @return true if and only if there are no skulls on the game board.
	 */
	public boolean areSkullsFinished() {
		return remainingSkulls <= 0;
	}

	/**
	 * Returns the number of skulls remaining on the game board.
	 * @return the number of skulls remaining on the game board.
	 */
	public int getRemainingSkulls() {
		return remainingSkulls;
	}

	/**
	 * Adds a double kill.
	 * @param shootingPlayer player that has done the double kill.
	 */
	private void addDoubleKill(Player shootingPlayer) {
		doubleKills.add(shootingPlayer);
		setChanged();
		Utils.logInfo("GameBoard -> addDoubleKill(): adding to the double kill track " + shootingPlayer.getPlayerName());
	}

	/**
	 * This method award points in the killshot track. It checks whether or not the killshot is an overkill, and
	 * decreases the number of skulls in the gameBoard.
	 *
	 * @param shootingPlayer player how has shot.
	 * @param overkill true if the kill is also an overkill.
	 */
	public void addKillShot(Player shootingPlayer, boolean overkill) {
		killShots.add(new KillShot(shootingPlayer, overkill));
		Utils.logInfo("GameBoard -> addKillShot(): adding to the killshot track " + shootingPlayer.getPlayerName() + (overkill ? " with overkill" : ""));
		if (shootingPlayer == getCurrentPlayer()) {
			if (killShotInThisTurn) {
				addDoubleKill(shootingPlayer);
			}

			if (!areSkullsFinished()) {
				remainingSkulls--;
				Utils.logInfo("GameBoard -> addKillShot(): decreasing num of skulls to " + remainingSkulls);
			}

			killShotInThisTurn = true;
		}

		setChanged();
	}

	/**
	 * Returns a copy of the kill shot track.
	 *
	 * @return a copy of the kill shot track.
	 */
	public List<KillShot> getKillShots() {
		return new ArrayList<>(killShots);
	}

	/**
	 * Returns a copy of the double kills.
	 * @return a copy of the double kills.
	 */
	public List<Player> getDoubleKills() {
		return new ArrayList<>(doubleKills);
	}

	/**
	 * Returns the game map.
	 * @return the game map.
	 */
	public GameMap getGameMap() {
		return gameMap;
	}

	/**
	 * Returns the weapon deck.
	 * @return the weapon deck.
	 */
	public WeaponDeck getWeaponDeck() {
		return weaponDeck;
	}

	/**
	 * Returns the powerup deck.
	 * @return the powerup deck.
	 */
	public PowerupDeck getPowerupDeck() {
		return powerupDeck;
	}

	/**
	 * Returns the ammo deck.
	 * @return the ammo deck.
	 */
	public AmmoDeck getAmmoDeck() {
		return ammoDeck;
	}

	/**
	 * Updates the representation of the game board.
	 */
	public void updateRep() {
		if (gameBoardRep == null || hasChanged()) {
			gameBoardRep = new GameBoardRep(this);
			Utils.logInfo("GameBoard -> updateRep(): The game board representation has been updated");
		} else {
			Utils.logInfo("GameBoard -> updateRep(): The game board representation is already up to date");
		}
	}

	/**
	 * Returns the representation of the game board.
	 * @return the representation of the game board.
	 */
	public Representation getRep() {
		return gameBoardRep;
	}

	public void setCorrectDamageStatus(Player player) {
		if(player.getTurnStatus().equals(TurnStatus.PRE_SPAWN)){
			player.setDamageStatus(new LowDamage());
			return;
		}

		List<Player> damageBoard = player.getPlayerBoard().getDamageBoard();

		//If the game is in frenzy mode, then the player already has the right damageStatus.
		if (isFrenzyStarted())
			player.getDamageStatus().refillMacroActions();

		else if(damageBoard.size() < GameConstants.MEDIUM_DAMAGE_THRESHOLD)
			player.setDamageStatus(new LowDamage());

		else if(damageBoard.size() < GameConstants.HIGH_DAMAGE_THRESHOLD)
			player.setDamageStatus(new MediumDamage());

		else
			player.setDamageStatus(new HighDamage());
	}

	public void setTurnStatus(Player player, TurnStatus turnStatus) {
		player.setTurnStatus(turnStatus);
	}

	public void spawnPlayer(Player player, int indexOfCard) {
		//Move player to spawn square
		Coordinates spawningCoordinates = gameMap.getSpawnSquare(player.getPlayerBoard().getPowerupCards().get(indexOfCard).getAssociatedAmmo());
		gameMap.movePlayerTo(player, spawningCoordinates);

		//Remove card from player
		player.getPlayerBoard().removePowerup(indexOfCard);
		//Set its turn status
		if (player.getTurnStatus() == TurnStatus.PRE_SPAWN) {
			setTurnStatus(player, TurnStatus.YOUR_TURN);
		} else {
			setTurnStatus(player, TurnStatus.IDLE);
		}
	}

	public void addPowerupCardTo(Player player) {
		player.getPlayerBoard().addPowerup(powerupDeck.drawCard());
	}

	public Pair playerWeaponHandleFire(Player player, int choice) {
		return player.getFiringWeapon().handleFire(choice);
	}

	public boolean isThePlayerDoneFiring(Player player) {
		return player.getFiringWeapon().doneFiring();
	}

	public void resetPlayerCurrentWeapon(Player player) {
		player.getFiringWeapon().reset();
	}
}

