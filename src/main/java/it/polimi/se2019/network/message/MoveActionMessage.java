package it.polimi.se2019.network.message;

import it.polimi.se2019.model.gamemap.Coordinates;

public class MoveActionMessage extends ActionMessage {
	Coordinates coordinates;

	public MoveActionMessage(Coordinates coordinates, MessageType messageType, MessageSubtype messageSubtype) {
		super(MessageType.MOVE, messageSubtype);
		this.coordinates = coordinates;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}
}
