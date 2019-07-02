package it.polimi.se2019.network.message;

/**
 * Message used for the answer of SWAP_WEAPON action.
 */
public class SwapMessage extends Message {

	private final int indexToGrab;
	private final int indexToDiscard;


	/**
	 * Constructs a message.
	 *
	 * @param indexToGrab    the index of the weapon the player is grabbing.
	 * @param indexToDiscard the index of the weapon the player wants to discard.
	 */
	public SwapMessage(int indexToGrab, int indexToDiscard) {
		super(MessageType.SWAP_WEAPON, MessageSubtype.ANSWER);
		this.indexToGrab = indexToGrab;
		this.indexToDiscard = indexToDiscard;
	}


	/**
	 * Returns the index of the weapon grabbed.
	 *
	 * @return the index of the weapon grabbed.
	 */
	public int getIndexToGrab() {
		return indexToGrab;
	}

	/**
	 * Returns the index of the weapon to discard.
	 *
	 * @return the index of the weapon to discard.
	 */
	public int getIndexToDiscard() {
		return indexToDiscard;
	}
}
