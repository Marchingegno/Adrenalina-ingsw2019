package it.polimi.se2019.network.message;

import it.polimi.se2019.model.gamemap.Coordinates;

import java.util.List;

public class AskCoordinatesMessage extends CoordinatesRequestMessage {

	private String question;


	public AskCoordinatesMessage(String question, List<Coordinates> coordinates, MessageType messageType) {
		super(coordinates, messageType);
		this.question = question;
	}


	public String getQuestion() {
		return question;
	}
}
