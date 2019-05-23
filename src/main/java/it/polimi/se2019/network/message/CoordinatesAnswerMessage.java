package it.polimi.se2019.network.message;

import it.polimi.se2019.model.gamemap.Coordinates;

public class CoordinatesAnswerMessage extends Message {

	private final Coordinates singleCoordinates;


	public CoordinatesAnswerMessage(Coordinates coordinates, MessageType messageType) {
		super(messageType, MessageSubtype.ANSWER);
		this.singleCoordinates = coordinates;
	}


	public Coordinates getSingleCoordinates() {
		return singleCoordinates;
	}
}
