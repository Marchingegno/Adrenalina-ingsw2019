package it.polimi.se2019.network.message;

import java.util.ArrayList;
import java.util.List;

public class RequestChoiseInArrayMessage extends Message {

	private ArrayList<Integer> activablePowerups;


	public RequestChoiseInArrayMessage(List<Integer> activablePowerups, MessageType messageType) {
		super(messageType, MessageSubtype.REQUEST);
		this.activablePowerups = new ArrayList<>(activablePowerups);
	}


	public List<Integer> getAvailableIndexes() {
		return activablePowerups;
	}
}
