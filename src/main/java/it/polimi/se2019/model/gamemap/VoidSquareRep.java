package it.polimi.se2019.model.gamemap;

/**
 * A sharable version of the void square.
 *
 * @author MarcerAndrea
 */
public class VoidSquareRep extends SquareRep {

    VoidSquareRep(VoidSquare voidSquareToRepresent) {
		super(voidSquareToRepresent);
	}

	@Override
	public String[] getElementsToPrint() {
		return new String[]{" ", " ", " "};
	}
}
