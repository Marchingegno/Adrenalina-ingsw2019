package it.polimi.se2019.model.cards;

/**
 * Abstract class that all cards have to extend.
 *
 * @author MarcerAndrea
 */
public abstract class Card {

	protected String description;

	public Card(String description) {
		this.description = description;
	}

	/**
	 * Returns the description of the card.
	 *
	 * @return the description of the card.
	 */
	public String getDescription() {
		return description;
	}

}