package it.polimi.se2019.network.message;

import it.polimi.se2019.model.gamemap.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class CoordinatesRequestMessage extends Message {

	private final ArrayList<Coordinates> coordinates;


	public CoordinatesRequestMessage(List<Coordinates> coordinates, MessageType messageType) {
		super(messageType, MessageSubtype.REQUEST);
		this.coordinates = new ArrayList<>(coordinates);
	}


	public List<Coordinates> getCoordinates() {
		return coordinates;
	}
}
