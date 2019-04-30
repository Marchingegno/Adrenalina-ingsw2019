package it.polimi.se2019.model.gamemap;

import java.io.Serializable;

public class MapSquareRep implements Serializable {

	private int roomID;
	private Coordinates coordinates;
	private boolean[] possibleDirection;
	private String[] elementToPrint;

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

	public boolean[] getPossibleDirection() { return possibleDirection; }

	public String[] getElementToPrint() {return elementToPrint;}
}
