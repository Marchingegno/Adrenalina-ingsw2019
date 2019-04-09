package it.polimi.se2019.model.player;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.Newton;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.weapons.Cyberblade;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerBoardTest {

	private Player player1;
	private Player player2;
	private Player player3;

	@Before
	public void setUp() throws Exception {
		player1 = new Player("Test 1", 0);
		player2 = new Player("Test 2", 1);
		player3 = new Player("Test 3", 2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void addDamage_correctInput_noOutput() {
		PlayerBoard playerBoard = new PlayerBoard();
		playerBoard.addDamage(player1, 2);
		playerBoard.addDamage(player2, 1);
		playerBoard.addDamage(player3, 2);
		playerBoard.addDamage(player1, 3);
	}

	@Test (expected = IllegalArgumentException.class)
	public void addDamage_negativeInput_throwsException() {
		PlayerBoard playerBoard = new PlayerBoard();
		playerBoard.addDamage(player1, -1);
	}

	@Test
	public void addDamage_correctInputWithMarks_shouldBeDead() {
		PlayerBoard playerBoard = new PlayerBoard();
		playerBoard.addMarks(player1, 2);
		playerBoard.addDamage(player1, PlayerBoard.DEATH_DAMAGE - 1);
		assertTrue(playerBoard.isDead());

		playerBoard = new PlayerBoard();
		playerBoard.addMarks(player1, 3);
		playerBoard.addDamage(player1, PlayerBoard.DEATH_DAMAGE - 1);
		assertTrue(playerBoard.isDead());

		playerBoard = new PlayerBoard();
		playerBoard.addMarks(player1, 3);
		playerBoard.addMarks(player2, 2);
		playerBoard.addMarks(player3, 1);
		playerBoard.addDamage(player1, PlayerBoard.DEATH_DAMAGE - 1);
		assertTrue(playerBoard.isDead());
	}

	@Test
	public void addDamage_correctInputWithMarks_shouldBeAlive() {
		PlayerBoard playerBoard = new PlayerBoard();
		playerBoard.addMarks(player1, 2);
		playerBoard.addDamage(player1, PlayerBoard.DEATH_DAMAGE - 3);
		assertFalse(playerBoard.isDead());

		playerBoard = new PlayerBoard();
		playerBoard.addMarks(player1, 3);
		playerBoard.addDamage(player1, 1);
		assertFalse(playerBoard.isDead());

		playerBoard = new PlayerBoard();
		playerBoard.addMarks(player1, 3);
		playerBoard.addMarks(player2, 2);
		playerBoard.addMarks(player3, 1);
		playerBoard.addDamage(player3, PlayerBoard.DEATH_DAMAGE - 2);
		assertFalse(playerBoard.isDead());
	}

	@Test
	public void addMarks_correctInput_noOutput() {
		PlayerBoard playerBoard = new PlayerBoard();
		playerBoard.addMarks(player1, 2);
		playerBoard.addMarks(player2, 1);
		playerBoard.addMarks(player3, 2);
		playerBoard.addMarks(player1, 3);
	}

	@Test (expected = IllegalArgumentException.class)
	public void addMarks_negativeInput_throwsException() {
		PlayerBoard playerBoard = new PlayerBoard();
		playerBoard.addMarks(player1, -1);
	}

	@Test
	public void isDead_moreDamageThanDeathLimitAdded_shouldBeDead() {
		PlayerBoard playerBoard = new PlayerBoard();
		playerBoard.addDamage(player1, PlayerBoard.DEATH_DAMAGE);
		assertTrue(playerBoard.isDead());

		playerBoard = new PlayerBoard();
		playerBoard.addDamage(player1, PlayerBoard.DEATH_DAMAGE + 1);
		assertTrue(playerBoard.isDead());
	}

	@Test
	public void isDead_lessDamageThanDeathLimitAdded_shouldBeAlive() {
		PlayerBoard playerBoard = new PlayerBoard();
		playerBoard.addDamage(player1, 0);
		assertFalse(playerBoard.isDead());

		playerBoard = new PlayerBoard();
		playerBoard.addDamage(player1, PlayerBoard.DEATH_DAMAGE - 1);
		assertFalse(playerBoard.isDead());
	}

	@Test
	public void addPoints() {
	}

	@Test
	public void addPoints_correctInput_shouldBeAsSpecified() {
		PlayerBoard playerBoard = new PlayerBoard();
		playerBoard.addPoints(27);
		assertEquals(27, playerBoard.getPoints());
		playerBoard.addPoints(2);
		assertEquals(29, playerBoard.getPoints());

		playerBoard = new PlayerBoard();
		assertEquals(0, playerBoard.getPoints());
		playerBoard.addPoints(999);
		assertEquals(999, playerBoard.getPoints());
	}

	@Test (expected = IllegalArgumentException.class)
	public void addPoints_negativeInput_throwsException() {
		PlayerBoard playerBoard = new PlayerBoard();
		playerBoard.addPoints(-1);
	}

	@Test
	public void score() {
		// TODO
	}

	@Test
	public void getNumberOfDeaths_normalState_correctOutput() {
		PlayerBoard playerBoard = new PlayerBoard();
		assertEquals(0, playerBoard.getNumberOfDeaths());
		playerBoard.addDamage(player1, 11);
		playerBoard.score();
		assertEquals(1, playerBoard.getNumberOfDeaths());
		playerBoard.addDamage(player2, 12);
		playerBoard.score();
		assertEquals(2, playerBoard.getNumberOfDeaths());
		playerBoard.addDamage(player2, 2);
		playerBoard.score();
		assertEquals(2, playerBoard.getNumberOfDeaths());
	}

	@Test
	public void addWeapon_correctInput_correctOutput() {
		PlayerBoard playerBoard = new PlayerBoard();
		WeaponCard cyberblade = new Cyberblade("Desc1");
		assertEquals(0, playerBoard.getWeaponCards().size());
		playerBoard.addWeapon(cyberblade);
		assertEquals(cyberblade, playerBoard.getWeaponCards().get(0));

		playerBoard = new PlayerBoard();
		WeaponCard cyberblade1 = new Cyberblade("Desc1");
		WeaponCard cyberblade2 = new Cyberblade("Desc2");
		WeaponCard cyberblade3 = new Cyberblade("Desc3");
		assertEquals(0, playerBoard.getWeaponCards().size());
		playerBoard.addWeapon(cyberblade1);
		assertEquals(1, playerBoard.getWeaponCards().size());
		playerBoard.addWeapon(cyberblade2);
		assertEquals(2, playerBoard.getWeaponCards().size());
		playerBoard.addWeapon(cyberblade3);
		assertEquals(3, playerBoard.getWeaponCards().size());
		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade1));
		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade2));
		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade3));
	}

	@Test (expected = InventoryFullException.class)
	public void addWeapon_moreWeaponCardsThanLimit_shouldThrowException() {
		PlayerBoard playerBoard = new PlayerBoard();
		WeaponCard cyberblade1 = new Cyberblade("Desc1");
		WeaponCard cyberblade2 = new Cyberblade("Desc2");
		WeaponCard cyberblade3 = new Cyberblade("Desc3");
		WeaponCard cyberblade4 = new Cyberblade("Desc4");
		playerBoard.addWeapon(cyberblade1);
		playerBoard.addWeapon(cyberblade2);
		playerBoard.addWeapon(cyberblade3);
		playerBoard.addWeapon(cyberblade4);
	}

	@Test
	public void swapWeapon_correctInput_correctOutput() {
		PlayerBoard playerBoard = new PlayerBoard();
		WeaponCard cyberblade1 = new Cyberblade("Desc1");
		WeaponCard cyberblade2 = new Cyberblade("Desc2");
		WeaponCard cyberblade3 = new Cyberblade("Desc3");
		WeaponCard cyberblade4 = new Cyberblade("Desc4");
		playerBoard.addWeapon(cyberblade1);
		playerBoard.addWeapon(cyberblade2);
		playerBoard.addWeapon(cyberblade3);
		playerBoard.swapWeapon(cyberblade4, cyberblade2);
		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade1));
		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade4));
		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade3));
		assertEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade2));
	}

	@Test (expected = IllegalArgumentException.class)
	public void swapWeapon_weaponToRemoveNotInInventory_shouldThrowException() {
		PlayerBoard playerBoard = new PlayerBoard();
		WeaponCard cyberblade1 = new Cyberblade("Desc1");
		WeaponCard cyberblade2 = new Cyberblade("Desc2");
		WeaponCard cyberblade3 = new Cyberblade("Desc3");
		WeaponCard cyberblade4 = new Cyberblade("Desc4");
		WeaponCard cyberblade5 = new Cyberblade("Desc4");
		playerBoard.addWeapon(cyberblade1);
		playerBoard.addWeapon(cyberblade2);
		playerBoard.addWeapon(cyberblade3);
		playerBoard.swapWeapon(cyberblade4, cyberblade5);
	}

	@Test
	public void addPowerup_correctInput_correctOutput() {
		PlayerBoard playerBoard = new PlayerBoard();
		PowerupCard newton = new Newton(AmmoType.RED_AMMO, "Desc1");
		assertEquals(0, playerBoard.getPowerupCards().size());
		playerBoard.addPowerup(newton);
		assertEquals(newton, playerBoard.getPowerupCards().get(0));

		playerBoard = new PlayerBoard();
		PowerupCard newton1 = new Newton(AmmoType.RED_AMMO, "Desc1");
		PowerupCard newton2 = new Newton(AmmoType.BLUE_AMMO, "Desc2");
		PowerupCard newton3 = new Newton(AmmoType.YELLOW_AMMO, "Desc3");
		assertEquals(0, playerBoard.getPowerupCards().size());
		playerBoard.addPowerup(newton1);
		assertEquals(1, playerBoard.getPowerupCards().size());
		playerBoard.addPowerup(newton2);
		assertEquals(2, playerBoard.getPowerupCards().size());
		playerBoard.addPowerup(newton3);
		assertEquals(3, playerBoard.getPowerupCards().size());
		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton1));
		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton2));
		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton3));
	}

	@Test (expected = InventoryFullException.class)
	public void addPowerup_morePowerupCardsThanLimit_shouldThrowException() {
		PlayerBoard playerBoard = new PlayerBoard();
		PowerupCard newton1 = new Newton(AmmoType.RED_AMMO, "Desc1");
		PowerupCard newton2 = new Newton(AmmoType.BLUE_AMMO, "Desc2");
		PowerupCard newton3 = new Newton(AmmoType.YELLOW_AMMO, "Desc3");
		PowerupCard newton4 = new Newton(AmmoType.RED_AMMO, "Desc4");
		playerBoard.addPowerup(newton1);
		playerBoard.addPowerup(newton2);
		playerBoard.addPowerup(newton3);
		playerBoard.addPowerup(newton4);
	}

	@Test
	public void removePowerup_correctInput_correctOutput() {
		PlayerBoard playerBoard = new PlayerBoard();
		PowerupCard newton1 = new Newton(AmmoType.RED_AMMO, "Desc1");
		PowerupCard newton2 = new Newton(AmmoType.BLUE_AMMO, "Desc2");
		PowerupCard newton3 = new Newton(AmmoType.YELLOW_AMMO, "Desc3");
		PowerupCard newton4 = new Newton(AmmoType.RED_AMMO, "Desc4");
		playerBoard.addPowerup(newton1);
		playerBoard.addPowerup(newton2);
		playerBoard.addPowerup(newton3);
		playerBoard.removePowerup(newton2);
		playerBoard.addPowerup(newton4);
		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton1));
		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton4));
		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton3));
		assertEquals(-1, playerBoard.getPowerupCards().indexOf(newton2));
	}
}