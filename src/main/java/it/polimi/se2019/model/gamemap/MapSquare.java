package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.Representable;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represent a square in the map
 * @author MarcerAndrea
 */
public abstract class MapSquare implements Representable {

	private int roomID;
	private boolean[] possibleDirections;
	private List<MapSquare> adjacentMapSquares;
	private Coordinates coordinates;
	protected List<Card> cards;
	protected Deck<Card> deck;
	protected boolean isChanged;
	private boolean isFilled;
	protected Representation squareRep;


	public MapSquare(boolean[] possibleDirections, int roomID, Coordinates coordinates) {
		adjacentMapSquares = new ArrayList<>();
		this.possibleDirections = possibleDirections;
		this.roomID = roomID;
		this.coordinates = coordinates;
	}

	/**
	 * Returns the list of adjacent squares.
	 * @return the list of adjacent squares.
	 */
	public List<MapSquare> getAdjacentMapSquares(){
		return new ArrayList<>(adjacentMapSquares);
	}

	/**
	 * Returns the room ID.
	 * @return the room ID.
	 */
	public int getRoomID() {return roomID;}

	/**
	 * Returns the coordinates of the square in the map.
	 * @return the coordinates of the square in the map.
	 */
	public Coordinates getCoordinates() {
		return coordinates;
	}

	/**
	 * Returns the possible direction toward which the can move.
	 * @return the possible direction toward which the can move.
	 */
	public boolean[] getPossibleDirections() {return possibleDirections.clone();}

	public boolean isFilled() {
		return isFilled;
	}

	public void setFilled() {
		isFilled = true;
	}

	public void setNotFilled() {
		isFilled = false;
	}

	/**
	 * Adds an adjacent square to the list of adjacent squares.
	 * @param adjacentMapSquare the adjacent square to add to the list.
	 */
	public void addAdjacentSquare(MapSquare adjacentMapSquare){
		adjacentMapSquares.add(adjacentMapSquare);
	}

	public abstract Card grabCard(int index);

	public abstract void refillCards();

	public boolean isChanged(){	return isChanged; }

	public List<Card> getCards() { return new ArrayList<>(cards); }

	public abstract Representation getRep();

	public abstract String[] getElementsToPrint();
}