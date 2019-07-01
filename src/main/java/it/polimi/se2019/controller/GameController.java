package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.network.message.IntMessage;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;

import java.util.Optional;

/**
 * This class is in a lower level than Controller. It handles the logic relative to the game advancement, spawning,
 * starting turns, starting frenzy and connection errors.
 * @author Marchingegno
 */
public class GameController {

	private VirtualViewsContainer virtualViewsContainer;
	private TurnController turnController;
	private Model model;

	/**
	 * Constructor of the class.
	 *
	 * @param model                 the model of this game.
	 * @param virtualViewsContainer the VirtualViewsContainer of this game's players.
	 */
	public GameController(Model model, VirtualViewsContainer virtualViewsContainer) {
		this.virtualViewsContainer = virtualViewsContainer;
		this.model = model;
		turnController = new TurnController(model, virtualViewsContainer);
	}

	/**
	 * Start the turn and send the updated reps.
	 */
	void startGame() {
		Utils.logInfo("GameController -> startGame(): Game has started");
		startTurn();
		virtualViewsContainer.sendUpdatedReps(); // Send updated reps to other clients.
	}

	/**
	 * Refills the map with AmmoCards and WeaponCards.
	 */
	private void refillCardsOnMap() {
		model.fillGameMap();
	}

	/**
	 * Handles the beginning of a new turn. Starts frenzy if it needs to be started
	 * and calls the method to begin the next turn.
	 */
	private void handleTurnBeginning() {
		refillCardsOnMap();
		//This checks if frenzy is to start.
		if(model.areSkullsFinished() && !model.isFrenzyStarted()) {
			model.startFrenzy();
		}
		//This checks if the frenzy is started and flips the DamageBoard of the players.
		if(model.isFrenzyStarted()){
			model.flipPlayersWithNoDamage();
		}

		//Move the next player at the top of the queue.
		model.nextPlayerTurn();

		//Checks if the game can continue, if the game is in frenzy mode.
		//If it can't, end the game.
		if (model.isFrenzyStarted() && !model.canGameContinue()) {
			model.endGameAndFindWinner();
			virtualViewsContainer.sendEndGameMessage(model.getFinalPlayersInfo());
		}

		//start its turn.
		startTurn();
	}

	/**
	 * Start the turn.
	 */
	private void startTurn(){
		String currentPlayerName = model.getCurrentPlayerName();

		// Check if the player is to be spawned.
		// Which should be only in its beginning turns.
		if(model.getTurnStatus(currentPlayerName) == TurnStatus.PRE_SPAWN) {
			askToSpawn(currentPlayerName);
		} else {
			model.setCorrectDamageStatus(currentPlayerName);
			model.setTurnStatusOfCurrentPlayer(TurnStatus.YOUR_TURN);
			virtualViewsContainer.getVirtualViewFromPlayerName(currentPlayerName).askAction(model.doesPlayerHaveActivableOnTurnPowerups(currentPlayerName), model.doesPlayerHaveLoadedWeapons(currentPlayerName));
		}
	}

	/**
	 * End the turn of the current player, scores dead players and spawns dead players, then begin the next turn.
	 */
	private void handleEndTurn() {
		model.setTurnStatusOfCurrentPlayer(TurnStatus.IDLE);
		model.scoreDeadPlayers();
		spawnNextDeadPlayerOrBeginTurn();
	}

	/**
	 * Check if there are dead players to be spawned and spawn them, otherwise start next turn.
	 */
	private void spawnNextDeadPlayerOrBeginTurn() {
		Optional<VirtualView> virtualViewOfPlayerToSpawn = virtualViewsContainer.getVirtualViews().stream()
				.filter(virtualView -> model.getTurnStatus(virtualView.getNickname()) == TurnStatus.DEAD)
				.findFirst();

		if (virtualViewOfPlayerToSpawn.isPresent()) {
			askToSpawn(virtualViewOfPlayerToSpawn.get().getNickname());
		} else {
			handleTurnBeginning();
		}
	}

	/**
	 * Make the player draw a card and ask which card to use to spawn.
	 * @param playerName the name of the player to be spawned.
	 */
	private void askToSpawn(String playerName) {
        model.addSpawnPowerupCardTo(playerName);
		VirtualView virtualView = virtualViewsContainer.getVirtualViewFromPlayerName(playerName);
		virtualView.askSpawn();
	}

	/**
	 * Handle a connection error from a player.
	 * @param playerName the name of the player that has a connection error.
	 */
	private void handleConnectionError(String playerName) {
		if(model.getTurnStatus(playerName) == TurnStatus.YOUR_TURN || model.getTurnStatus(playerName) == TurnStatus.PRE_SPAWN) {
			Utils.logInfo("Received a CONNECTION ERROR from the player doing its turn.");
			model.cancelAction(playerName);
			model.setAsDisconnected(playerName);
			if(model.isGameEnded())
				virtualViewsContainer.sendEndGameMessage(model.getFinalPlayersInfo());
			else
				handleEndTurn();
		} else {
			Utils.logInfo("Received a CONNECTION ERROR from a player waiting for its turn.");
			model.setAsDisconnected(playerName);
			if(model.isGameEnded()) {
				model.cancelAction(model.getCurrentPlayerName());
				virtualViewsContainer.sendEndGameMessage(model.getFinalPlayersInfo());
			} else {
				handleEndTurn();
			}
		}
	}

	/**
	 * Process an event observed by the Controller.
	 * @param event the event observed.
	 */
	void processEvent(Event event){
		VirtualView virtualView = event.getVirtualView();
		String playerName = virtualView.getNickname();
		MessageSubtype messageSubtype = event.getMessage().getMessageSubtype();

		Utils.logInfo("GameController -> processEvent(): processing an event received from \"" + playerName + "\" with a message of type " + event.getMessage().getMessageType() + " and subtype " + messageSubtype + ".");

		switch (event.getMessage().getMessageType()) {
			case END_TURN:
				if(model.getTurnStatus(playerName) == TurnStatus.YOUR_TURN)
					handleEndTurn();
				else
					Utils.logError("It's not the turn of " + playerName + "!", new IllegalStateException());
				break;
			case SPAWN:
				//TODO player should be spawned even if the current player turnStatus is prespawn.
				int spawnAnswer = ((IntMessage) event.getMessage()).getContent();
				//If the player was dead, spawn him and check if there are other dead players.
				if (model.getTurnStatus(playerName) == TurnStatus.DEAD){
					model.spawnPlayer(playerName, spawnAnswer);
					spawnNextDeadPlayerOrBeginTurn();
				}
				//If the player was in PRE_SPAWN, spawn him and begin its turn.
				else if (model.getTurnStatus(playerName) == TurnStatus.PRE_SPAWN) {
					model.spawnPlayer(playerName, spawnAnswer);
					startTurn();
				}
				break;
			case CONNECTION:
				if(messageSubtype == MessageSubtype.ERROR)
					handleConnectionError(playerName);
				else if(messageSubtype == MessageSubtype.INFO)
					model.setAsReconnected(playerName);
				break;
			default: turnController.processEvent(event);
				break;
		}
		virtualViewsContainer.sendUpdatedReps(); // Send updated reps to other clients.
	}
}