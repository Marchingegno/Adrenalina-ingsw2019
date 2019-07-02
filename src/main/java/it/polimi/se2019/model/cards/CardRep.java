package it.polimi.se2019.model.cards;

import it.polimi.se2019.model.Representation;

public class CardRep implements Representation {

	private String description;
	private String cardName;
	private String imagePath;

	public CardRep(Card card) {
		this.cardName = card.getCardName();
		this.description = card.getCardDescription();
		this.imagePath = card.getImagePath();
	}

	/**
	 * Returns the description of the card.
	 *
	 * @return the description of the card.
	 */
	public String getCardDescription() {
		return description;
	}

	public String getCardName() {
		return cardName;
	}

	public String getImagePath() {
		return imagePath;
	}
}
