package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.cards.weapons.WeaponRep;

import java.util.ArrayList;
import java.util.List;

/**
 * A sharable version of the spawn square.
 *
 * @author MarcerAmdrea
 */
public class SquareSpawnRep extends SquareRep {

	private List<WeaponRep> weaponsRep;

	SquareSpawnRep(Square squareToRepresent) {
		super(squareToRepresent);
		weaponsRep = new ArrayList<>();
		for (Card card : squareToRepresent.getCards()) {
			weaponsRep.add(new WeaponRep((WeaponCard) card));
		}
	}

	@Override
	public String[] getElementsToPrint() {
		return new String[]{"S", "P", "W"};
	}

	/**
	 * Returns the list of the weapons' reps in the square.
	 *
	 * @return the list of the weapons' reps in the square.
	 */
	public List<WeaponRep> getWeaponsRep() {
		return weaponsRep;
	}


}
