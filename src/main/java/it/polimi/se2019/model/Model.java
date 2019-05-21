package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.ammo.AmmoCard;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.powerups.QuestionContainer;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerBoard;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.model.player.damagestatus.FrenzyAfter;
import it.polimi.se2019.model.player.damagestatus.FrenzyBefore;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.ActionType;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Facade of the game board.
 *
 * @author Marchingegno
 * @author Desno365
 * @author MarcerAndrea
 */
public class Model {

	private GameBoard gameBoard;
	private GameMap gameMap;

	public Model(String mapName, List<String> playerNames, int startingSkulls) {
		if(startingSkulls < GameConstants.MIN_SKULLS || startingSkulls > GameConstants.MAX_SKULLS)
			throw new IllegalArgumentException("Invalid number of skulls!");
		if(playerNames.size() > GameConstants.MAX_PLAYERS || playerNames.size() < GameConstants.MIN_PLAYERS)
			throw new IllegalArgumentException("Invalid number of players!");
		gameBoard = new GameBoard(mapName, playerNames, startingSkulls);
		gameMap = gameBoard.getGameMap();
	}


	public void movePlayerTo(String playerName, Coordinates coordinates) {
		Player player = getPlayerFromName(playerName);
		gameMap.movePlayerTo(player, coordinates);
		updateReps();
	}

	public boolean isFrenzyStarted() {
		return gameBoard.isFrenzyStarted();
	}

	//TODO: Revisit the location of the first player. I don't know if he is the player that just ended the turn, or the following.
	public void startFrenzy() {
		gameBoard.startFrenzy();

		Player firstPlayer = gameBoard.getPlayers().get(0);

		boolean isAfterFirstPlayer = false;
		for (Player player : gameBoard.getPlayerQueue()) {
			//The first must also receive the Frenzy After damage status
			if (player.getPlayerName().equals(firstPlayer.getPlayerName()))
				isAfterFirstPlayer = true;

			player.setDamageStatus(isAfterFirstPlayer ? new FrenzyAfter() : new FrenzyBefore());
		}
		flipPlayers();

		updateReps();
	}

	public void resetPlayerCurrentWeapon(String playerName){
		Player player = getPlayerFromName(playerName);
		gameBoard.resetPlayerCurrentWeapon(player);
	}
	public boolean isTheplayerDoneFiring(String playerName){
		Player player = getPlayerFromName(playerName);
		return gameBoard.isThePlayerDoneFiring(player);
	}

	public Pair playerWeaponHandleFire(String playerName, int choice){
		Player player = getPlayerFromName(playerName);
		return gameBoard.playerWeaponHandleFire(player, choice);
	}

	public void spawnPlayer(String playerName, int indexOfCard) {
		Player player = getPlayerFromName(playerName);
		gameBoard.spawnPlayer(player, indexOfCard);
		updateReps();
	}

	public void addPowerupCardTo(String playerName) {
		Player player = getPlayerFromName(playerName);
		gameBoard.addPowerupCardTo(player);
		updateReps();
	}


	public String getCurrentPlayerName() {
		return gameBoard.getCurrentPlayer().getPlayerName();
	}

	public void addGameBoardObserver(Observer observer) {
		gameBoard.addObserver(observer);
	}

	public void addGameMapObserver(Observer observer) {
		gameBoard.getGameMap().addObserver(observer);
	}

	public void addPlayersObserver(Observer observer) {
		for (Player player : gameBoard.getPlayers()) {
			player.addObserver(observer);
		}
	}

	public Player getCurrentPlayer() {
		return gameBoard.getCurrentPlayer();
	}

	public void nextPlayerTurn() {
		gameBoard.nextPlayerTurn();
		updateReps();
	}

	public void setCorrectDamageStatus(String playerName){
		Player player = getPlayerFromName(playerName);
		gameBoard.setCorrectDamageStatus(player);
		updateReps();
	}

	public TurnStatus getTurnStatus(String playerName) {
		return getPlayerFromName(playerName).getTurnStatus();
	}

	public void setTurnStatusOfCurrentPlayer(TurnStatus turnStatus){
		setTurnStatus(getCurrentPlayerName(), turnStatus);
	}

