package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.cards.weapons.WeaponRep;

import java.util.ArrayList;
import java.util.List;

public class MapSquareSpawnRep extends MapSquareRep {

	private List<WeaponRep> weaponsRep;

	public MapSquareSpawnRep(MapSquare mapSquareToRepresent) {
		super(mapSquareToRepresent);
		elementsToPrint = new String[3];
		elementsToPrint[0] = "S";
		elementsToPrint[1] = "P";
		elementsToPrint[2] = "W";
		weaponsRep = new ArrayList<>();
		for (Card card : mapSquareToRepresent.getCards()) {
			weaponsRep.add(new WeaponRep((WeaponCard) card));
		}
	}

	public List<WeaponRep> getWeaponsRep() {
		return new ArrayList<>(weaponsRep);
	}
}
