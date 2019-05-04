package it.polimi.se2019.model.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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

	public List<Player> toList(){
		return new ArrayList (Arrays.asList(this));
	}





}