package it.polimi.se2019.model.gamemap;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.Newton;
import it.polimi.se2019.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class VoidSquareTest {

	VoidSquare voidSquare;

	@Before
	public void setup() {
		voidSquare = new VoidSquare(new Coordinates(0, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void grabCard_methodCall_shouldGiceException() {
		voidSquare.grabCard(0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addCard_methodCall_shouldGiceException() {
		voidSquare.addCard(new Newton(AmmoType.RED_AMMO));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void canGrab_methodCall_shouldGiceException() {
		voidSquare.canGrab(new Player("Test1", 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getGrabMessageType_methodCall_shouldGiceException() {
		voidSquare.getGrabMessageType();
	}

	@Test
	public void getRep_methodCall_correctOutput() {
		assertNotNull(voidSquare.getRep());
		voidSquare.setChanged();
		voidSquare.getRep();
	}

}