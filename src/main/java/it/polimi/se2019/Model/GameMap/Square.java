package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;

import java.util.ArrayList;

public abstract class Square {

	private ArrayList<Boolean> possibleDirections;
	private int roomID;


	public abstract void addCard(Card cardToAdd);

	public abstract Card grabCard();

	public abstract ArrayList<Card> listCards();

}