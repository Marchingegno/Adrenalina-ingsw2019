package it.polimi.se2019.network.message;

import it.polimi.se2019.model.gamemap.Coordinates;

public class MoveActionMessage extends Message {
	private Coordinates coordinates;

	public MoveActionMessage(Coordinates coordinates, MessageSubtype messageSubtype) {
		super(MessageType.MOVE, messageSubtype);
		this.coordinates = coordinates;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}
}
