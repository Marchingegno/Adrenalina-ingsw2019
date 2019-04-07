package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.weapons.WeaponCard;

import java.util.ArrayList;

public class SpawnSquare extends Square {

	private AmmoType color;
	private WeaponCard weaponCard;


	public SpawnSquare(Card card1, Card card2, Card card3) {
	}


	@Override
	public void addCard(Card cardToAdd) {
	}

	@Override
	public Card grabCard() {
		return null;
	}

	@Override
	public ArrayList<Card> listCards() {
		return null;
	}

	public AmmoType getAmmoType() {
		return null;
	}


}