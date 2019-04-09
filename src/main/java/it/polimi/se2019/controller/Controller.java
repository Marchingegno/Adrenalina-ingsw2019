package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;

/**
 * This class is the top level controller. Its jobs are to delegate the model initialization and to start the game.
 * @author Marchingegno
 */
public class Controller {
	private static final int MIN_SKULLS = 5;
	private static final int MAX_SKULLS = 8;
	private static final int MIN_PLAYERS = 3;
	private static final int MAX_PLAYERS = 5;

	private GameController gameController;
	private Model model;


	public Controller(ArrayList<Player> playerQueue) {
		GameController gameController = new GameController(model, playerQueue);
		this.model = new Model();
	}


	public void initializeModel(String mapPath, ArrayList<String> playerNames, int skulls) {
		if(skulls < MIN_SKULLS || skulls > MAX_SKULLS)
			throw new IllegalArgumentException("Invalid number of skulls!");
		if(playerNames.size() > MAX_PLAYERS || playerNames.size() < MIN_PLAYERS)
			throw new IllegalArgumentException("Invalid number of players!");

		this.model.initialize(mapPath, playerNames, skulls);
	}

	public void startGame() {
		gameController.startGame();
	}

}