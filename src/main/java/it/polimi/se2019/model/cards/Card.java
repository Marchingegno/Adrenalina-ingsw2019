package it.polimi.se2019.model.cards;

import it.polimi.se2019.model.Representable;

/**
 * Abstract class that all cards have to extend.
 *
 * @author MarcerAndrea
 */
public abstract class Card implements Representable {

	private String description;
	private String cardName;
	private String imagePath;

	public Card(String cardName, String description, String imagePath) {
		this.cardName = cardName;
		this.description = description;
		this.imagePath = imagePath;
	}

	/**
	 * Returns the description of the card.
	 *
	 * @return the description of the card.
	 */
	public String getCardDescription() {
		return description;
	}

	String getImagePath() {
		return imagePath;
	}

	public String getCardName() {
		return cardName;
	}
}