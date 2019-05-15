package it.polimi.se2019.model.cards;

import it.polimi.se2019.model.Representation;

public class CardRep extends Representation {

	private String description;
	private String cardName;

	public CardRep(Card card) {
		this.cardName = card.getCardName();
		this.description = card.getCardDescription();
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
}