	public void doDamageAndAddMarks(String shootingPlayerName, String damagedPlayerName, int amountOfDamage, int amountOfMarks) {
		Player shootingPlayer = getPlayerFromName(shootingPlayerName);
		Player damagedPlayer = getPlayerFromName(damagedPlayerName);

		doDamage(shootingPlayer, damagedPlayer, amountOfDamage);

		addMarks(shootingPlayer, damagedPlayer, amountOfMarks);

		updateReps();

	}

	public void addMarks(String shootingPlayerName, String damagedPlayerName, int amountOfMarks) {
		Player shootingPlayer = getPlayerFromName(shootingPlayerName);
		Player damagedPlayer = getPlayerFromName(damagedPlayerName);

		addMarks(shootingPlayer, damagedPlayer, amountOfMarks);

		updateReps();
	}

	public void doDamage(String shootingPlayerName, String damagedPlayerName, int amountOfDamage) {
		Player shootingPlayer = getPlayerFromName(shootingPlayerName);
		Player damagedPlayer = getPlayerFromName(damagedPlayerName);

		doDamage(shootingPlayer, damagedPlayer, amountOfDamage);

		updateReps();
	}

	public boolean areSkullsFinished() {
		return gameBoard.areSkullsFinished();
	}

	public void scoreDeadPlayers() {
		gameBoard.getPlayers().stream()
				.filter(item -> item.getPlayerBoard().isDead())
				.forEach(this::scoreDeadPlayer);
		updateReps();
	}

	public List<Coordinates> getReachableCoordinatesOfTheCurrentPlayer() {
		return getReachableCoordinates(getCurrentPlayerName(), gameBoard.getCurrentPlayer().getDamageStatus().getCurrentMacroAction().getNumOfMovements());
	}

	public List<Coordinates> getEmptyReachableCoordinatesOfTheCurrentPlayer() {
		return gameMap.getNotEmptyReachableCoordinates(gameMap.getPlayerCoordinates(getCurrentPlayer()), gameBoard.getCurrentPlayer().getDamageStatus().getCurrentMacroAction().getNumOfMovements());
	}

	public void fillGameMap() {
		gameMap.fillMap();
		updateReps();
	}

	public MessageType getGrabMessageType() {
		return gameMap.getPlayerSquare(getCurrentPlayer()).getGrabMessageType();
	}

	public void grabWeaponCard(String playerName, int index) {
		Player player = getPlayerFromName(playerName);
		WeaponCard cardToGrab = (WeaponCard) gameMap.grabCard(gameMap.getPlayerCoordinates(player), index);
		addWeaponCardToPlayer(player, cardToGrab);
		updateReps();
	}

	public void grabAmmoCard(String playerName, int index) {
		Player player = getPlayerFromName(playerName);
		AmmoCard cardToGrab = (AmmoCard) gameMap.grabCard(gameMap.getPlayerCoordinates(player), index);
		addAmmoCardToPlayer(player, cardToGrab);
		updateReps();
	}

	public void discardPowerupCard(String playerName, int indexOfThePowerup) {
		Player player = getPlayerFromName(playerName);
		player.getPlayerBoard().removePowerup(indexOfThePowerup);
		updateReps();
	}

	public void swapWeapons(String playerName, int indexOfThePlayerWeapon, int indexOfTheSpawnWeapon) {
		Player player = getPlayerFromName(playerName);
		Coordinates playerCoordinates = gameMap.getPlayerCoordinates(player);
		WeaponCard squareWeapon = (WeaponCard) (gameMap.grabCard(playerCoordinates, indexOfTheSpawnWeapon));

		gameMap.addCard(playerCoordinates, player.getPlayerBoard().swapWeapon(squareWeapon, indexOfThePlayerWeapon));
		updateReps();
	}

	public void reloadWeapon(String playerName, int indexOfTheWeapon) {
		Player player = getPlayerFromName(playerName);
		player.reload(indexOfTheWeapon);
		updateReps();
	}

	public List<Coordinates> getReachableCoordinates(String playerName, int distance) {
		Player player = getPlayerFromName(playerName);
		return gameMap.reachableCoordinates(player, distance);
	}

