package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.model.player.damagestatus.FrenzyAfter;
import it.polimi.se2019.model.player.damagestatus.FrenzyBefore;
import it.polimi.se2019.network.message.DefaultActionMessage;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;

import java.util.List;

/**
 * This class is in a lower level than Controller. It handles the logic relative to the game.
 * @author Marchingegno
 */
public class GameController {

	private TurnController turnController;
	private Model model;
	private List<VirtualView> virtualViews;


	public GameController(Model model, List<VirtualView> virtualViews) {
		this.virtualViews = virtualViews;
		this.model = model;
		turnController = new TurnController(model, virtualViews);
	}


	void startGame() {
		Utils.logInfo("GameController: startGame");
		startTurn();
	}

	private void refillCardsOnMap() {
		model.fillGameMap();
	}


	private void spawnPlayers(){
		for (VirtualView virtualView : virtualViews) {
			if (model.getTurnStatus(virtualView.getPlayerName()) == TurnStatus.DEAD) {
				askToSpawn(virtualView.getPlayerName());
			}
		}
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
			model.flipPlayers();
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
			Controller.sendUpdatedReps(virtualViews); // Send updated reps to other clients.
		} else {
			model.setTurnStatusOfCurrentPlayer(TurnStatus.YOUR_TURN);
			Controller.getVirtualViewFromPlayerName(currentPlayerName, virtualViews).askAction();
			Controller.sendUpdatedReps(virtualViews); // Send updated reps to other clients.
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
		VirtualView virtualView = Controller.getVirtualViewFromPlayerName(playerName, virtualViews);
		virtualView.askSpawn();
	}


	void processEvent(Event event){
		MessageSubtype messageSubtype = event.getMessage().getMessageSubtype();
		VirtualView virtualView = event.getVirtualView();
		String playerName = virtualView.getPlayerName();

		Utils.logInfo("GameController -> processing an event: "+ event.toString());
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
					DefaultActionMessage defaultActionMessage = (DefaultActionMessage) event.getMessage();
					model.spawnPlayer(playerName, defaultActionMessage.getContent());
					virtualView.askAction();
					Controller.sendUpdatedReps(virtualViews); // Send updated reps to other clients.
				}
				break;
			default: turnController.processEvent(event);
				break;
		}

	}
}