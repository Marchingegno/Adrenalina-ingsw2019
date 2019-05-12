package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.Utils;

public class VoidSquare extends Square {

	public VoidSquare(Coordinates coordinates) {
		super(-1, new boolean[]{false, false, false, false}, Color.CharacterColorType.DEFAULT, coordinates);
	}

	@Override
	public Card grabCard(int index) {
		throw new UnsupportedOperationException("Can't call grabCard() in a VoidSquare");
	}

	@Override
	public void addCard(Card cardToAdd) {
		throw new UnsupportedOperationException("Can't call addCard() in a VoidSquare");
	}

	@Override
	public void refillCards() {
		Utils.logInfo("No refill needed in a VoidSquare.");
	}

	/**
	 * Returns the spawn square's representation.
	 *
	 * @return the spawn square's representation.
	 */
	@Override
	public SquareRep getRep() {
		if (hasChanged || squareRep == null) {
			squareRep = new VoidSquareRep(this);
			setNotChanged();
			Utils.logInfo("SpawnSquare -> getRep(): Updated the square's representation");
		}
		return squareRep;
	}
}
