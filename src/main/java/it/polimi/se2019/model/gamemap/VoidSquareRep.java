package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.utils.Color;

public class VoidSquareRep extends SquareRep {

	private String[] elementsToPrint;
	private int roomID;
	private Color.CharacterColorType squareColor;
	private Coordinates coordinates;
	private boolean[] possibleDirection;

	public VoidSquareRep(VoidSquare voidSquareToRepresent) {
		super(voidSquareToRepresent);
		elementsToPrint = new String[]{" ", " ", " "};
	}
}
