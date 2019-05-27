package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.network.message.IntMessage;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;

/**
 * This class is in a lower level than Controller. It handles the logic relative to the game.
 * @author Marchingegno
 */
public class GameController {

	private VirtualViewsContainer virtualViewsContainer;
	private TurnController turnController;
	private Model model;


	public GameController(Model model, VirtualViewsContainer virtualViewsContainer) {
		this.virtualViewsContainer = virtualViewsContainer;
		this.model = model;
		turnController = new TurnController(model, virtualViewsContainer);
	}


	void startGame() {
		startTurn();
		virtualViewsContainer.sendUpdatedReps(); // Send updated reps to other clients.
		Utils.logInfo("GameController -> startGame(): Game has started");
	}

	private void refillCardsOnMap() {
		model.fillGameMap();
	}


	private void spawnPlayers(){
		//Let's test this method.
		virtualViewsContainer.getVirtualViews().parallelStream()
				.filter(virtualView -> model.getTurnStatus(virtualView.getNickname()) == TurnStatus.DEAD)
				.forEach(virtualView -> askToSpawn(virtualView.getNickname()));
//
		//This method doesn't work.
//		for (VirtualView virtualView : virtualViewsContainer.getVirtualViews()) {
//			if (model.getTurnStatus(virtualView.getNickname()) == TurnStatus.DEAD) {
//				askToSpawn(virtualView.getNickname());
//			}
//		}
	}


	/**
	 * This method ends the turn of a player, scores dead players, starts frenzy if it needs to be started
	 * and moves to the bottom the first player of playerQueue.
	 */
	private void endTurn() {
		model.setTurnStatusOfCurrentPlayer(TurnStatus.IDLE);
		model.scoreDeadPlayers();
		spawnPlayers();
		refillCardsOnMap();
		//This checks if frenzy is to start.
		if(model.areSkullsFinished() && !model.isFrenzyStarted()) {
			model.startFrenzy();
		}
		//This checks if the frenzy is started and flips the DamageBoard of the players.
		if(model.isFrenzyStarted()){
			model.flipPlayersWithNoDamage();
		}

		nextPlayerTurn();
	}

	private void startTurn(){
		String currentPlayerName = model.getCurrentPlayerName();

		model.setCorrectDamageStatus(currentPlayerName);

		// Check if the player is to be spawned.
		// Which should be only in its beginning turns.
		if(model.getTurnStatus(currentPlayerName) == TurnStatus.PRE_SPAWN) {
			askToSpawn(currentPlayerName);
		} else {
			model.setTurnStatusOfCurrentPlayer(TurnStatus.YOUR_TURN);
			virtualViewsContainer.getVirtualViewFromPlayerName(currentPlayerName).askAction(model.doesPlayerHaveActivableOnTurnPowerups(currentPlayerName), model.doesPlayerHaveLoadedWeapons(currentPlayerName));
		}
	}


	private void nextPlayerTurn() {
		//Move the next player at the top of the queue.
		model.nextPlayerTurn();
		//start its turn.
		startTurn();
	}

	/**
	 * Make the player draw a card and ask which card to use to spawn.
	 * @param playerName the name of the player to be spawned.
	 */
	private void askToSpawn(String playerName) {
		model.addPowerupCardTo(playerName);
		VirtualView virtualView = virtualViewsContainer.getVirtualViewFromPlayerName(playerName);
		virtualView.askSpawn();
	}


	void processEvent(Event event){
		VirtualView virtualView = event.getVirtualView();
		String playerName = virtualView.getNickname();
		MessageSubtype messageSubtype = event.getMessage().getMessageSubtype();

		Utils.logInfo("GameController -> processEvent(): processing an event received from \"" + playerName + "\" with a message of type " + event.getMessage().getMessageType() + " and subtype " + messageSubtype + ".");

		switch (event.getMessage().getMessageType()) {
			case END_TURN:
				if(model.getTurnStatus(playerName) == TurnStatus.YOUR_TURN)
					endTurn();
				else
					Utils.logError("It's not the turn of " + playerName + "!", new IllegalStateException());
				break;
			case SPAWN:
				if(messageSubtype == MessageSubtype.REQUEST){
					// If the player requests to spawn, make it draw a powerup card and request the choice.
					askToSpawn(playerName);
				} else if(messageSubtype == MessageSubtype.ANSWER) {
					// Process the answer of a spawn request.
					int spawnAnswer = ((IntMessage) event.getMessage()).getContent();
					model.spawnPlayer(playerName, spawnAnswer);
					virtualView.askAction(model.doesPlayerHaveActivableOnTurnPowerups(playerName), model.doesPlayerHaveLoadedWeapons(playerName));
				}
				break;
			default: turnController.processEvent(event);
				break;
		}
		virtualViewsContainer.sendUpdatedReps(); // Send updated reps to other clients.
	}
}