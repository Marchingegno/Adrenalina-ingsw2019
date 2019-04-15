package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2019.utils.GameConstants.*;

/**
 * This class is the top level controller. Its jobs are to delegate the model initialization and to start the game.
 * @author Marchingegno
 */
public class Controller {

	private GameController gameController;
	private Model model;


	public Controller(List<String> playerNames, int skulls, String mapPath) {
		if(skulls < MIN_SKULLS || skulls > MAX_SKULLS)
			throw new IllegalArgumentException("Invalid number of skulls!");
		if(playerNames.size() > MAX_PLAYERS || playerNames.size() < MIN_PLAYERS)
			throw new IllegalArgumentException("Invalid number of players!");


		this.model = new Model(mapPath, playerNames, skulls);
		GameController gameController = new GameController(model);
	}


	@Deprecated
	private void initializeModel(String mapPath, ArrayList<String> playerNames, int skulls) {
		if(skulls < MIN_SKULLS || skulls > MAX_SKULLS)
			throw new IllegalArgumentException("Invalid number of skulls!");
		if(playerNames.size() > MAX_PLAYERS || playerNames.size() < MIN_PLAYERS)
			throw new IllegalArgumentException("Invalid number of players!");

		this.model = new Model (mapPath, playerNames, skulls);
	}


	public void startGame() {
		gameController.startGame();
	}

}