	public void flipPlayers() {
		gameBoard.getPlayers().forEach(Player::flipIfNoDamage);
		updateReps();
	}

	public boolean doesThePlayerHaveActionsLeft(String playerName) {
		return getPlayerFromName(playerName).getDamageStatus().hasMacroActionLeft();
	}

	public ActionType getNextActionToExecute(String playerName) {
		Player player = getPlayerFromName(playerName);
		return player.getDamageStatus().getNextActionToExecute();
	}

	public void setNextMacroAction(String playerName, int indexOfMacroAction) {
		Player player = getPlayerFromName(playerName);
		player.getDamageStatus().decreaseMacroActionsToPerform();
		player.getDamageStatus().setCurrentMacroActionIndex(indexOfMacroAction);
		updateReps();
	}

	public boolean canOnTurnPowerupBeActivated(String playerName, int indexOfPowerup) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();

		if(indexOfPowerup >= powerupCards.size() || indexOfPowerup < 0)
			return false;

		PowerupCard powerupCard = powerupCards.get(indexOfPowerup);
		if(powerupCard.getUseCase() != PowerupCard.PowerupUseCaseType.ON_TURN)
			return false;
		return powerupCard.canBeActivated();
	}

	public List<Integer> getActivableOnTurnPowerups(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();
		List<Integer> activablePowerups = new ArrayList<>();

		for (int i = 0; i < powerupCards.size(); i++) {
			if(powerupCards.get(i).getUseCase() == PowerupCard.PowerupUseCaseType.ON_TURN && powerupCards.get(i).canBeActivated())
				activablePowerups.add(i);
		}

		return activablePowerups;
	}

	public QuestionContainer activateOnTurnPowerup(String playerName, int indexOfPowerup, Message answer) {
		Player player = getPlayerFromName(playerName);
		PowerupCard powerupCard = player.getPlayerBoard().getPowerupCards().get(indexOfPowerup);
		return powerupCard.doPowerupStep(answer);
	}


	// ####################################
	// PRIVATE METHODS
	// ####################################

	private void addAmmoCardToPlayer(Player player, AmmoCard ammoCard){
		for (AmmoType ammo : ammoCard.getAmmo() ) {
			player.getPlayerBoard().getAmmoContainer().addAmmo(ammo);
		}

		if (ammoCard.hasPowerup() && player.getPlayerBoard().getPowerupCards().size() < GameConstants.MAX_POWERUP_CARDS_PER_PLAYER)
			gameBoard.addPowerupCardTo(player);

		gameBoard.getAmmoDeck().discardCard(ammoCard);

		updateReps();
	}

	private void addWeaponCardToPlayer(Player player, WeaponCard weaponCard){
		player.getPlayerBoard().addWeapon(weaponCard);
		updateReps();
	}

	private void addMarks(Player shootingPlayer, Player damagedPlayer, int amountOfMarks) {
		damagedPlayer.getPlayerBoard().addMarks(shootingPlayer, amountOfMarks);
		updateReps();
	}

	private void doDamage(Player shootingPlayer, Player damagedPlayer, int amountOfDamage) {
		PlayerBoard damagedPlayerBoard = damagedPlayer.getPlayerBoard();
		damagedPlayerBoard.addDamage(shootingPlayer, amountOfDamage);
		if (damagedPlayerBoard.isDead()) {
			gameBoard.addKillShot(shootingPlayer, damagedPlayerBoard.isOverkilled());
		}
		updateReps();
	}

	private void scoreDeadPlayer(Player player) {
		PlayerBoard playerBoard = player.getPlayerBoard();
		DamageDone damageDone = new DamageDone();
		ArrayList<Player> sortedPlayers;
		Player killingPlayer;
		boolean overkill = false;

		//This will check the damageBoard of the player and award killShot points.
		killingPlayer = playerBoard.getDamageBoard().get(GameConstants.DEATH_DAMAGE - 1);
		if (playerBoard.getDamageBoard().lastIndexOf(killingPlayer) == GameConstants.OVERKILL_DAMAGE - 1) {
			overkill = true;
		}
		gameBoard.addKillShot(killingPlayer, overkill);

		playerBoard.getDamageBoard().forEach(damageDone::damageUp);

		sortedPlayers = damageDone.getSortedPlayers();
		awardPoints(playerBoard, sortedPlayers);

		player.resetAfterDeath(); //This automatically increases its number of deaths.
		updateReps();
	}

	private void awardPoints(PlayerBoard deadPlayerBoard, ArrayList<Player> sortedPlayers) {
		int offset = 0;

		//TODO: The implementation is WRONG. A player should give FRENZY_SCORES only if the playerBoard is flipped. @Marchingegno

		//This method relies on the "SCORES" array defined in GameConstants.
		if (deadPlayerBoard.isFlipped()) {
			for (Player p : sortedPlayers) {
				p.getPlayerBoard().addPoints(GameConstants.FRENZY_SCORES.get(offset));
				offset++;
			}
		} else {
			//AWARD FIRST BLOOD POINT
			sortedPlayers.get(0).getPlayerBoard().addPoints(1);

			for (Player p : sortedPlayers) {
				p.getPlayerBoard().addPoints(GameConstants.SCORES.get(deadPlayerBoard.getNumberOfDeaths() + offset));
				offset++;
				/*Se con questa morte si attiva la frenzy, o la frenzy è già attivata,
				 *si deve swappare la damageBoard e il damageStatus del player.
				 *
				 * Fatto dal gameController!
				 */
			}

		}
		updateReps();
	}

	private void setTurnStatus(String playerName, TurnStatus turnStatus){
		Player player = getPlayerFromName(playerName);
		gameBoard.setTurnStatus(player, turnStatus);
		updateReps();
	}

	private Player getPlayerFromName(String playerName){
		for (Player player : gameBoard.getPlayers()) {
			if (playerName.equals(player.getPlayerName()))
				return player;
		}
		throw new IllegalArgumentException("No player with name: " + playerName);
	}

	private void updateReps() {
		gameBoard.updateRep();
		gameBoard.notifyObservers();
		gameMap.updateRep();
		gameMap.notifyObservers();
		for (Player player : gameBoard.getPlayers()) {
			player.updateRep();
			player.notifyObservers();
		}
	}

	// ####################################
	// METHODS ONLY FOR TESTS
 	// ####################################

	public GameBoard getGameBoard() {
		return gameBoard;
	}
}

