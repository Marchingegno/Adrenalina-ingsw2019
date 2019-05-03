package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.TurnStatus;
import it.polimi.se2019.model.player.damagestatus.*;
import it.polimi.se2019.network.message.ActionMessage;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.VirtualView;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2019.utils.GameConstants.HIGH_DAMAGE_THRESHOLD;
import static it.polimi.se2019.utils.GameConstants.MEDIUM_DAMAGE_THRESHOLD;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * This class is in a lower level than Controller. It handles the logic relative to the game.
 * @author Marchingegno
 */
public class GameController {

	private TurnController turnController;
	private Model model;


	public GameController(Model model) {
		this.model = model;
		turnController = new TurnController(model);
		Utils.logInfo("Create GameController");
	}


	public void startGame() {
		Utils.logInfo("GameController: startGame");
		nextPlayerTurn();
	}

	public void initialRounds() {
	}




	private void flipPlayers(){
		model.getPlayerQueue().getAsArray().stream()
				.forEach(Player::flipIfNoDamage);

	}

	//TODO: Revisit the location of the first player. I don't know if he is the player that just ended the turn, or the following.
	private void startFrenzy() {
		Player firstPlayer = model.getPlayers().get(0);
		ArrayList<Player> sortedPlayers = model.getPlayerQueue().getAsArray();
		int i = 0;

		while(sortedPlayers.get(i) != firstPlayer){
			sortedPlayers.get(i).setDamageStatus(new FrenzyBefore());
			i++;
		}

		while(i < sortedPlayers.size()){
			sortedPlayers.get(i).setDamageStatus(new FrenzyAfter());
			i++;
		}

		flipPlayers();
	}

	public void refillCardsOnMap() {
	}

	public void spawnPlayer(Player player, int indexOfPowerup) {
	}

	private void setCorrectDamageStatus(Player player){
		List<Player> damageBoard = player.getPlayerBoard().getDamageBoard();

		//If the game is in frenzy mode, then the player already has the right damageStatus.
		if (model.isFrenzyStarted())
			player.getDamageStatus().refillActions();

		else if(damageBoard.size() < MEDIUM_DAMAGE_THRESHOLD)
			player.setDamageStatus(new LowDamage());

		else if(damageBoard.size() < HIGH_DAMAGE_THRESHOLD)
			player.setDamageStatus(new MediumDamage());

		else
			player.setDamageStatus(new HighDamage());
	}


	/**
	 * This method ends the turn of a player, scores dead players, starts frenzy if it needs to be started
	 * and moves to the bottom the first player of playerQueue.
	 */
	private void endTurn() {
		model.getCurrentPlayer().setTurnStatus(TurnStatus.IDLE);
		model.scoreDeadPlayers();
		refillCardsOnMap();
		nextPlayerTurn();
		//This checks if frenzy is to start.
		if(model.areSkullsFinished() && !model.isFrenzyStarted()) {
			model.startFrenzy();
			startFrenzy();
		}

		//This checks if the frenzy is started and flips the DamageBoard of the players.
		if(model.isFrenzyStarted()){
			flipPlayers();
		}


	}


	private void nextPlayerTurn() {
		//Move the next player at the top of the queue.
		model.nextPlayerTurn();
		//Set its correct DamageStatus.
		setCorrectDamageStatus(model.getCurrentPlayer());
		//Set its TurnStatus.
		model.getCurrentPlayer().setTurnStatus(TurnStatus.YOUR_TURN);
	}


	void processMessage(Message message){
		MessageType messageType = message.getMessageType();

		//TODO: Add other cases.
		switch (messageType) {
			case END_TURN:
				VirtualView virtualView = ((ActionMessage)message).getVirtualView();
				Player player = model.getPlayerFromName(virtualView.getPlayerName());
				if(player.getTurnStatus() != TurnStatus.YOUR_TURN){
					throw new RuntimeException("It's not your turn!");
				}
				endTurn();
				break;
			default: turnController.processMessage(message);
		}

	}
}