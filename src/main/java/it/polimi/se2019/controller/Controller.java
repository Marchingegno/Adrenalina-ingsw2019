package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;

import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

/**
 * This class is the top level controller.
 * Its jobs are to create the model, to set observers and to start the game.
 *
 * @author Marchingegno
 */
public class Controller implements Observer {

	private VirtualViewsContainer virtualViewsContainer;
	private GameController gameController;
	private Model model;


	/**
	 * Constructor of the class.
	 *
	 * @param mapType      the type of the map, chosen from one of the values in GameConstants.
	 * @param virtualViews the VirtualViews of the players.
	 * @param skulls       the amount of skulls that the player chose to play with.
	 */
	public Controller(GameConstants.MapType mapType, Collection<VirtualView> virtualViews, int skulls) {
		this.virtualViewsContainer = new VirtualViewsContainer(virtualViews);

		// Create a list of player names.
		List<String> playerNames = virtualViews.stream()
				.map(VirtualView::getNickname)
				.collect(Collectors.toList());

		this.model = new Model(mapType.getMapName(), playerNames, skulls);
		setObservers();
		gameController = new GameController(model, virtualViewsContainer);
		Utils.logInfo("Created controller.");
	}

	/**
	 * Starts the game.
	 */
	public void startGame() {
		Utils.logInfo("Controller -> startGame(): Starting the game");
		gameController.startGame();
	}

	/**
	 * Returns true if the game is ended.
	 * @return true if the game is ended.
	 */
	public boolean isGameEnded() {
		return model.isGameEnded();
	}


	@Override
	public void update(Observable o, Object arg) {
		if(!isGameEnded())
			gameController.processEvent((Event) arg);
	}

	/**
	 * For every VirtualView, attach its observers to the corresponding observable classes of the model.
	 */
	private void setObservers() {
		for (VirtualView virtualView : virtualViewsContainer.getVirtualViews()) {
			// Add VirtualView's observers to the model. (VirtualView -👀-> Model)
			model.addGameBoardObserver(virtualView.getGameBoardObserver());
			Utils.logInfo(virtualView.getNickname() + " now observes Game Board.");
			model.addGameMapObserver(virtualView.getGameMapObserver());
			Utils.logInfo(virtualView.getNickname() + " now observes Game Map.");
			model.addPlayersObserver(virtualView.getPlayerObserver());
			Utils.logInfo(virtualView.getNickname() + " now observes all the Players.");

			// Add Controller's observer to the VirtualView. (Controller -👀-> VirtualView)
			virtualView.addObserver(this);
			Utils.logInfo("Controller now observes virtual View of "+virtualView.getNickname());
		}
	}

	// ####################################
	// METHODS ONLY FOR TESTS
	// ####################################

	protected Model getModel() {
		return model;
	}
}