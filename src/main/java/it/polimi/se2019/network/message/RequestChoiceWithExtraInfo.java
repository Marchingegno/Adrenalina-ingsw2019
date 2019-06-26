package it.polimi.se2019.network.message;

import java.util.List;

public class RequestChoiceWithExtraInfo extends RequestChoiceInArrayMessage {

	private String info;


	public RequestChoiceWithExtraInfo(String info, List<Integer> availableIndexes, MessageType messageType) {
		super(availableIndexes, messageType);
		this.info = info;
	}


	public String getInfo() {
		return info;
	}
}
