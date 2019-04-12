package it.polimi.se2019.controller;

import it.polimi.se2019.model.player.Player;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Extension of ArrayDeque that only uses the class Player as the type of elements and adds the method moveFirstToLast().
 * @author Desno365
 */
public class PlayerQueue extends ArrayDeque<Player> {

	/**
	 * Constructs a PlayerQueue containing the elements of the specified list, preserving the order.
	 * @param playerQueue the list whose elements are to be placed into the PlayerQueue.
	 */
	public PlayerQueue(List<Player> playerQueue) {
		super(playerQueue);
	}


	/**
	 * Move the first element of this Queue to the last position.
	 */
	public void moveFirstToLast() {
		if(!isEmpty()) {
			addLast(removeFirst());
		}
	}

}