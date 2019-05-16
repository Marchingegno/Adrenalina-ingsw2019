package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.ammo.AmmoCardRep;
import it.polimi.se2019.utils.Color;

/**
 * A sharable version of the ammo square.
 *
 * @author MarcerAmdrea
 */
public class AmmoSquareRep extends SquareRep {

	private AmmoCardRep ammoCardRep;

	AmmoSquareRep(Square squareToRepresent) {
		super(squareToRepresent);
		if (!squareToRepresent.cards.isEmpty()) {
			ammoCardRep = (AmmoCardRep) (squareToRepresent.cards.get(0)).getRep();
		} else
			ammoCardRep = null;
	}

	@Override
	public String[] getElementsToPrint() {
		String[] elementsToPrint = new String[3];
		if (ammoCardRep != null) {
			elementsToPrint[0] = Color.getColoredCell(ammoCardRep.getAmmo().get(0).getBackgroundColorType());
			elementsToPrint[1] = Color.getColoredCell(ammoCardRep.getAmmo().get(1).getBackgroundColorType());
			elementsToPrint[2] = Color.getColoredCell(ammoCardRep.hasPowerup() ? Color.BackgroundColorType.WHITE : ammoCardRep.getAmmo().get(2).getBackgroundColorType());
		} else {
			elementsToPrint[0] = " ";
			elementsToPrint[1] = " ";
			elementsToPrint[2] = " ";
		}
		return elementsToPrint;
	}
}
