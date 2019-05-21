package it.polimi.se2019.network.message;

public class SwapMessage extends Message {

	int indexToGrab;
	int indexToDescard;

	public SwapMessage(int indexToGrab, int indexToDescard, MessageType messageType) {
		super(messageType, MessageSubtype.ANSWER);
		this.indexToGrab = indexToGrab;
		this.indexToDescard = indexToDescard;
	}

	public int getIndexToGrab() {
		return indexToGrab;
	}

	public int getIndexToDescard() {
		return indexToDescard;
	}
}
