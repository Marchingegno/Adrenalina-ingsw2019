package it.polimi.se2019.network.message;

import java.util.ArrayList;
import java.util.List;

public class RequestChoiceInArrayMessage extends Message {

	private final ArrayList<Integer> availableIndexes;


	public RequestChoiceInArrayMessage(List<Integer> availableIndexes, MessageType messageType) {
		super(messageType, MessageSubtype.REQUEST);
		this.availableIndexes = new ArrayList<>(availableIndexes);
	}


	public List<Integer> getAvailableIndexes() {
		return availableIndexes;
	}
}
