package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.ammo.AmmoCard;
import it.polimi.se2019.utils.Color;

/**
 * A sharable version of the ammo square.
 *
 * @author MarcerAmdrea
 */
public class AmmoSquareRep extends SquareRep {

	AmmoSquareRep(Square squareToRepresent) {
		super(squareToRepresent);
		elementsToPrint = new String[3];
		AmmoCard ammoCard = (AmmoCard) squareToRepresent.getCards().get(0);
		elementsToPrint[0] = Color.getColoredCell(ammoCard.getAmmo().get(0).getBackgroundColorType());
		elementsToPrint[1] = Color.getColoredCell(ammoCard.getAmmo().get(1).getBackgroundColorType());
		elementsToPrint[2] = Color.getColoredCell(ammoCard.hasPowerup() ? Color.BackgroundColorType.WHITE : ammoCard.getAmmo().get(2).getBackgroundColorType());
	}
}
