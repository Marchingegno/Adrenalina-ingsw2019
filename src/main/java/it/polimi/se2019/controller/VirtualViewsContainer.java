package it.polimi.se2019.controller;

import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.VirtualView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VirtualViewsContainer {

	private ArrayList<VirtualView> virtualViews;


	public VirtualViewsContainer(Collection<VirtualView> virtualViews) {
		this.virtualViews = new ArrayList<>(virtualViews);
	}


	public List<VirtualView> getVirtualViews() {
		return virtualViews;
	}

	/**
	 * Sends the updated reps contained in the VirtualView and sent by the model.
	 * Note: if an action is requested to the player this methods mustn't be called before the request (otherwise two messages will be sent)!
	 */
	public void sendUpdatedReps() {
		Utils.logInfo("VirtualViewsContainer -> sendUpdatedReps(): sending latest reps to everyone");
		for (VirtualView virtualView : virtualViews) {
			virtualView.sendReps();
		}
	}

	public VirtualView getVirtualViewFromPlayerName(String playerName){
		return virtualViews.stream()
				.filter(item -> item.getNickname().equals(playerName))
				.findFirst()
				.orElseThrow(() -> new RuntimeException(playerName + " not found!"));
	}
}