/**
 * Data structure that helps with counting players and damage done on damage boards.
 *
 * @author Marchingegno
 */
class DamageDone {
	private ArrayList<Player> players;
	private ArrayList<Integer> damages;


	DamageDone() {
		this.players = new ArrayList<>();
		this.damages = new ArrayList<>();
	}

	/**
	 * Only for testing purposes.
	 */
	List<Integer> getDamages() {
		return new ArrayList<>(damages);
	}


	/**
	 * Only for testing purposes.
	 */
	List<Player> getPlayers() {
		return new ArrayList<>(players);
	}

	void damageUp(Player player) {

		int indexOfPlayer;
		int oldDamage;

		if (!players.contains(player)) {
			addPlayer(player);
		}

		indexOfPlayer = players.indexOf(player);
		oldDamage = damages.get(indexOfPlayer);
		damages.set(indexOfPlayer, (oldDamage + 1));
	}

	ArrayList<Player> getSortedPlayers() {
		sort();
		return new ArrayList<>(players);
	}

	private void addPlayer(Player player) {
		players.add(player);
		damages.add(0);
	}

	private void sort() {
		Player pToSwap;
		Integer iToSwap;

		while (!isSorted()) {
			for (int i = damages.size() - 1; i > 0; i--) {
				if (damages.get(i) > damages.get(i - 1)) {
					//Swap in damages
					iToSwap = damages.get(i);
					damages.set(i, damages.get(i - 1));
					damages.set(i - 1, iToSwap);

					//Swap in players
					pToSwap = players.get(i);
					players.set(i, players.get(i - 1));
					players.set(i - 1, pToSwap);

				}
			}
		}
	}

	private boolean isSorted() {
		for (int i = 0; i < damages.size() - 1; i++) {
			if (damages.get(i) < damages.get(i + 1)) {
				return false;
			}
		}

		return true;
	}
}