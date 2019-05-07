package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.model.player.damagestatus.*;
import it.polimi.se2019.network.message.*;
import it.polimi.se2019.utils.Utils;
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
		Utils.logInfo("Create GameController");
	}


	void startGame() {
		Utils.logInfo("GameController: startGame");
		//Set its correct DamageStatus.
		model.setCorrectDamageStatus(model.getCurrentPlayer());
		//Set its TurnStatus.
		model.setTurnStatus(model.getCurrentPlayer(), TurnStatus.YOUR_TURN);
		Controller.getVirtualViewFromPlayer(model.getCurrentPlayer(), virtualViews).askAction();
	}

	private void flipPlayers(){
		model.getPlayers().stream()
				.forEach(Player::flipIfNoDamage);

	}

	//TODO: Revisit the location of the first player. I don't know if he is the player that just ended the turn, or the following.
	private void startFrenzy() {
		Player firstPlayer = model.getPlayers().get(0);

		boolean isAfterFirstPlayer = false;
		for (Player player : model.getPlayerQueue()) {
			//The first must also receive the Frenzy After damage status
			if (player.getPlayerName().equals(firstPlayer.getPlayerName()))
				isAfterFirstPlayer = true;

			player.setDamageStatus(isAfterFirstPlayer ? new FrenzyAfter() : new FrenzyBefore());
		}
		flipPlayers();
	}

	private void refillCardsOnMap() {
		model.fillGameMap();
	}

	private void spawnPlayers(){
		for (Player item : model.getPlayers()) {
			if (item.getTurnStatus() == TurnStatus.DEAD) {
				spawnPlayer(item);
			}
		}
	}

	private void spawnPlayer(Player player) {
		Controller.getVirtualViewFromPlayer(player, virtualViews).askSpawn();
	}


	/**
	 * This method ends the turn of a player, scores dead players, starts frenzy if it needs to be started
	 * and moves to the bottom the first player of playerQueue.
	 */
	private void endTurn() {
		model.setTurnStatus(model.getCurrentPlayer(), TurnStatus.IDLE);
		model.scoreDeadPlayers();
		spawnPlayers();
		refillCardsOnMap();
		//This checks if frenzy is to start.
		if(model.areSkullsFinished() && !model.isFrenzyStarted()) {
			model.startFrenzy();
			startFrenzy();
		}
		nextPlayerTurn();

		//This checks if the frenzy is started and flips the DamageBoard of the players.
		if(model.isFrenzyStarted()){
			flipPlayers();
		}


	}


	private void nextPlayerTurn() {
		//Move the next player at the top of the queue.
		model.nextPlayerTurn();
		//Set its correct DamageStatus.
		model.setCorrectDamageStatus(model.getCurrentPlayer());
		model.setTurnStatus(model.getCurrentPlayer(), TurnStatus.YOUR_TURN);
	}


	void processMessage(Message message){
		MessageType messageType = message.getMessageType();
		VirtualView virtualView = ((ActionMessage)message).getVirtualView();
		Player player = model.getPlayerFromName(virtualView.getPlayerName());

		//TODO: Add other cases.
		switch (messageType) {
			case END_TURN:
				if(player.getTurnStatus() != TurnStatus.YOUR_TURN){
					throw new RuntimeException("It's not your turn!");
				}
				endTurn();
				break;
			case SPAWN:
				//If the player requests to spawn, make it draw a powerup card and request the choice.
				if(message.getMessageSubtype() == MessageSubtype.REQUEST){
					model.addPowerupCardTo(player);
					virtualView.askSpawn();
				}
				else if(message.getMessageSubtype() == MessageSubtype.ANSWER){
					model.spawnPlayer(player, ((DefaultActionMessage) message).getIndex());
				}
				break;
			default: turnController.processMessage(message);
		}

	}
}