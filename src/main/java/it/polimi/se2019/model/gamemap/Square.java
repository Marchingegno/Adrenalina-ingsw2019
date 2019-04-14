package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;
import java.util.ArrayList;

/**
 * Class that represent a square in the map
 * @author MarcerAndrea
 */
public abstract class Square {

	private boolean[] possibleDirections;
	private int roomID;
	private ArrayList<Square> adjacentSquares = new ArrayList<>();
	private ArrayList<Card> cards = new ArrayList<>();
	private Coordinates coordinates;

	public Square(boolean[] possibleDirections, int roomID, Coordinates coordinates) {
		this.possibleDirections = possibleDirections;
		this.roomID = roomID;
		this.coordinates = coordinates;
	}

	/**
	 * Adds to the square a card
	 * @param cardToAdd card to add to the square
	 */
	public void addCard(Card cardToAdd){cards.add(cardToAdd);}

	/**
	 * Removes and returns a card from the square
	 * @return the removed card
	 */
	public abstract Card grabCard();

	/**
	 *
	 * @return
	 */
	public int getRoomID() {return roomID;	}

	/**
	 *
	 * @return
	 */
	public Coordinates getCoordinates() {
		return coordinates;
	}

	/**
	 *
	 * @return
	 */
	public boolean[] getPossibleDirections() {return possibleDirections.clone();}

	/**
	 * @return the list of the cards in the square
	 */
	public ArrayList<Card> getCardList(){return (ArrayList<Card>) cards.clone();}

	public ArrayList<Square> getAdjacentSquares(){
		return (ArrayList<Square>) adjacentSquares.clone();
	}


	void addAdjacentSquare(Square adjacentSquare){
		adjacentSquares.add(adjacentSquare);
	}

}