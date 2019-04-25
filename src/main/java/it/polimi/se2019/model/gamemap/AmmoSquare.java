package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.ammo.AmmoCard;
import it.polimi.se2019.utils.Utils;

/**
 * Normal square associated with an ammo card
 */
public class AmmoSquare extends MapSquare {

	private AmmoCard ammoCard;
	private boolean isFilled;

	public AmmoSquare(int roomID, boolean[] possibleDirections, Coordinates coordinates) {
		super(possibleDirections, roomID, coordinates);
		isFilled = false;
	}

	public AmmoCard grabAmmoCard() {
		return ammoCard;
	}

	public void setAmmoCard(AmmoCard ammoCard) {
		this.ammoCard = ammoCard;
	}

	public boolean isFilled() {
		return isFilled;
	}

	public void setFilled() {
		isFilled = true;
	}

	public void setNtoFilled() {
		isFilled = false;
	}

	public String[] getElementsToPrint(){
		String[] elementsToPrint = new String[3];
		elementsToPrint[0] = Utils.getColoredCell(ammoCard.getAmmos().get(0).getBackgroundColorType());
		elementsToPrint[1] = Utils.getColoredCell(ammoCard.getAmmos().get(1).getBackgroundColorType());
		elementsToPrint[2] = Utils.getColoredCell(ammoCard.hasPowerup()? Utils.BackgroundColorType.WHITE : ammoCard.getAmmos().get(2).getBackgroundColorType());
		return elementsToPrint;
	}
}