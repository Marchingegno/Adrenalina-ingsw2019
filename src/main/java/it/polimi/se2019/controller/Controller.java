package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.model.player.Player;
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
		model.updateReps();
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
		gameController.processMessage((Event) arg);
	}

	private void setObservers() {
		for (VirtualView virtualView : virtualViews) {
			// Add VirtualView's observers to the model. (VirtualView -👀-> Model)
			model.getGameBoard().addObserver(virtualView.getGameBoardObserver());
			Utils.logInfo(virtualView.getPlayerName() + " now observes Game Board");
			model.getGameBoard().getGameMap().addObserver(virtualView.getGameMapObserver());
			Utils.logInfo(virtualView.getPlayerName() + " now observes Game Map");
			for (Player player : model.getPlayers()) {
				player.addObserver(virtualView.getPlayerObserver());
				Utils.logInfo(virtualView.getPlayerName() + " now observes " + player.getPlayerName());
			}

			// Add Controller's observer to the VirtualView. (Controller -👀-> VirtualView)
			virtualView.addObserver(this);
		}
	}

	static VirtualView getVirtualViewFromPlayer(Player player, List<VirtualView> virtualViews){
		String playerName = player.getPlayerName();
		return virtualViews.stream()
				.filter(item -> item.getPlayerName().equals(playerName))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Player not found!"));
	}
}