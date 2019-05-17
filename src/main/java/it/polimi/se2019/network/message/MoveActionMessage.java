package it.polimi.se2019.network.message;

import it.polimi.se2019.model.gamemap.Coordinates;

import java.util.List;

public class MoveActionMessage extends Message {
	private List<Coordinates> coordinates;
	private int distance;

	public MoveActionMessage(List<Coordinates> coordinates, int distance, MessageSubtype messageSubtype) {
		super(MessageType.MOVE, messageSubtype);
		this.coordinates = coordinates;
		this.distance = distance;
	}

	public MoveActionMessage(List<Coordinates> coordinates, MessageSubtype messageSubtype) {
		super(MessageType.MOVE, messageSubtype);
		this.coordinates = coordinates;
		this.distance = -1;
	}

	public List<Coordinates> getCoordinates() {
		return coordinates;
	}
}
