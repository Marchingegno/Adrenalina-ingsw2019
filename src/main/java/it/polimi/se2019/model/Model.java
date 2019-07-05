package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoCard;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.gameboard.KillShot;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerBoard;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.model.player.damagestatus.FrenzyAfter;
import it.polimi.se2019.model.player.damagestatus.FrenzyBefore;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.*;
import it.polimi.se2019.view.server.Event;

import java.util.*;

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
	private boolean hasPayed = false;
	private Event savedEvent;
	private ArrayDeque<String> playersWaitingForDamagePowerups;

	// Game ending variables
	private boolean gameEnded = false;
	private List<PlayerRepPosition> finalPlayerRepPosition;

	public Model(String mapName, List<String> playerNames, int startingSkulls) {
		if (startingSkulls < GameConstants.MIN_SKULLS || startingSkulls > GameConstants.MAX_SKULLS)
			throw new IllegalArgumentException("Invalid number of skulls!");
		if (playerNames.size() > GameConstants.MAX_PLAYERS || playerNames.size() < GameConstants.MIN_PLAYERS)
			throw new IllegalArgumentException("Invalid number of players!");
		gameBoard = new GameBoard(mapName, playerNames, startingSkulls);
		gameMap = gameBoard.getGameMap();
	}


	// ####################################
	// OBSERVERS MANAGEMENT METHODS
	// ####################################

	/**
	 * Adds an observer to the associated gameBoard.
	 *
	 * @param observer the observer that wants to observe the gameBoard.
	 */
	public void addGameBoardObserver(Observer observer) {
		gameBoard.addObserver(observer);
	}

	/**
	 * Adds an observer to the associated gameMap.
	 *
	 * @param observer the observer that wants to observe the gameMap.
	 */
	public void addGameMapObserver(Observer observer) {
		gameBoard.getGameMap().addObserver(observer);
	}

	/**
	 * Adds an observer to all the associated players.
	 *
	 * @param observer the observer that wants to observe all the players.
	 */
	public void addPlayersObserver(Observer observer) {
		for (Player player : gameBoard.getPlayers()) {
			player.addObserver(observer);
		}
	}


	// ####################################
	// GAME MANAGEMENT METHODS
	// ####################################

	/**
	 * Returns true if and only if the game is ended.
	 *
	 * @return true if and only if the game is ended.
	 */
	public boolean isGameEnded() {
		return gameEnded;
	}

	/**
	 * Returns true if and only if the game can continue.
	 *
	 * @return true if and only if the game can continue.
	 */
	public boolean canGameContinue() {
		return gameBoard.canGameContinue();
	}

	/**
	 * Returns true if and only if the frenzy is started.
	 *
	 * @return true if and only if the frenzy is started.
	 */
	public boolean isFrenzyStarted() {
		return gameBoard.isFrenzyStarted();
	}

	/**
	 * Starts the frenzy status.
	 */
	public void startFrenzy() {
		gameBoard.startFrenzy();

		Player firstPlayer = gameBoard.getPlayers().get(0);


		boolean isAfterFirstPlayer = false;
		boolean playerStartingFrenzy = true;
		for (Player player : gameBoard.getPlayerQueue()) {
			//The player that started the frenzy is still at the top of the queue, since nextPlayerTurn has not been called yet.
			//So he should receive FrenzyAfter status, even though he is before first player.
			if (playerStartingFrenzy) {
				playerStartingFrenzy = false;
				player.setDamageStatus(new FrenzyAfter());
				continue;
			}

			//The first must also receive the Frenzy After damage status
			if (player.getPlayerName().equals(firstPlayer.getPlayerName()))
				isAfterFirstPlayer = true;

			player.setDamageStatus(isAfterFirstPlayer ? new FrenzyAfter() : new FrenzyBefore());
		}
		flipPlayersWithNoDamage();

		updateReps();
	}

	/**
	 * Returns true if and only of the skulls are finished.
	 *
	 * @return true if and only of the skulls are finished.
	 */
	public boolean areSkullsFinished() {
		return gameBoard.areSkullsFinished();
	}

	/**
	 * Solves the points of the dead players in this turn and updates the reps.
	 */
	public void scoreDeadPlayers() {
		gameBoard.getPlayers().stream()
				.filter(item -> item.getPlayerBoard().isDead())
				.forEach(this::scoreDeadPlayer);
		gameBoard.setKillShotInThisTurn(false);
		updateReps();
	}

	/**
	 * Refills the map and updates the reps.
	 */
	public void fillGameMap() {
		gameMap.refillMap();
		updateReps();
	}

	/**
	 * Flips the board of the players who have no damage and updates the reps.
	 */
	public void flipPlayersWithNoDamage() {
		gameBoard.getPlayers().forEach(Player::flipIfNoDamage);
		updateReps();
	}


	// ####################################
	// PAYMENT METHODS
	// ####################################

	/**
	 * Returns the price of the selected weapon in the square.
	 *
	 * @param index of the weapon in the square.
	 * @return the price of the selected weapon in the square.
	 */
	public List<AmmoType> getPriceOfTheChosenWeapon(int index) {
		return ((WeaponCard) gameMap.getPlayerSquare(getCurrentPlayer()).getCards().get(index)).getGrabPrice();
	}

	/**
	 * Returns the price of the selected weapon in the player's inventory.
	 *
	 * @param index of the weapon in the inventory.
	 * @return the price of the selected weapon in the player's inventory.
	 */
	public List<AmmoType> getPriceOfTheSelectedWeapon(int index) {
		return ((getCurrentPlayer().getPlayerBoard().getWeaponCards()).get(index)).getReloadPrice();
	}

	/**
	 * Handles the payment.
	 *
	 * @param playerName       name odf the player who is paying.
	 * @param priceToPay       the price in AmmoType that needs to be payed.
	 * @param indexesOfPowerup indexes of the poweups used to pay.
	 */
	public void pay(String playerName, List<AmmoType> priceToPay, List<Integer> indexesOfPowerup) {
		Player player = getPlayerFromName(playerName);
		PlayerBoard playerBoard = player.getPlayerBoard();
		List<AmmoType> price = new ArrayList<>(priceToPay);
		Utils.logInfo("Model -> pay(): " + playerName + " is paying the price of " + price + " with indexes of powerups " + indexesOfPowerup);
		while (!indexesOfPowerup.isEmpty()) {
			Integer max = Collections.max(indexesOfPowerup);
			PowerupCard discardedPowerup = playerBoard.removePowerup(max);
			price.remove(discardedPowerup.getAssociatedAmmo());
			indexesOfPowerup.remove(max);
			gameBoard.getPowerupDeck().discardCard(discardedPowerup); // Put discarded powerup in the discard deck.
		}
		playerBoard.getAmmoContainer().removeAmmo(price);
		setPayed(true);
	}

	/**
	 * Returns true if and only if the specified player has at least one powerup he can use to pay the price.
	 *
	 * @param playerName player who is paying.
	 * @param ammoToPay  price to pay.
	 * @return true if and only if the specified player has at least one powerup he can use to pay the price.
	 */
	public boolean canUsePowerupToPay(String playerName, List<AmmoType> ammoToPay) {
		List<PowerupCard> powerupCards = getPlayerFromName(playerName).getPlayerBoard().getPowerupCards();
		Utils.logInfo("Model() -> canUsePowerupToPay: " + playerName + " needs to pay " + ammoToPay + " and has " + powerupCards + " and " + getCurrentPlayer().getPlayerBoard().getAmmoContainer().getAmmo());
		for (PowerupCard powerupCard : powerupCards) {
			if (ammoToPay.contains(powerupCard.getAssociatedAmmo()))
				return true;
		}
		Utils.logInfo("Model() -> canUsePowerupToPay: " + playerName + " has no powerup to pay with");
		return false;
	}

	/**
	 * Returns true if and only if the player can afford the price without using powerups.
	 *
	 * @param playerName player who is paying.
	 * @param ammoToPay  price to pay.
	 * @return true if and only if the player can afford the price without using powerups.
	 */
	public boolean canAffordWithOnlyAmmo(String playerName, List<AmmoType> ammoToPay) {
		Player player = getPlayerFromName(playerName);
		return player.getPlayerBoard().getAmmoContainer().hasEnoughAmmo(ammoToPay);
	}

	/**
	 * Sets the hasPayed variable to the specified value.
	 *
	 * @param hasPayed value to set.
	 */
	public void setPayed(boolean hasPayed) {
		this.hasPayed = hasPayed;
	}

	/**
	 * Returns true if and only if the current player has already payed.
	 *
	 * @return true if and only if the current player has already payed.
	 */
	public boolean hasCurrentPlayerPayed() {
		return hasPayed;
	}

	/**
	 * Saves the event so that it can be processed later.
	 *
	 * @param event the event to save.
	 */
	public void saveEvent(Event event) {
		this.savedEvent = event;
	}

	/**
	 * Returns the event to resume.
	 *
	 * @return the event to resume;
	 */
	public Event resumeAction() {
		return savedEvent;
	}


	// ####################################
	// PLAYERS MANAGEMENT METHODS
	// ####################################

	/**
	 * Returns true if and only if the specified player is in a macro action.
	 *
	 * @param playerName player name of the player to check.
	 * @return true if and only if the specified player is in a macro action.
	 */
	public boolean isInAMacroAction(String playerName) {
		Player player = getPlayerFromName(playerName);
		return player.getDamageStatus().getCurrentMacroActionIndex() != -1;
	}

	/**
	 * Ends the action of the specified player.
	 *
	 * @param playerName name of the player.
	 */
	public void endAction(String playerName) {
		Player player = getPlayerFromName(playerName);
		gameBoard.endAction(player);
	}

	/**
	 * Sets the specified player as disconnected and updates the reps.
	 *
	 * @param playerName name of the player to set as disconnected.
	 */
	public void setAsDisconnected(String playerName) {
		Player player = getPlayerFromName(playerName);
		player.setConnected(false);
		long connectedPlayers = gameBoard.getPlayers().stream()
				.filter(Player::isConnected)
				.count();
		if (connectedPlayers < GameConstants.MIN_PLAYERS) {
			Utils.logInfo("Connected players less than minimum, ending the game...");
			endGameAndFindWinner();
		}
		updateReps();
	}

	/**
	 * Sets the specified player as reconnected and updates the reps.
	 *
	 * @param playerName name of the player that has reconnected.
	 */
	public void setAsReconnected(String playerName) {
		Player player = getPlayerFromName(playerName);
		player.setConnected(true);
		forceUpdateOfReps();
	}

	/**
	 * Resets the action of the specified player.
	 *
	 * @param playerName name of the player that needs to be reset.
	 */
	public void cancelAction(String playerName) {
		Player player = getPlayerFromName(playerName);

		// Reset weapon.
		if (player.isShootingWeapon())
			player.handleWeaponEnd();

		// Reset powerup.
		if (player.isPowerupInExecution())
			handlePowerupEnd(playerName);

		// Finish actions.
		while (player.getDamageStatus().hasMacroActionLeft())
			player.getDamageStatus().decreaseMacroActionsToPerform();

		// Reset payment
		hasPayed = false;
		savedEvent = null;
	}

	/**
	 * Moves the player to the specified coordinates and updates the reps.
	 *
	 * @param playerName  name of the player to move.
	 * @param coordinates coordinates of the square where the player needs to be moved.
	 */
	public void movePlayerTo(String playerName, Coordinates coordinates) {
		Player player = getPlayerFromName(playerName);
		gameMap.movePlayerTo(player, coordinates);
		updateReps();
	}

	/**
	 * Spawns the player and updates the reps.
	 *
	 * @param playerName  name of the player that needs to spawn.
	 * @param indexOfCard index of the powerup chosen to spawn.
	 */
	public void spawnPlayer(String playerName, int indexOfCard) {
		Player player = getPlayerFromName(playerName);
		gameBoard.spawnPlayer(player, indexOfCard);
		updateReps();
	}

	/**
	 * Adds a powerup to the specified player's inventory and updates the reps.
	 *
	 * @param playerName name of the player that needs to receive the powerup.
	 */
	void addPowerupCardTo(String playerName) {
		Player player = getPlayerFromName(playerName);
		gameBoard.addPowerupCardTo(player);
		updateReps();
	}

	/**
	 * Adds a powerup to the specified player's inventory in order to spawn and updates the reps.
	 *
	 * @param playerName name of the player that needs to receive the powerup.
	 */
	public void addSpawnPowerupCardTo(String playerName) {
		getGameBoard().addSpawnPowerupCardTo(getPlayerFromName(playerName));
		updateReps();
	}

	/**
	 * Returns the name of the current player.
	 *
	 * @return the name of the current player.
	 */
	public String getCurrentPlayerName() {
		return gameBoard.getCurrentPlayer().getPlayerName();
	}

	/**
	 * Sets the current player to IDLE and the next player as YOUR_TURN; then updates the reps.
	 */
	public void nextPlayerTurn() {
		gameBoard.nextPlayerTurn();
		updateReps();
	}

	/**
	 * Sets the correct damage status according to the damage of the player and if the game is in frenzy;
	 * then updates the reps.
	 *
	 * @param playerName name of the player to whom the damage status needs to be changed.
	 */
	public void setCorrectDamageStatus(String playerName) {
		Player player = getPlayerFromName(playerName);
		gameBoard.setCorrectDamageStatus(player);
		updateReps();
	}

	/**
	 * Returns the turn status of the specified player.
	 *
	 * @param playerName name of the player.
	 * @return the turn status of the specified player.
	 */
	public TurnStatus getTurnStatus(String playerName) {
		return getPlayerFromName(playerName).getTurnStatus();
	}

	/**
	 * Sets the specified turn status to the current player and updates the reps.
	 *
	 * @param turnStatus the turn status to set.
	 */
	public void setTurnStatusOfCurrentPlayer(TurnStatus turnStatus) {
		Player player = getCurrentPlayer();
		gameBoard.setTurnStatus(player, turnStatus);
		updateReps();
	}

	/**
	 * Returns true if and only if the player's weapon inventory is full.
	 *
	 * @return true if and only if the player's weapon inventory is full.
	 */
	public boolean currPlayerHasWeaponInventoryFull() {
		return getCurrentPlayer().getPlayerBoard().numOfWeapons() == GameConstants.MAX_WEAPON_CARDS_PER_PLAYER;
	}

	/**
	 * Returns the coordinates where the current player can move and perform an action.
	 *
	 * @return the coordinates where the current player can move and perform an action.
	 */
	public List<Coordinates> getCoordinatesWherePlayerCanMove() {
		if (getCurrentPlayer().getDamageStatus().getCurrentMacroAction().isGrab()) {
			int numberOfMovements = gameBoard.getCurrentPlayer().getDamageStatus().getCurrentMacroAction().getNumOfMovements();
			return gameMap.getCoordinatesWhereCurrentPlayerCanGrab(getCurrentPlayer(), numberOfMovements);
		} else {
			return getReachableCoordinatesOfTheCurrentPlayer();
		}
	}

	/**
	 * Returns the coordinate where the current player can move.
	 *
	 * @return the coordinate where the current player can move.
	 */
	public List<Coordinates> getReachableCoordinatesOfTheCurrentPlayer() {
		int numberOfMovements = gameBoard.getCurrentPlayer().getDamageStatus().getCurrentMacroAction().getNumOfMovements();
		return getReachableCoordinates(getCurrentPlayerName(), numberOfMovements);
	}

	/**
	 * Returns the coordinate where the current player can move within the specified distance.
	 *
	 * @param playerName the name of the player who is moving.
	 * @param distance   max distance that the player can travel.
	 * @return the coordinate where the current player can move within the specified distance.
	 */
	public List<Coordinates> getReachableCoordinates(String playerName, int distance) {
		Player player = getPlayerFromName(playerName);
		return gameMap.reachableCoordinates(player, distance);
	}

	/**
	 * Returns the indexes of the weapons that the current player can afford in the square where he is.
	 *
	 * @return the indexes of the weapons that the current player can afford in the square where he is.
	 */
	public List<Integer> getIndexesOfTheGrabbableWeaponCurrentPlayer() {
		Player player = getCurrentPlayer();
		List<Integer> indexes = new ArrayList<>();
		List<Card> weapons = gameMap.getPlayerSquare(player).getCards();
		for (int i = 0; i < weapons.size(); i++) {
			if (hasEnoughAmmo(player, (WeaponCard) weapons.get(i))) {
				indexes.add(i);
			}
		}
		if (indexes.isEmpty())
			throw new IllegalStateException("The player should grab at least one weapon");
		Utils.logInfo("Model -> getIndexesOfTheGrabbableWeaponCurrentPlayer(): player can grab " + indexes);
		return indexes;
	}

	/**
	 * Returns the grab message type to use according to the current player's position.
	 *
	 * @return the grab message type to use according to the current player's position.
	 */
	public MessageType getGrabMessageType() {
		return gameMap.getPlayerSquare(getCurrentPlayer()).getGrabMessageType();
	}

	/**
	 * Removes from the square where the player is standing the specified weapon and adds it to the player inventory and then updates the reps.
	 *
	 * @param playerName player who is grabbing the weapon.
	 * @param index      of the weapon to grab.
	 */
	public void grabWeaponCard(String playerName, int index) {
		Player player = getPlayerFromName(playerName);
		WeaponCard cardToGrab = (WeaponCard) gameMap.grabCard(gameMap.getPlayerCoordinates(player), index);
		cardToGrab.setOwner(player);
		cardToGrab.load();
		player.getPlayerBoard().addWeapon(cardToGrab);
		updateReps();
	}

	/**
	 * Removes the ammo card from the square where the player is standing and adds its content to the player inventory and then updates the reps.
	 *
	 * @param playerName player who is grabbing.
	 */
	public void grabAmmoCard(String playerName) {
		Player player = getPlayerFromName(playerName);
		AmmoCard cardToGrab = (AmmoCard) gameMap.grabCard(gameMap.getPlayerCoordinates(player), 0);
		addAmmoCardToPlayer(player, cardToGrab);
		updateReps();
	}

	/**
	 * Swaps the specified weapon in the player's inventory with the specified weapon in the spawn square and then updates the reps.
	 *
	 * @param indexOfThePlayerWeapon index of the player's weapon to swap.
	 * @param indexOfTheSpawnWeapon  index of the weapon in the square to grab.
	 */
	public void swapWeapons(int indexOfThePlayerWeapon, int indexOfTheSpawnWeapon) {
		Player player = getCurrentPlayer();
		Coordinates playerCoordinates = gameMap.getPlayerCoordinates(player);
		WeaponCard squareWeapon = (WeaponCard) (gameMap.grabCard(playerCoordinates, indexOfTheSpawnWeapon));
		squareWeapon.setOwner(player);
		squareWeapon.load();
		WeaponCard playerWeapon = player.getPlayerBoard().swapWeapon(squareWeapon, indexOfThePlayerWeapon);
		gameMap.addCard(playerCoordinates, playerWeapon);
		updateReps();
	}

	/**
	 * Reloads the specified weapon in the player's inventory and then updates the reps.
	 *
	 * @param playerName       player who is reloading.
	 * @param indexOfTheWeapon index of the weapon to reload.
	 */
	public void reloadWeapon(String playerName, int indexOfTheWeapon) {
		Player player = getPlayerFromName(playerName);
		player.reload(indexOfTheWeapon);
		updateReps();
	}


	// ####################################
	// ENDGAME METHODS
	// ####################################

	public void endGameAndFindWinner() {
		gameEnded = true;
		finalPlayerRepPosition = new ArrayList<>();
		List<LeaderboardSlot> tempLeaderBoard;

		Utils.logInfo("Model -> endGameAndFindWinner(): Initiating endGame.");

		scorePlayersInEndGame();
		awardKillshotTrackPoint();

		//This sets the winners in their positions without tiebreaking.
		tempLeaderBoard = setWinners();

		updateReps();

		//tieBreak returns the final scores.
		finalPlayerRepPosition = tieBreak(tempLeaderBoard);
	}

	private void scorePlayersInEndGame() {
		//This will score EVERY player in the endgame phase.
		gameBoard.getPlayers().forEach(this::scorePlayerEndGame);
	}

	private void scorePlayerEndGame(Player player) {
		Utils.logInfo("Model -> scorePlayerEndGame(): Scoring " + player.getPlayerName());
		PlayerBoard playerBoard = player.getPlayerBoard();
		DamageDone damageDone = new DamageDone();
		List<Player> sortedPlayers;

		playerBoard.getDamageBoard().forEach(damageDone::damageUp);

		sortedPlayers = damageDone.getSortedPlayers();
		awardPoints(playerBoard, sortedPlayers);
	}

	private void awardKillshotTrackPoint() {
		//Here I will use DamageDone since it's a similar procedure
		//to scoring DamageBoards and has the capability to sort itself.
		DamageDone damageDone = new DamageDone();
		List<KillShot> killShotList = gameBoard.getKillShots();
		List<Player> sortedPlayers;

		Utils.logInfo("Model -> awardKillshotTrackPoint(): Awarding killshots points.");

		for (KillShot killShot : killShotList) {
			damageDone.damageUp(killShot.getPlayer());
			if (killShot.isOverkill()) {
				damageDone.damageUp(killShot.getPlayer());
			}
		}

		sortedPlayers = damageDone.getSortedPlayers();

		int offset = 0;
		for (Player p : sortedPlayers) {
			Utils.logInfo("Model -> awardKillShotTrackPoint(): Killshot: player " + p.getPlayerName() + " is awarded " + GameConstants.KILLSHOT_SCORES.get(offset) + " points.");
			p.getPlayerBoard().addPoints(GameConstants.KILLSHOT_SCORES.get(offset));
			offset++;
		}

	}

	private List<LeaderboardSlot> setWinners() {
		List<LeaderboardSlot> leaderboard = new ArrayList<>();

		for (Player player : gameBoard.getPlayers()) {
			player.updateRep();
		}

		Utils.logInfo("Model -> setWinners(): Finding winners.");

		Comparator<Player> playerComparator = (o1, o2) -> {
			int firstPoints = o1.getPlayerBoard().getPoints();
			int secondPoints = o2.getPlayerBoard().getPoints();

			return secondPoints - firstPoints;
		};

		List<Player> players = gameBoard.getPlayers();

		Utils.logInfo("Model -> setWinners(): Players before sorting: ");
		players.forEach(player -> Utils.logInfo("\t\t" + player.getPlayerName() + "with points " + player.getPlayerBoard().getPoints()));

		players.sort(playerComparator);

		Utils.logInfo("----------------------------------------------------------");
		Utils.logInfo("Model -> setWinners(): Players after sorting: ");
		players.forEach(player -> Utils.logInfo("\t\t" + player.getPlayerName() + "with points " + player.getPlayerBoard().getPoints()));


		int precPoint = -1;
		int precIndex = -1;
		int tiesFound = 0;
		for (int i = 0; i < players.size(); i++) {
			Player currPlayer = players.get(i);
			Utils.logInfo("Adding to leaderbord player " + currPlayer.getPlayerName());
			if (currPlayer.getPlayerBoard().getPoints() == precPoint) {
				//This is a tie,
				Utils.logInfo("Found a tie. Points " + currPlayer.getPlayerBoard().getPoints());
				Utils.logInfo("Adding in position " + precIndex);
				leaderboard.get(precIndex).addInPosition(currPlayer);
				tiesFound++;
			} else {
				Utils.logInfo("Adding in position " + (i - tiesFound));
				precPoint = currPlayer.getPlayerBoard().getPoints();
				precIndex = i - tiesFound;
				leaderboard.add(i - tiesFound, new LeaderboardSlot());
				leaderboard.get(i - tiesFound).addInPosition(currPlayer);

			}
		}

		return leaderboard;
	}

	private List<PlayerRepPosition> tieBreak(List<LeaderboardSlot> leaderboard) {
		List<PlayerRepPosition> playerRepLeaderboard = new ArrayList<>();

		Utils.logInfo("Model -> tieBreak(): Breaking ties.");

		int nextPosition = 0;
		for (int i = 0; i < leaderboard.size(); i++) {
			if (leaderboard.get(i).isATie()) {
				List<Player> tiedPlayers = leaderboard.get(i).getPlayersInThisPosition();
				List<Player> orderedPlayers = new ArrayList<>();
				List<Player> alreadyAddedPlayers = new ArrayList<>();

				List<KillShot> killShotTrack = gameBoard.getKillShots();
				for (int j = 0; j < killShotTrack.size(); j++) {
					if (tiedPlayers.indexOf(killShotTrack.get(j).getPlayer()) != -1) {
						//I found a tied player in the killshot track.
						//This player should be the next in leaderboard.
						if (alreadyAddedPlayers.indexOf(killShotTrack.get(j).getPlayer()) == -1) {
							orderedPlayers.add(killShotTrack.get(j).getPlayer());
							alreadyAddedPlayers.add(killShotTrack.get(j).getPlayer());
						}
					}
				}
				//Here, if the size of orderedPlayers is less than tiedPlayers, it means that
				//one or more tied players have not obtained killshots.
				//These player(s) should be classified right after ordered players.
				//Let's add the player we found to the leaderboard.

				//lets add all ordered players to new slots
				for (Player player : orderedPlayers) {
					playerRepLeaderboard.add(nextPosition, new PlayerRepPosition());
					playerRepLeaderboard.get(nextPosition).addInPosition((PlayerRep) player.getRep());
					Utils.logInfo("Model -> tieBreak(): Adding in position " + nextPosition + " player " + player.getPlayerName());
					nextPosition++;
				}

				tiedPlayers.removeAll(orderedPlayers);
				//These remaining players are DEFINITELY tied together.
				playerRepLeaderboard.add(nextPosition, new PlayerRepPosition());
				if (!tiedPlayers.isEmpty()) {
					for (Player player : tiedPlayers) {
						playerRepLeaderboard.get(nextPosition).addInPosition((PlayerRep) player.getRep());
						Utils.logInfo("Model -> tieBreak(): Adding in position " + nextPosition + " player " + player.getPlayerName());
					}
					nextPosition++;
				}
			} else {
				playerRepLeaderboard.add(nextPosition, new PlayerRepPosition());
				playerRepLeaderboard.get(nextPosition).addInPosition((PlayerRep) leaderboard.get(i).getPlayer().getRep());
				Utils.logInfo("Model -> tieBreak(): Adding in position " + nextPosition + " player " + leaderboard.get(i).getPlayer().getPlayerName());
				nextPosition++;
			}
		}
		playerRepLeaderboard.forEach(item -> Utils.logInfo(item.toString()));
		return playerRepLeaderboard;
	}

	public List<PlayerRepPosition> getFinalPlayersInfo() {
		return finalPlayerRepPosition;
	}


	// ####################################
	// MACRO ACTION METHODS
	// ####################################

	/**
	 * Returns true if and only of the player has actions left.
	 *
	 * @param playerName name of the player to check.
	 * @return true if and only of the player has actions left.
	 */
	public boolean doesThePlayerHaveActionsLeft(String playerName) {
		return getPlayerFromName(playerName).getDamageStatus().hasMacroActionLeft();
	}

	/**
	 * Returns the macroaction of the current player.
	 *
	 * @return the macroaction of the current player.
	 */
	public MacroAction getCurrentAction() {
		return gameBoard.getCurrentPlayer().getDamageStatus().getCurrentMacroAction();
	}

	/**
	 * Returns the next action the specified player needs to perform and than sets that action as current.
	 *
	 * @param playerName name of the player to check.
	 * @return the next action the specified player needs to perform.
	 */
	public ActionType getNextActionToExecuteAndAdvance(String playerName) {
		Player player = getPlayerFromName(playerName);
		return player.getDamageStatus().getNextActionToExecuteAndAdvance();
	}

	/**
	 * Sets the nex macroaction.
	 *
	 * @param playerName         name of the player to whom set the next macroaction.
	 * @param indexOfMacroAction index of the macroaction to set.
	 */
	public void setNextMacroAction(String playerName, int indexOfMacroAction) {
		Player player = getPlayerFromName(playerName);
		player.getDamageStatus().decreaseMacroActionsToPerform();
		player.getDamageStatus().setCurrentMacroActionIndex(indexOfMacroAction);
		Utils.logInfo("Model -> setNextMacroAction(): Set " + playerName + "'s next macro action to " + player.getDamageStatus().getCurrentMacroAction().toString());
		updateReps();
	}


	// ####################################
	// PUBLIC WEAPONS USE METHODS
	// ####################################

	/**
	 * Returns true if and only if the current weapon has finished to shoot.
	 *
	 * @param playerName the name of the player who is shooting.
	 * @return true if and only if the current weapon has finished to shoot.
	 */
	public boolean isTheWeaponConcluded(String playerName) {
		return getPlayerFromName(playerName).isTheWeaponConcluded();
	}

	/**
	 * Returns true if and only if the specified weapon can be activated.
	 *
	 * @param playerName    name of the owner of the weapon.
	 * @param indexOfWeapon index of the weapon in the player's inventory.
	 * @return true if and only if the specified weapon can be activated.
	 */
	public boolean canWeaponBeActivated(String playerName, int indexOfWeapon) {
		Player player = getPlayerFromName(playerName);
		List<WeaponCard> weaponCards = player.getPlayerBoard().getWeaponCards();

		if (indexOfWeapon >= weaponCards.size() || indexOfWeapon < 0)
			return false;

		return weaponCards.get(indexOfWeapon).canBeActivated();
	}

	public boolean isPaymentStep(String playerName) {
		Player player = getPlayerFromName(playerName);
		return player.getFiringWeapon().isPaymentStep();
	}

	public List<AmmoType> getFiringCost(String playerName, int choice) {
		Player player = getPlayerFromName(playerName);
		return player.getFiringWeapon().getFiringCost(choice);
	}

	/**
	 * Returns a list of weapons that can be activated in the current state.
	 *
	 * @param playerName the name of the player.
	 * @return a list of weapons that can be activated in the current state.
	 */
	public List<Integer> getActivableWeapons(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<WeaponCard> weaponCards = player.getPlayerBoard().getWeaponCards();
		List<Integer> activableWeapons = new ArrayList<>();

		for (int i = 0; i < weaponCards.size(); i++) {
			if (weaponCards.get(i).canBeActivated())
				activableWeapons.add(i);
		}

		return activableWeapons;
	}

	/**
	 * Returns a list of weapons that can be reloaded in the current state.
	 *
	 * @param playerName the name of the player.
	 * @return a list of weapons that can be reloaded in the current state.
	 */
	public List<Integer> getLoadableWeapons(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<WeaponCard> weaponCards = player.getPlayerBoard().getWeaponCards();
		List<Integer> loadableWeapons = new ArrayList<>();

		for (int i = 0; i < weaponCards.size(); i++) {
			if (!weaponCards.get(i).isLoaded() && getCurrentPlayer().hasEnoughAmmo(weaponCards.get(i).getReloadPrice()))
				loadableWeapons.add(i);
		}

		return loadableWeapons;
	}

	/**
	 * Returns true if the player has at least one loaded weapon.
	 *
	 * @param playerName the name of the player.
	 * @return true if the player has at least one loaded weapon.
	 */
	public boolean doesPlayerHaveLoadedWeapons(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<WeaponCard> weaponCards = player.getPlayerBoard().getWeaponCards();

		return weaponCards.stream().anyMatch(WeaponCard::isLoaded);
	}

	/**
	 * Returns true if and only if the player is shooting.
	 *
	 * @param playerName name of the player to check.
	 * @return true if and only if the player is shooting.
	 */
	public boolean isShootingWeapon(String playerName) {
		Player player = getPlayerFromName(playerName);
		return player.isShootingWeapon();
	}

	/**
	 * Sets the specified weapon as the shooting weapon of the player and then updates the reps.
	 *
	 * @param playerName    name of the player to whom set the weapon.
	 * @param indexOfWeapon index of the weapon to set as the shooting weapon.
	 * @return the initial question container.
	 */
	public QuestionContainer initialWeaponActivation(String playerName, int indexOfWeapon) {
		Player player = getPlayerFromName(playerName);
		QuestionContainer questionContainer = player.initialWeaponActivation(indexOfWeapon);
		updateReps();
		return questionContainer;
	}

	/**
	 * Communicates the choice of the player, advance the weapon step and updates the reps.
	 *
	 * @param playerName name of the player that is shooting.
	 * @param choice     index of the choice of the player.
	 * @return the next question container to show to the player.
	 */
	public QuestionContainer doWeaponStep(String playerName, int choice) {
		Player player = getPlayerFromName(playerName);
		QuestionContainer questionContainer = player.doWeaponStep(choice);
		updateReps();
		return questionContainer;
	}

	/**
	 * Handles the end of the weapon, and checks if the shooting player has the targeting scope
	 * and the damaged players have the tagback grenade; then updates the reps.
	 *
	 * @param playerName name of the shooting player.
	 */
	public void handleWeaponEnd(String playerName) {
		Player player = getPlayerFromName(playerName);
		saveShootedPlayersForShootPowerups(player);
		initiatePlayerWaitingForDamagePowerups(player);
		player.handleWeaponEnd();
		updateReps();
	}


	// ####################################
	// PUBLIC POWERUPS USE METHODS
	// ####################################

	/**
	 * Returns true if and only if the current powerup is concluded.
	 *
	 * @param playerName the name of the player who is using the powerup.
	 * @return true if and only if the current powerup id concluded.
	 */
	public boolean isThePowerupConcluded(String playerName) {
		Player player = getPlayerFromName(playerName);
		return player.isThePowerupConcluded();
	}

	/**
	 * Returns true if the powerup can be activated.
	 *
	 * @param playerName the player that wants to activate the powerup.
	 * @param indexOfPowerup the index of the powerup to activate.
	 * @return true if the powerup can be activated.
	 */
	public boolean canPowerupBeActivated(String playerName, int indexOfPowerup) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();

		if (indexOfPowerup >= powerupCards.size() || indexOfPowerup < 0)
			return false;

		PowerupCard powerupCard = powerupCards.get(indexOfPowerup);
		return powerupCard.canBeActivated();
	}

	/**
	 * Returns true if the player has an activable powerup of type ON_SHOOT.
	 *
	 * @param playerName the player to check for.
	 * @return true if the player has an activable powerup of type ON_SHOOT.
	 */
	public boolean doesPlayerHaveActivableOnShootPowerups(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();

		return powerupCards.stream()
				.anyMatch(powerupCard -> powerupCard.getUseCase() == PowerupCard.PowerupUseCaseType.ON_SHOOT && powerupCard.canBeActivated());
	}

	/**
	 * Returns a list of activable powerup of type ON_SHOOT for the selected player.
	 *
	 * @param playerName the player to check for activable powerups.
	 * @return a list of activable powerup of type ON_SHOOT.
	 */
	public List<Integer> getActivableOnShootPowerups(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();
		List<Integer> activablePowerups = new ArrayList<>();

		for (int i = 0; i < powerupCards.size(); i++) {
			if (powerupCards.get(i).getUseCase() == PowerupCard.PowerupUseCaseType.ON_SHOOT && powerupCards.get(i).canBeActivated()) {
				activablePowerups.add(i);
			}
		}

		return activablePowerups;
	}

	/**
	 * Returns the next player waiting to do his damage powerup.
	 *
	 * @return the next player waiting to do his damage powerup.
	 */
	public String getNextPlayerWaitingForDamagePowerups() {
		return playersWaitingForDamagePowerups.poll();
	}

	/**
	 * Returns true if there aren't anymore players left to do their damage powerups.
	 *
	 * @return true if there aren't anymore players left to do their damage powerups.
	 */
	public boolean isPlayerWaitingForDamagePowerupsEmpty() {
		return playersWaitingForDamagePowerups == null || playersWaitingForDamagePowerups.isEmpty();
	}

	/**
	 * Returns a list of activable powerup of type ON_DAMAGE for the selected player.
	 *
	 * @param damagedPlayerName the player to check for activable powerups.
	 * @param shootingPlayerName the player shooting.
	 * @return a list of activable powerup of type ON_SHOOT.
	 */
	public List<Integer> getActivableOnDamagePowerups(String damagedPlayerName, String shootingPlayerName) {
		Player damagedPlayer = getPlayerFromName(damagedPlayerName);
		Player shootingPlayer = getPlayerFromName(shootingPlayerName);
		List<PowerupCard> powerupCards = damagedPlayer.getPlayerBoard().getPowerupCards();
		List<Integer> activablePowerups = new ArrayList<>();
		Utils.logInfo("Model -> getActivableOnDamagePowerups(): checking if " + shootingPlayerName + " has activable powerups");

		for (int i = 0; i < powerupCards.size(); i++) {
			if (powerupCards.get(i).getUseCase() == PowerupCard.PowerupUseCaseType.ON_DAMAGE) {
				Utils.logInfo("Model -> getActivableOnDamagePowerups(): checking powerup " + powerupCards.get(i));
				powerupCards.get(i).setShootingPlayer(shootingPlayer);
				if (powerupCards.get(i).canBeActivated()) {
					Utils.logInfo("Model -> getActivableOnDamagePowerups(): added " + powerupCards.get(i));
					activablePowerups.add(i);
				} else
					powerupCards.get(i).setShootingPlayer(null);
			}
		}

		return activablePowerups;
	}

	/**
	 * Returns a list of activable powerup of type ON_TURN for the selected player.
	 *
	 * @param playerName the player to check for activable powerups.
	 * @return a list of activable powerup of type ON_TURN.
	 */
	public List<Integer> getActivableOnTurnPowerups(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();
		List<Integer> activablePowerups = new ArrayList<>();

		for (int i = 0; i < powerupCards.size(); i++) {
			if (powerupCards.get(i).getUseCase() == PowerupCard.PowerupUseCaseType.ON_TURN && powerupCards.get(i).canBeActivated()) {
				activablePowerups.add(i);
			}
		}

		return activablePowerups;
	}

	/**
	 * Returns true if the player has an activable powerup of type ON_TURN.
	 *
	 * @param playerName the player to check for.
	 * @return true if the player has an activable powerup of type ON_TURN.
	 */
	public boolean doesPlayerHaveActivableOnTurnPowerups(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();

		return powerupCards.stream()
				.anyMatch(powerupCard -> powerupCard.getUseCase() == PowerupCard.PowerupUseCaseType.ON_TURN && powerupCard.canBeActivated());
	}

	/**
	 * Returns true if there is a powerup in execution.
	 * @param playerName the player to check for.
	 * @return true if there is a powerup in execution.
	 */
	public boolean isPowerupInExecution(String playerName) {
		Player player = getPlayerFromName(playerName);
		return player.isPowerupInExecution();
	}

	/**
	 * Sets the specified powerup as the activated powerup of the player and then updates the reps.
	 *
	 * @param playerName name of the player.
	 * @param indexOfPowerup index of the powerup to set as the activated powerup.
	 * @return the initial question container.
	 */
	public QuestionContainer initialPowerupActivation(String playerName, int indexOfPowerup) {
		Player player = getPlayerFromName(playerName);
		QuestionContainer questionContainer = player.initialPowerupActivation(indexOfPowerup);
		updateReps();
		return questionContainer;
	}

	/**
	 * Communicates the choice of the player, advance the powerup step and updates the reps.
	 *
	 * @param playerName name of the player that is activating the powerup.
	 * @param choice index of the choice of the player.
	 * @return the next question container to show to the player.
	 */
	public QuestionContainer doPowerupStep(String playerName, int choice) {
		Player player = getPlayerFromName(playerName);
		QuestionContainer questionContainer = player.doPowerupStep(choice);
		updateReps();
		return questionContainer;
	}

	/**
	 * Handles the end of the powerup while resetting its attributes.
	 *
	 * @param playerName name of the player.
	 */
	public void handlePowerupEnd(String playerName) {
		Player player = getPlayerFromName(playerName);
		PowerupCard powerupCardToDiscard = player.handlePowerupEnd();
		gameBoard.getPowerupDeck().discardCard(powerupCardToDiscard);
		updateReps();
	}


	// ####################################
	// PROTECTED METHODS
	// ####################################

	protected Player getPlayerFromName(String playerName) {
		for (Player player : gameBoard.getPlayers()) {
			if (playerName.equals(player.getPlayerName()))
				return player;
		}
		throw new IllegalArgumentException("No player with name: " + playerName);
	}


	// ####################################
	// PRIVATE METHODS
	// ####################################

	private void saveShootedPlayersForShootPowerups(Player shootingPlayer) {
		List<Player> playersHit = shootingPlayer.getPlayersHitWithWeapon();
		for (PowerupCard powerupCard : shootingPlayer.getPlayerBoard().getPowerupCards()) {
			if (powerupCard.getUseCase() == PowerupCard.PowerupUseCaseType.ON_SHOOT)
				powerupCard.setShotPlayers(playersHit);
		}
	}

	private void initiatePlayerWaitingForDamagePowerups(Player shootingPlayer) {
		List<Player> playersHit = shootingPlayer.getPlayersHitWithWeapon();
		playersWaitingForDamagePowerups = new ArrayDeque<>();
		for (Player damagedPlayer : playersHit) {
			if (damagedPlayer.isConnected() && doesPlayerHaveActivableOnDamagePowerups(damagedPlayer.getPlayerName(), shootingPlayer.getPlayerName()))
				playersWaitingForDamagePowerups.add(damagedPlayer.getPlayerName());
		}
	}

	private boolean doesPlayerHaveActivableOnDamagePowerups(String damagedPlayerName, String shootingPlayerName) {
		return !getActivableOnDamagePowerups(damagedPlayerName, shootingPlayerName).isEmpty();
	}

	private Player getCurrentPlayer() {
		return gameBoard.getCurrentPlayer();
	}

	private void addAmmoCardToPlayer(Player player, AmmoCard ammoCard) {
		for (AmmoType ammo : ammoCard.getAmmo()) {
			player.getPlayerBoard().getAmmoContainer().addAmmo(ammo);
		}

		if (ammoCard.hasPowerup() && player.getPlayerBoard().getPowerupCards().size() < GameConstants.MAX_POWERUP_CARDS_PER_PLAYER)
			gameBoard.addPowerupCardTo(player);

		gameBoard.getAmmoDeck().discardCard(ammoCard);

		updateReps();
	}

	private void scoreDeadPlayer(Player player) {
		PlayerBoard playerBoard = player.getPlayerBoard();
		DamageDone damageDone = new DamageDone();
		List<Player> sortedPlayers;
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

	private void awardPoints(PlayerBoard playerBoardToScore, List<Player> sortedPlayers) {
		int offset = 0;
		if (playerBoardToScore.getDamageBoard().isEmpty()) {
			return;
		}

		//This method relies on the "PLAYER_SCORES" array defined in GameConstants.
		if (playerBoardToScore.isFlipped()) {
			for (Player p : sortedPlayers) {
				p.getPlayerBoard().addPoints(GameConstants.FRENZY_SCORES.get(offset));
				offset++;
			}
		} else {
			//AWARD FIRST BLOOD POINT wronggg
			sortedPlayers.get(sortedPlayers.indexOf(playerBoardToScore.getDamageBoard().get(0))).getPlayerBoard().addPoints(GameConstants.FIRST_BLOOD_SCORE);

			for (Player p : sortedPlayers) {
				p.getPlayerBoard().addPoints(GameConstants.PLAYER_SCORES.get(playerBoardToScore.getNumberOfDeaths() + offset));
				offset++;
			}

		}
		updateReps();
	}

	private boolean hasEnoughAmmo(Player player, WeaponCard weapon) {
		return player.hasEnoughAmmo(weapon.getGrabPrice());
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

	private void forceUpdateOfReps() {
		gameBoard.forceUpdateOfReps();
		gameBoard.notifyObservers();
		gameMap.forceUpdateOfReps();
		gameMap.notifyObservers();
		for (Player player : gameBoard.getPlayers()) {
			player.forceUpdateOfReps();
			player.notifyObservers();
		}
	}

	// ####################################
	// METHODS ONLY FOR TESTS
	// ####################################

	protected GameBoard getGameBoard() {
		return gameBoard;
	}
}

