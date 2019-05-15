package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is the top level controller. Its jobs are to delegate the model initialization and to start the game.
 * @author Marchingegno
 */
public class Controller implements Observer {

	private List<VirtualView> virtualViews;
	private GameController gameController;
	private Model model;


	public Controller(GameConstants.MapType mapType, Collection<VirtualView> virtualViews, int skulls) {
		this.virtualViews = new ArrayList<>(virtualViews);

		// Create a list of player names.
		List<String> playerNames = virtualViews.stream()
				.map(VirtualView::getPlayerName)
				.collect(Collectors.toList());

		this.model = new Model(mapType.getMapName(), playerNames, skulls);
		setObservers();
		gameController = new GameController(model, this.virtualViews);
		Utils.logInfo("Created controller.");
	}


	public void startGame() {
		Utils.logInfo("Controller -> startGame");
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
		Utils.logInfo("Controller: received an event:" + arg.toString());
		gameController.processEvent((Event) arg);
	}

	private void setObservers() {
		for (VirtualView virtualView : virtualViews) {
			// Add VirtualView's observers to the model. (VirtualView -👀-> Model)
			model.addGameBoardObserver(virtualView.getGameBoardObserver());
			Utils.logInfo(virtualView.getPlayerName() + " now observes Game Board.");
			model.addGameMapObserver(virtualView.getGameMapObserver());
			Utils.logInfo(virtualView.getPlayerName() + " now observes Game Map.");
			model.addPlayersObserver(virtualView.getPlayerObserver());
			Utils.logInfo(virtualView.getPlayerName() + " now observes all the Players.");

			// Add Controller's observer to the VirtualView. (Controller -👀-> VirtualView)
			virtualView.addObserver(this);
			Utils.logInfo("Controller now observes virtual View of "+virtualView.getPlayerName());
		}
	}

	static VirtualView getVirtualViewFromPlayerName(String playerName, List<VirtualView> virtualViews){
		return virtualViews.stream()
				.filter(item -> item.getPlayerName().equals(playerName))
				.findFirst()
				.orElseThrow(() -> new RuntimeException(playerName + " not found!"));
	}

	/**
	 * Sends the updated reps contained in the VirtualView and sent by the model.
	 * Note: if an action is requested to the player this methods mustn't be called before the request (otherwise two messages will be sent)!
	 * @param virtualViews the VirtualViews that have the reps.
	 */
	static void sendUpdatedReps(List<VirtualView> virtualViews) {
		for (VirtualView virtualView : virtualViews) {
			Utils.logInfo("Controller -> sendUpdatedReps(): sending latest rep for " + virtualView.getPlayerName() + ".");
			virtualView.sendReps();
		}
	}
}