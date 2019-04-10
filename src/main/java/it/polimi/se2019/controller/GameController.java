package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
import java.util.List;

public class GameController {

	private static final Integer scores[] ={8,6,4,2,1,1};
	private TurnController turnController;
	private Model model;
	private PlayerQueue<Player> playerQueue;


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
			startTurn((Player)playerQueue.getFirst());
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
		playerQueue.moveToBottom();
	}

}