package it.polimi.se2019.model.cards;

import it.polimi.se2019.model.Representation;

/**
 * Represents a card.
 *
 * @author MarcerAndrea
 */
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

    /**
     * Returns the name of the card.
     *
     * @return the name of the card.
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Returns the image path of the card.
     *
     * @return the image path of the card.
     */
    public String getImagePath() {
        return imagePath;
    }
}
