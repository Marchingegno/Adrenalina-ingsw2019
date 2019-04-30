package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.cards.weapons.WeaponRep;

import java.util.ArrayList;
import java.util.List;

public class MapSquareSpawnRep extends MapSquareRep {

	private List<WeaponRep> weaponsRep;

	public MapSquareSpawnRep(MapSquare mapSquareToRepresent) {
		super(mapSquareToRepresent);
		weaponsRep = new ArrayList<>();
		for (WeaponCard weaponCard : ((SpawnSquare) mapSquareToRepresent).getWeapons()) {
			weaponsRep.add(new WeaponRep(weaponCard));
		}
	}

	public List<WeaponRep> getWeaponsRep(){
		return new ArrayList<>(weaponsRep);
	}
}
