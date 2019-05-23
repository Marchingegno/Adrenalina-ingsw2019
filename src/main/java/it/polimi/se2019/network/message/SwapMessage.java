package it.polimi.se2019.network.message;

public class SwapMessage extends Message {

	private final int indexToGrab;
	private final int indexToDiscard;


	public SwapMessage(int indexToGrab, int indexToDiscard, MessageType messageType) {
		super(messageType, MessageSubtype.ANSWER);
		this.indexToGrab = indexToGrab;
		this.indexToDiscard = indexToDiscard;
	}


	public int getIndexToGrab() {
		return indexToGrab;
	}

	public int getIndexToDiscard() {
		return indexToDiscard;
	}
}
