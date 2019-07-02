package it.polimi.se2019.controller;

import it.polimi.se2019.utils.PlayerRepPosition;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.VirtualView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Container for the VirtualViews of the player.
 */
class VirtualViewsContainer {

	private ArrayList<VirtualView> virtualViews;


	/**
	 * Constructor of the class.
	 *
	 * @param virtualViews the collection of the player's VirtualViews.
	 */
	VirtualViewsContainer(Collection<VirtualView> virtualViews) {
		this.virtualViews = new ArrayList<>(virtualViews);
	}


	/**
	 * Returns a list with all the VirtualViews in this container.
	 *
	 * @return a list with all the VirtualViews in this container.
	 */
	List<VirtualView> getVirtualViews() {
		return virtualViews;
	}

	/**
	 * Sends the updated reps contained in the VirtualView and sent by the model.
	 * Note: if an action is requested to the player this methods mustn't be called before the request (otherwise two messages will be sent)!
	 */
	void sendUpdatedReps() {
		Utils.logInfo("VirtualViewsContainer -> sendUpdatedReps(): sending latest reps to everyone");
		for (VirtualView virtualView : virtualViews) {
			virtualView.sendReps();
		}
	}

	/**
	 * Sends the end game message to the players.
	 *
	 * @param finalPlayersInfo the leaderboard of the ended game.
	 */
	void sendEndGameMessage(List<PlayerRepPosition> finalPlayersInfo) {
		virtualViews.forEach(virtualView -> virtualView.endOfGame(finalPlayersInfo));
	}

	/**
	 * Returns the VirtualView associated with the nickname of the player.
	 *
	 * @param playerName the nickname of the player.
	 * @return the VirtualView of the player.
	 */
	VirtualView getVirtualViewFromPlayerName(String playerName) {
		return virtualViews.stream()
				.filter(item -> item.getNickname().equals(playerName))
				.findFirst()
				.orElseThrow(() -> new RuntimeException(playerName + " not found!"));
	}
}
