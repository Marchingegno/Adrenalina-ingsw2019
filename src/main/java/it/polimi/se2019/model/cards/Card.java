package it.polimi.se2019.model.cards;

public abstract class Card {

	private String description;

	public Card(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}