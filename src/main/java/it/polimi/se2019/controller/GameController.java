package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.model.player.PlayerQueue;
import it.polimi.se2019.model.player.damagestatus.FrenzyAfter;
import it.polimi.se2019.model.player.damagestatus.FrenzyBefore;

import java.util.ArrayList;
import java.util.List;

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
	}


	public void startGame() {
		gameLogic();
	}

	public void initialRounds() {
	}

	public void finalRounds() {
	}

	private void gameLogic() {
		while(!model.areSkullsFinished()){
			startTurn(model.getCurrentPlayer());
			endTurn();
		}


	}

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

		sortedPlayers.stream().forEach(Player::flipIfNoDamage);
	}

	public void refillCardsOnMap() {
	}

	public void spawnPlayer(Player player, int indexOfPowerup) {
	}

	private void startTurn(Player player) {
		turnController.handleTurn(player);
	}

	/**
	 * This method scores dead players, starts frenzy if it needs to be started
	 * and moves to the bottom the first player of playerQueue.
	 */
	private void endTurn() {
		model.scoreDeadPlayers();
		refillCardsOnMap();
		model.nextPlayerTurn();
		if(model.areSkullsFinished())
			startFrenzy();
	}

}