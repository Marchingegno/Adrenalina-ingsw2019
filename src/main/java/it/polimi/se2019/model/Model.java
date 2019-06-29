package it.polimi.se2019.model;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoCard;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.gameboard.GameBoard;
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
	private List<PlayersPosition> finalPlayersPosition;

	public Model(String mapName, List<String> playerNames, int startingSkulls) {
		if(startingSkulls < GameConstants.MIN_SKULLS || startingSkulls > GameConstants.MAX_SKULLS)
			throw new IllegalArgumentException("Invalid number of skulls!");
		if(playerNames.size() > GameConstants.MAX_PLAYERS || playerNames.size() < GameConstants.MIN_PLAYERS)
			throw new IllegalArgumentException("Invalid number of players!");
		gameBoard = new GameBoard(mapName, playerNames, startingSkulls);
		gameMap = gameBoard.getGameMap();
	}


	// ####################################
	// OBSERVERS MANAGEMENT METHODS
	// ####################################

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


	// ####################################
	// GAME MANAGEMENT METHODS
	// ####################################

	public boolean isGameEnded() {
		return gameEnded;
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
		flipPlayersWithNoDamage();

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

	public boolean isTheWeaponConcluded(String playerName) {
		return getPlayerFromName(playerName).isTheWeaponConcluded();
	}

	public boolean isThePowerupConcluded(String playerName) {
		Player player = getPlayerFromName(playerName);
		return player.isThePowerupConcluded();
	}

	public void fillGameMap() {
		gameMap.refillMap();
		updateReps();
	}

	public void flipPlayersWithNoDamage() {
		gameBoard.getPlayers().forEach(Player::flipIfNoDamage);
		updateReps();
	}

	public void setPayed(boolean hasPayed){
		this.hasPayed = hasPayed;
	}

	public boolean hasCurrentPlayerPayed(){
		return hasPayed;
	}

	public void saveEvent(Event event){
		this.savedEvent = event;
	}

	public Event resumeAction(){
		return savedEvent;
	}

	// ####################################
	// PLAYERS MANAGEMENT METHODS
	// ####################################

	public List<AmmoType> getPriceOfTheChosenWeapon(int index){
		return ((WeaponCard) gameMap.getPlayerSquare(getCurrentPlayer()).getCards().get(index)).getGrabPrice();
	}

	public List<AmmoType> getPriceOfTheSelectedWeapon(int index) {
		return ((getCurrentPlayer().getPlayerBoard().getWeaponCards()).get(index)).getReloadPrice();
	}

	public void setAsDisconnected(String playerName) {
		Player player = getPlayerFromName(playerName);
		player.setConnected(false);
		long connectedPlayers = gameBoard.getPlayers().stream()
				.filter(Player::isConnected)
				.count();
		if(connectedPlayers < GameConstants.MIN_PLAYERS) {
			Utils.logInfo("Connected players less than minimum, ending the game...");
			endGameAndFindWinner();
		}
		updateReps();
	}

	public void setAsReconnected(String playerName) {
		Player player = getPlayerFromName(playerName);
		player.setConnected(true);
		updateReps();
	}

	public void cancelAction(String playerName) {
		Player player = getPlayerFromName(playerName);

		// Reset weapon.
		if(player.isShootingWeapon())
			player.handleWeaponEnd();

		// Reset powerup.
		if(player.isPowerupInExecution())
			handlePowerupEnd(playerName);

		// Finish actions.
		while(player.getDamageStatus().hasMacroActionLeft())
			player.getDamageStatus().decreaseMacroActionsToPerform();

		// TODO maybe powerup payments needs to be reset?
	}

	public void movePlayerTo(String playerName, Coordinates coordinates) {
		Player player = getPlayerFromName(playerName);
		gameMap.movePlayerTo(player, coordinates);
		updateReps();
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

	public void setTurnStatusOfCurrentPlayer(TurnStatus turnStatus) {
		Player player = getCurrentPlayer();
		gameBoard.setTurnStatus(player, turnStatus);
		updateReps();
	}

	public boolean currPlayerHasWeaponInventoryFull() {
		return getCurrentPlayer().getPlayerBoard().numOfWeapons() == GameConstants.MAX_WEAPON_CARDS_PER_PLAYER;
	}

	public List<Coordinates> getCoordinatesWherePlayerCanMove() {
		if (getCurrentPlayer().getDamageStatus().getCurrentMacroAction().isGrab()) {
			int numberOfMovements = gameBoard.getCurrentPlayer().getDamageStatus().getCurrentMacroAction().getNumOfMovements();
			return gameMap.getCoordinatesWhereCurrentPlayerCanGrab(getCurrentPlayer(), numberOfMovements);
		} else {
			return getReachableCoordinatesOfTheCurrentPlayer();
		}
	}

	public void pay(String playerName, List<AmmoType> price){
		pay(playerName, price, new ArrayList<>());
	}

	public void pay(String playerName, List<AmmoType> priceToPay, List<Integer> indexesOfPowerup){
		Player player = getPlayerFromName(playerName);
		PlayerBoard playerBoard = player.getPlayerBoard();
		List<AmmoType> price = new ArrayList<>(priceToPay);
		Utils.logInfo("Model -> pay(): " + playerName + " is paying the price of " + price + " with powerups " + indexesOfPowerup);
		while (!indexesOfPowerup.isEmpty()){
			Integer max = Collections.max(indexesOfPowerup);
			PowerupCard discardedPowerup = playerBoard.removePowerup(max);
			price.remove(discardedPowerup.getAssociatedAmmo());
			indexesOfPowerup.remove(max);
			gameBoard.getPowerupDeck().discardCard(discardedPowerup); // Put discarded powerup in the discard deck.
		}
		playerBoard.getAmmoContainer().removeAmmo(price);
		setPayed(true);
	}

	public boolean canUsePowerupToPay(String playerName, List<AmmoType> ammoToPay){
		List<PowerupCard> powerupCards = getPlayerFromName(playerName).getPlayerBoard().getPowerupCards();
		Utils.logInfo("Model() -> canUsePowerupToPay: " + playerName + " needs to pay " + ammoToPay + " and has " + powerupCards + " and " + getCurrentPlayer().getPlayerBoard().getAmmoContainer().getAmmo());
		for (PowerupCard powerupCard :powerupCards ) {
			if(ammoToPay.contains(powerupCard.getAssociatedAmmo()))
				return true;
		}
		Utils.logInfo("Model() -> canUsePowerupToPay: " + playerName + " has no powerup to pay with");
		return false;
	}

	public boolean canAffordWithOnlyAmmo(String playerName, List<AmmoType> ammoToPay){
		Player player = getPlayerFromName(playerName);
		return player.getPlayerBoard().getAmmoContainer().hasEnoughAmmo(ammoToPay);
	}

	public List<Coordinates> getReachableCoordinatesOfTheCurrentPlayer() {
		int numberOfMovements = gameBoard.getCurrentPlayer().getDamageStatus().getCurrentMacroAction().getNumOfMovements();
		return getReachableCoordinates(getCurrentPlayerName(), numberOfMovements);
	}

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
			throw new IllegalStateException("Should have at least one weapon");
		return indexes;
	}

	public MessageType getGrabMessageType() {
		return gameMap.getPlayerSquare(getCurrentPlayer()).getGrabMessageType();
	}

	public void grabWeaponCard(String playerName, int index) {
		Player player = getPlayerFromName(playerName);
		WeaponCard cardToGrab = (WeaponCard) gameMap.grabCard(gameMap.getPlayerCoordinates(player), index);
		cardToGrab.setOwner(player);
		cardToGrab.load();
		player.getPlayerBoard().addWeapon(cardToGrab);
		updateReps();
	}

	public void grabAmmoCard(String playerName, int index) {
		Player player = getPlayerFromName(playerName);
		AmmoCard cardToGrab = (AmmoCard) gameMap.grabCard(gameMap.getPlayerCoordinates(player), index);
		addAmmoCardToPlayer(player, cardToGrab);
		updateReps();
	}

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

	public void reloadWeapon(String playerName, int indexOfTheWeapon) {
		Player player = getPlayerFromName(playerName);
		player.reload(indexOfTheWeapon);
		updateReps();
	}

	public List<Coordinates> getReachableCoordinates(String playerName, int distance) {
		Player player = getPlayerFromName(playerName);
		return gameMap.reachableCoordinates(player, distance);
	}

	public void endGameAndFindWinner() {
		gameEnded = true;
		// TODO punti tracciato colpo mortale, in caso di parità chi ha preso più punti da colpi mortali. Se entrambi nessun colpo mortale allora parità.

		// Initialization.
		finalPlayersPosition = new ArrayList<>();
		for(Player player : gameBoard.getPlayers()) {
			player.updateRep();
		}

		// First position.
		PlayersPosition p1 = new PlayersPosition();
		p1.addInPosition((PlayerRep) gameBoard.getPlayers().get(0).getRep());
		finalPlayersPosition.add(p1);

		for(int i = 1; i < gameBoard.getPlayers().size(); i++) {
			Player currPlayer = gameBoard.getPlayers().get(i);
			int currPoints = currPlayer.getPlayerBoard().getPoints();

			// Check if curr must be put last.
			if(currPoints < finalPlayersPosition.get(finalPlayersPosition.size() - 1).getPlayerReps().get(0).getPoints()) {
				PlayersPosition newP = new PlayersPosition();
				newP.addInPosition((PlayerRep) currPlayer.getRep());
				finalPlayersPosition.add(newP);
			} else {
				for(int j = 0; j < finalPlayersPosition.size(); j++) {
					// If curr has same points.
					if(currPoints == finalPlayersPosition.get(j).getPlayerReps().get(0).getPoints()) {
						finalPlayersPosition.get(j).addInPosition((PlayerRep) currPlayer.getRep());
					}
					// If curr has more points.
					if(currPoints > finalPlayersPosition.get(j).getPlayerReps().get(0).getPoints()) {
						PlayersPosition newP = new PlayersPosition();
						newP.addInPosition((PlayerRep) currPlayer.getRep());
						finalPlayersPosition.add(j, newP);
					}
				}
			}
		}
	}

	public List<PlayersPosition> getFinalPlayersInfo() {
		return finalPlayersPosition;
	}


	// ####################################
	// MACRO ACTION METHODS
	// ####################################

	public boolean doesThePlayerHaveActionsLeft(String playerName) {
		return getPlayerFromName(playerName).getDamageStatus().hasMacroActionLeft();
	}

	public MacroAction getCurrentAction() {
		return gameBoard.getCurrentPlayer().getDamageStatus().getCurrentMacroAction();
	}

	public ActionType getNextActionToExecuteAndAdvance(String playerName) {
		Player player = getPlayerFromName(playerName);
		return player.getDamageStatus().getNextActionToExecuteAndAdvance();
	}

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

	public boolean canWeaponBeActivated(String playerName, int indexOfWeapon) {
		Player player = getPlayerFromName(playerName);
		List<WeaponCard> weaponCards = player.getPlayerBoard().getWeaponCards();

		if(indexOfWeapon >= weaponCards.size() || indexOfWeapon < 0)
			return false;

		return weaponCards.get(indexOfWeapon).canBeActivated();
	}

	/**
	 * Returns a list of weapons that can be activated in the current state.
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
	 * @param playerName the name of the player.
	 * @return true if the player has at least one loaded weapon.
	 */
	public boolean doesPlayerHaveLoadedWeapons(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<WeaponCard> weaponCards = player.getPlayerBoard().getWeaponCards();

		return weaponCards.stream().anyMatch(WeaponCard::isLoaded);
	}

	public boolean isShootingWeapon(String playerName){
		Player player = getPlayerFromName(playerName);
		return player.isShootingWeapon();
	}

	public QuestionContainer initialWeaponActivation(String playerName, int indexOfWeapon) {
		Player player = getPlayerFromName(playerName);
		QuestionContainer questionContainer = player.initialWeaponActivation(indexOfWeapon);
		updateReps();
		return questionContainer;
	}

	public QuestionContainer doWeaponStep(String playerName, int choice){
		Player player = getPlayerFromName(playerName);
		QuestionContainer questionContainer = player.doWeaponStep(choice);
		updateReps();
		return questionContainer;
	}

	public void handleWeaponEnd(String playerName){
		Player player = getPlayerFromName(playerName);
		saveShootedPlayersForShootPowerups(player);
		initiatePlayerWaitingForDamagePowerups(player);
		player.handleWeaponEnd();
		updateReps();
	}


	// ####################################
	// PUBLIC POWERUPS USE METHODS
	// ####################################

	public boolean canPowerupBeActivated(String playerName, int indexOfPowerup) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();

		if(indexOfPowerup >= powerupCards.size() || indexOfPowerup < 0)
			return false;

		PowerupCard powerupCard = powerupCards.get(indexOfPowerup);
		return powerupCard.canBeActivated();
	}

	public boolean doesPlayerHaveActivableOnShootPowerups(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();

		return powerupCards.stream()
				.anyMatch(powerupCard -> powerupCard.getUseCase() == PowerupCard.PowerupUseCaseType.ON_SHOOT && powerupCard.canBeActivated());
	}

	public List<Integer> getActivableOnShootPowerups(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();
		List<Integer> activablePowerups = new ArrayList<>();

		for (int i = 0; i < powerupCards.size(); i++) {
			if(powerupCards.get(i).getUseCase() == PowerupCard.PowerupUseCaseType.ON_SHOOT && powerupCards.get(i).canBeActivated()){
				activablePowerups.add(i);
			}
		}

		return activablePowerups;
	}

	public String getNextPlayerWaitingForDamagePowerups() {
		return playersWaitingForDamagePowerups.poll();
	}

	public boolean isPlayerWaitingForDamagePowerupsEmpty() {
		return playersWaitingForDamagePowerups == null || playersWaitingForDamagePowerups.isEmpty();
	}

	public List<Integer> getActivableOnDamagePowerups(String damagedPlayerName, String shootingPlayerName) {
		Player damagedPlayer = getPlayerFromName(damagedPlayerName);
		Player shootingPlayer = getPlayerFromName(shootingPlayerName);
		List<PowerupCard> powerupCards = damagedPlayer.getPlayerBoard().getPowerupCards();
		List<Integer> activablePowerups = new ArrayList<>();
		Utils.logInfo("Model -> getActivableOnDamagePowerups(): checking if " + shootingPlayerName + " has activable powerups");

		for (int i = 0; i < powerupCards.size(); i++) {
			if(powerupCards.get(i).getUseCase() == PowerupCard.PowerupUseCaseType.ON_DAMAGE) {
				Utils.logInfo("Model -> getActivableOnDamagePowerups(): checking powerup "+powerupCards.get(i));
				powerupCards.get(i).setShootingPlayer(shootingPlayer);
				if(powerupCards.get(i).canBeActivated()){
					Utils.logInfo("Model -> getActivableOnDamagePowerups(): added "+powerupCards.get(i));
					activablePowerups.add(i);
				}
				else
					powerupCards.get(i).setShootingPlayer(null);
			}
		}

		return activablePowerups;
	}

	public List<Integer> getActivableOnTurnPowerups(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();
		List<Integer> activablePowerups = new ArrayList<>();

		for (int i = 0; i < powerupCards.size(); i++) {
			if(powerupCards.get(i).getUseCase() == PowerupCard.PowerupUseCaseType.ON_TURN && powerupCards.get(i).canBeActivated()){
				activablePowerups.add(i);
			}
		}

		return activablePowerups;
	}

	public boolean doesPlayerHaveActivableOnTurnPowerups(String playerName) {
		Player player = getPlayerFromName(playerName);
		List<PowerupCard> powerupCards = player.getPlayerBoard().getPowerupCards();

		return powerupCards.stream()
				.anyMatch(powerupCard -> powerupCard.getUseCase() == PowerupCard.PowerupUseCaseType.ON_TURN && powerupCard.canBeActivated());
	}

	public boolean isPowerupInExecution(String playerName) {
		Player player = getPlayerFromName(playerName);
		return player.isPowerupInExecution();
	}

	public QuestionContainer initialPowerupActivation(String playerName, int indexOfPowerup) {
		Player player = getPlayerFromName(playerName);
		QuestionContainer questionContainer = player.initialPowerupActivation(indexOfPowerup);
		updateReps();
		return questionContainer;
	}

	public QuestionContainer doPowerupStep(String playerName, int choice) {
		Player player = getPlayerFromName(playerName);
		QuestionContainer questionContainer = player.doPowerupStep(choice);
		updateReps();
		return questionContainer;
	}

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
		for(PowerupCard powerupCard : shootingPlayer.getPlayerBoard().getPowerupCards()) {
			if(powerupCard.getUseCase() == PowerupCard.PowerupUseCaseType.ON_SHOOT)
				powerupCard.setShootedPlayers(playersHit);
		}
	}

	private void initiatePlayerWaitingForDamagePowerups(Player shootingPlayer) {
		List<Player> playersHit = shootingPlayer.getPlayersHitWithWeapon();
		playersWaitingForDamagePowerups = new ArrayDeque<>();
		for(Player damagedPlayer : playersHit) {
			if(doesPlayerHaveActivableOnDamagePowerups(damagedPlayer.getPlayerName(), shootingPlayer.getPlayerName()))
				playersWaitingForDamagePowerups.add(damagedPlayer.getPlayerName());
		}
	}

	private boolean doesPlayerHaveActivableOnDamagePowerups(String damagedPlayerName, String shootingPlayerName) {
		return !getActivableOnDamagePowerups(damagedPlayerName, shootingPlayerName).isEmpty();
	}

	private Player getCurrentPlayer() {
		return gameBoard.getCurrentPlayer();
	}

	private void addAmmoCardToPlayer(Player player, AmmoCard ammoCard){
		for (AmmoType ammo : ammoCard.getAmmo() ) {
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

	// ####################################
	// METHODS ONLY FOR TESTS
 	// ####################################

	protected GameBoard getGameBoard() {
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