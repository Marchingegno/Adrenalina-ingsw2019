package it.polimi.se2019.model.player;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.Newton;
import it.polimi.se2019.model.cards.weapons.Cyberblade;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.exceptions.HiddenException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Desno365
 */
public class PlayerRepTest {

	private static final Player player1 = new Player("Test 1", 0);
	private static final Player player2 = new Player("Test 2", 1);
	private static final Player player3 = new Player("Test 3", 2);

	@BeforeClass
	public static void oneTimeSetUp() {
		player1.getPlayerBoard().addPoints(11);
		player2.getPlayerBoard().addPoints(22);
		player3.getPlayerBoard().addPoints(33);
		player1.getPlayerBoard().addDamage(player2, 3);
		player2.getPlayerBoard().addDamage(player3, 3);
		player3.getPlayerBoard().addDamage(player1, 3);
		player1.getPlayerBoard().addMarks(player2, 2);
		player2.getPlayerBoard().addMarks(player3, 2);
		player3.getPlayerBoard().addMarks(player1, 2);
		player1.getPlayerBoard().addWeapon(new Cyberblade("Desc1"));
		player2.getPlayerBoard().addWeapon(new Cyberblade("Desc2"));
		player2.getPlayerBoard().addWeapon(new Cyberblade("Desc3"));
		player3.getPlayerBoard().addWeapon(new Cyberblade("Desc4"));
		player3.getPlayerBoard().addWeapon(new Cyberblade("Desc5"));
		player3.getPlayerBoard().addWeapon(new Cyberblade("Desc6"));
		player1.getPlayerBoard().addPowerup(new Newton(AmmoType.RED_AMMO));
		player2.getPlayerBoard().addPowerup(new Newton(AmmoType.YELLOW_AMMO));
		player2.getPlayerBoard().addPowerup(new Newton(AmmoType.YELLOW_AMMO));
		player3.getPlayerBoard().addPowerup(new Newton(AmmoType.BLUE_AMMO));
		player3.getPlayerBoard().addPowerup(new Newton(AmmoType.BLUE_AMMO));
		player3.getPlayerBoard().addPowerup(new Newton(AmmoType.BLUE_AMMO));
		player1.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.RED_AMMO);
		player2.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.RED_AMMO);
		player3.getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.RED_AMMO);
	}

	@Test
	public void getHiddenPlayerRep_initialState_correctOutput() {
		PlayerRep playerRep = new PlayerRep(player1);
		assertFalse(playerRep.isHidden());
		PlayerRep hiddenRep = playerRep.getHiddenPlayerRep();
		assertTrue(hiddenRep.isHidden());
	}

	@Test
	public void getPlayerName_initialState_correctOutput() {
		PlayerRep playerRep = new PlayerRep(player1);
		assertEquals("Test 1", playerRep.getPlayerName());
	}

	@Test
	public void getPlayerColor_initialState_correctOutput() {
		PlayerRep playerRep = new PlayerRep(player2);
		assertEquals(Color.CharacterColorType.GREEN, playerRep.getPlayerColor());
	}

	@Test
	public void getPoints_initialState_correctOutput() throws HiddenException {
		PlayerRep playerRep = new PlayerRep(player3);
		assertEquals(33, playerRep.getPoints());
	}

	@Test (expected = HiddenException.class)
	public void getPoints_hiddenState_shouldThrowException() throws HiddenException {
		PlayerRep playerRep = new PlayerRep(player3).getHiddenPlayerRep();
		assertEquals(33, playerRep.getPoints());
	}

	@Test
	public void getDamageBoard_initialState_correctOutput() {
		PlayerRep playerRep = new PlayerRep(player1);
		List list = playerRep.getDamageBoard();
		assertEquals(player2.getPlayerColor(), list.get(0));
		assertEquals(player2.getPlayerColor(), list.get(1));
		assertEquals(player2.getPlayerColor(), list.get(2));
		assertEquals(3, list.size());
	}

	@Test
	public void getMarks_initialState_correctOutput() {
		PlayerRep playerRep = new PlayerRep(player1);
		List list = playerRep.getMarks();
		assertEquals(player2.getPlayerColor(), list.get(0));
		assertEquals(player2.getPlayerColor(), list.get(1));
		assertEquals(2, list.size());
	}

	@Test
	public void getRedAmmo_initialState_correctOutput() {
		PlayerRep playerRep = new PlayerRep(player1);
		assertEquals(GameConstants.INITIAL_AMMO_PER_AMMO_TYPE + 1, playerRep.getRedAmmo());
	}

	@Test
	public void getYellowAmmo_initialState_correctOutput() {
		PlayerRep playerRep = new PlayerRep(player2);
		assertEquals(GameConstants.INITIAL_AMMO_PER_AMMO_TYPE, playerRep.getYellowAmmo());
	}

	@Test
	public void getBlueAmmo_initialState_correctOutput() {
		PlayerRep playerRep = new PlayerRep(player3);
		assertEquals(GameConstants.INITIAL_AMMO_PER_AMMO_TYPE, playerRep.getBlueAmmo());
	}
}