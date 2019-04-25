package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.weapons.WeaponRep;

import java.util.ArrayList;

public class MapSquareSquareRep extends MapSquareRep {

	private ArrayList<WeaponRep> weaponsName;

	public MapSquareSquareRep(MapSquare mapSquareToRepresent) {
		super(mapSquareToRepresent);
	}
}
