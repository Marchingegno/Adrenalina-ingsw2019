package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.ammo.AmmoCard;
import it.polimi.se2019.utils.Color;

public class AmmoSquareRep extends MapSquareRep {
	public AmmoSquareRep(MapSquare mapSquareToRepresent) {
		super(mapSquareToRepresent);
		elementsToPrint = new String[3];
		AmmoCard ammoCard = (AmmoCard) mapSquareToRepresent.getCards().get(0);
		elementsToPrint[0] = Color.getColoredCell(ammoCard.getAmmos().get(0).getBackgroundColorType());
		elementsToPrint[1] = Color.getColoredCell(ammoCard.getAmmos().get(1).getBackgroundColorType());
		elementsToPrint[2] = Color.getColoredCell(ammoCard.hasPowerup() ? Color.BackgroundColorType.WHITE : ammoCard.getAmmos().get(2).getBackgroundColorType());
	}
}
