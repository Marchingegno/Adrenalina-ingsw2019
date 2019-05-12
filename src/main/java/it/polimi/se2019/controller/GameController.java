package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.model.player.damagestatus.*;
import it.polimi.se2019.network.message.*;
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
		Utils.logInfo("Create GameController");
	}


	void startGame() {
		Utils.logInfo("GameController: startGame");
		startTurn();
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
				askToSpawn(item);
			}
		}
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
		//This checks if the frenzy is started and flips the DamageBoard of the players.
		if(model.isFrenzyStarted()){
			flipPlayers();
		}

		nextPlayerTurn();//
	}

	private void startTurn(){
		Player player = model.getCurrentPlayer();

		//Check if the player is to be spawned.
		//Which should be only in its beginning turns.
		if(player.getTurnStatus() == TurnStatus.PRE_SPAWN){
			model.setCorrectDamageStatus(player);
			model.setTurnStatus(player, TurnStatus.YOUR_TURN);
			askToSpawn(player);
			return;
		}

		model.setCorrectDamageStatus(player);
		model.setTurnStatus(player, TurnStatus.YOUR_TURN);
		Controller.getVirtualViewFromPlayer(player, virtualViews).askAction();

	}


	private void nextPlayerTurn() {
		//Move the next player at the top of the queue.
		model.nextPlayerTurn();
		//start its turn.
		startTurn();
	}

	/**
	 * Make the player draw a card and ask which card to use to spawn.
	 * @param player the player to be spawned.
	 * @param virtualView the virtual view of the player.
	 */
	private void askToSpawn(Player player, VirtualView virtualView){
		model.addPowerupCardTo(player);
		virtualView.askSpawn();
	}

	/**
	 * Calls askToSpawn with the view.
	 * @param player the player to be spawned.
	 */
	private void askToSpawn(Player player) {
		askToSpawn(player, Controller.getVirtualViewFromPlayer(player, virtualViews));
	}


	void processEvent(Event event){
		MessageType messageType = event.getMessage().getMessageType();
		MessageSubtype messageSubtype = event.getMessage().getMessageSubtype();
		VirtualView virtualView = event.getVirtualView();
		Player player = model.getPlayerFromName(virtualView.getPlayerName());

		Utils.logInfo("GameController: processing an event: "+ event.toString());
		//TODO: Add other cases.
		switch (messageType) {
			case END_TURN:
				if(player.getTurnStatus() != TurnStatus.YOUR_TURN){
					Utils.logWarning("Not the turn of " + player.getPlayerName() + ".");
				}
				endTurn();
				break;
			case SPAWN:
				//If the player requests to spawn, make it draw a powerup card and request the choice.
				if(messageSubtype == MessageSubtype.REQUEST){
					askToSpawn(player, virtualView);
				}
				else if(messageSubtype == MessageSubtype.ANSWER){
					DefaultActionMessage defaultActionMessage = (DefaultActionMessage) event.getMessage();

					try {
						model.spawnPlayer(player, defaultActionMessage.getContent());
					} catch (Exception e){
						Utils.logError("Error spawning player", e);
					}
					virtualView.askAction();
				}
				break;
			default: turnController.processEvent(event);
				break;
		}

	}
}