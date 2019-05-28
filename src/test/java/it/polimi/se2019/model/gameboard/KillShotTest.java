package it.polimi.se2019.model.gameboard;

import it.polimi.se2019.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KillShotTest {

	private static final Player player1 = new Player("Test 1", 0);

	@Test
	public void getPlayer_initialState_correctOutput() {
		KillShot killShot = new KillShot(player1, true);
		assertSame(player1, killShot.getPlayer());
	}

	@Test
	public void isOverkill_initialState_correctOutput() {
		KillShot killShot = new KillShot(player1, true);
		assertTrue(killShot.isOverkill());

		killShot = new KillShot(player1, false);
		assertFalse(killShot.isOverkill());
	}

	@Test
	public void getRep_initialState_correctOutput() {
		KillShot killShot = new KillShot(player1, true);
		KillShotRep killShotRep = (KillShotRep) killShot.getRep();
		assertTrue(killShotRep.isOverkill());
		assertEquals(player1.getPlayerName(), killShotRep.getPlayerName());
		assertEquals(player1.getPlayerColor(), killShotRep.getPlayerColor());
	}
}