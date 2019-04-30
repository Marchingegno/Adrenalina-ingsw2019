package it.polimi.se2019.model.gamemap;

import java.io.Serializable;

public class MapSquareRep implements Serializable {

	protected String[] elementsToPrint;
	private int roomID;
	private Coordinates coordinates;
	private boolean[] possibleDirection;

	public MapSquareRep(MapSquare mapSquareToRepresent) {
		this.roomID = mapSquareToRepresent.getRoomID();
		this.coordinates = mapSquareToRepresent.getCoordinates();
		this.possibleDirection = mapSquareToRepresent.getPossibleDirections();
		//elementToPrint = mapSquareToRepresent.getElementsToPrint().clone();
	}

	public int getRoomID() {
		return roomID;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public boolean[] getPossibleDirection() {
		return possibleDirection;
	}

	public String[] getElementToPrint() {
		return elementsToPrint;
	}
}
