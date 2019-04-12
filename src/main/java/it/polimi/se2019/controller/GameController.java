package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import java.util.List;

/**
 * This class is in a lower level than Controller. It handles the logic relative to the game.
 * @author Marchingegno
 */
public class GameController {

	private TurnController turnController;
	private Model model;
	private PlayerQueue playerQueue;


	public GameController(Model model, List<Player> playerList) {
		this.model = model;
		this.playerQueue = new PlayerQueue(playerList);
		turnController = new TurnController(model);
	}


	public void startGame() {
		gameLogic();
	}

	public void initialRounds() {
	}

	public void finalRounds() {
	}

	public void gameLogic() {
		while(!model.areSkullsFinished()){
			startTurn(playerQueue.getFirst());
			endTurn();
		}


	}

	public void startFrenzy() {
	}

	public void refillCardsOnMap() {
	}

	public void spawnPlayer(Player player, int idexOfPowerup) {
	}

	public void startTurn(Player player) {
		turnController.handleTurn(player);
	}

	/**
	 * This method scores dead players, starts frenzy if it needs to be started
	 * and moves to the bottom the first player of playerQueue.
	 */
	public void endTurn() {
		model.scoreDeadPlayers();

		if(model.areSkullsFinished())
			startFrenzy();
		playerQueue.moveFirstToLast();
	}

}