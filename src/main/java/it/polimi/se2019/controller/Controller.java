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
 * This class is the top level controller. Its jobs are to delegate the model initialization and to start the game.
 * @author Marchingegno
 */
public class Controller implements Observer {

	private VirtualViewsContainer virtualViewsContainer;
	private GameController gameController;
	private Model model;


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


	public void startGame() {
		Utils.logInfo("Controller -> startGame(): Starting the game");
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
		gameController.processEvent((Event) arg);
	}

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
}