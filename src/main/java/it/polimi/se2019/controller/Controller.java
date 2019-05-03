package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static it.polimi.se2019.utils.GameConstants.*;

/**
 * This class is the top level controller. Its jobs are to delegate the model initialization and to start the game.
 * @author Marchingegno
 */
public class Controller implements Observer {

	private GameController gameController;
	private Model model;


	public Controller(List<String> playerNames, int skulls, String mapPath) {
		if(skulls < MIN_SKULLS || skulls > MAX_SKULLS)
			throw new IllegalArgumentException("Invalid number of skulls!");
		if(playerNames.size() > MAX_PLAYERS || playerNames.size() < MIN_PLAYERS)
			throw new IllegalArgumentException("Invalid number of players!");


		this.model = new Model(mapPath, playerNames, skulls);
		gameController = new GameController(model);
		Utils.logInfo("Created controller.");
	}

	public Model getModel(){
		return model;
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
		Utils.logInfo("Controller: startGame");
		gameController.startGame();
	}


	/**
	 * This method is called whenever the observed object is changed. An
	 * application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param o   the observable object.
	 * @param arg an argument passed to the <code>notifyObservers</code>
	 */
	@Override
	public void update(Observable o, Object arg) {
		gameController.processMessage( (Message) arg);
	}
}