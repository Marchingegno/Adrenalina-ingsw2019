//package it.polimi.se2019.model.player;
//
//import it.polimi.se2019.model.cards.ammo.AmmoType;
//import it.polimi.se2019.model.cards.powerups.Newton;
//import it.polimi.se2019.model.cards.powerups.PowerupCard;
//import it.polimi.se2019.model.cards.weapons.Cyberblade;
//import it.polimi.se2019.model.cards.weapons.WeaponCard;
//import it.polimi.se2019.utils.GameConstants;
//import it.polimi.se2019.utils.exceptions.InventoryFullException;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
///**
// * @author Desno365
// */
//public class PlayerBoardTest {
//
//	private static final Player player1 = new Player("Test 1", 0);
//	private static final Player player2 = new Player("Test 2", 1);
//	private static final Player player3 = new Player("Test 3", 2);
//
//	private PlayerBoard playerBoard;
//
//	@Before
//	public void setUp() throws Exception {
//		playerBoard = new PlayerBoard();
//	}
//
//	@Test
//	public void addDamage_correctInput_correctOutput() {
//		playerBoard.addDamage(player1, 2);
//		playerBoard.addDamage(player2, 1);
//		playerBoard.addDamage(player3, 2);
//		playerBoard.addDamage(player1, 3);
//		assertEquals(8, playerBoard.getDamageBoard().size());
//		assertEquals(player1, playerBoard.getDamageBoard().get(0));
//		assertEquals(player1, playerBoard.getDamageBoard().get(1));
//		assertEquals(player2, playerBoard.getDamageBoard().get(2));
//		assertEquals(player3, playerBoard.getDamageBoard().get(3));
//		assertEquals(player3, playerBoard.getDamageBoard().get(4));
//		assertEquals(player1, playerBoard.getDamageBoard().get(5));
//		assertEquals(player1, playerBoard.getDamageBoard().get(6));
//		assertEquals(player1, playerBoard.getDamageBoard().get(7));
//	}
//
//	@Test (expected = IllegalArgumentException.class)
//	public void addDamage_negativeInput_throwsException() {
//		playerBoard.addDamage(player1, -1);
//	}
//
//	@Test
//	public void addDamage_correctInputWithMarks_shouldBeDead() {
//		playerBoard.addMarks(player1, 2);
//		playerBoard.addDamage(player1, GameConstants.DEATH_DAMAGE - 1);
//		assertTrue(playerBoard.isDead());
//
//		playerBoard.resetBoardAfterDeath();
//		playerBoard.addMarks(player1, 3);
//		playerBoard.addDamage(player1, GameConstants.DEATH_DAMAGE - 1);
//		assertTrue(playerBoard.isDead());
//
//		playerBoard.resetBoardAfterDeath();
//		playerBoard.addMarks(player1, 3);
//		playerBoard.addMarks(player2, 2);
//		playerBoard.addMarks(player3, 1);
//		playerBoard.addDamage(player1, GameConstants.DEATH_DAMAGE - 1);
//		assertTrue(playerBoard.isDead());
//	}
//
//	@Test
//	public void addDamage_correctInputWithMarks_shouldBeAlive() {
//		playerBoard.addMarks(player1, 3);
//		playerBoard.addMarks(player2, 2);
//		playerBoard.addMarks(player3, 1);
//		playerBoard.addDamage(player3, GameConstants.DEATH_DAMAGE - 2);
//		assertFalse(playerBoard.isDead());
//	}
//
//	@Test
//	public void addMarks_correctInput_correctOutput() {
//		playerBoard.addMarks(player1, 2);
//		playerBoard.addMarks(player2, 1);
//		playerBoard.addMarks(player3, 2);
//		playerBoard.addMarks(player1, 3); // can't have more than 3!
//		assertEquals(6, playerBoard.getMarks().size());
//		assertEquals(player1, playerBoard.getMarks().get(0));
//		assertEquals(player1, playerBoard.getMarks().get(1));
//		assertEquals(player2, playerBoard.getMarks().get(2));
//		assertEquals(player3, playerBoard.getMarks().get(3));
//		assertEquals(player3, playerBoard.getMarks().get(4));
//		assertEquals(player1, playerBoard.getMarks().get(5));
//	}
//
//	@Test (expected = IllegalArgumentException.class)
//	public void addMarks_negativeInput_throwsException() {
//		playerBoard.addMarks(player1, -1);
//	}
//
//	@Test
//	public void isDead_moreDamageThanDeathLimitAdded_shouldBeDead() {
//		playerBoard.addDamage(player1, GameConstants.DEATH_DAMAGE);
//		assertTrue(playerBoard.isDead());
//
//		playerBoard.resetBoardAfterDeath();
//		playerBoard.addDamage(player1, GameConstants.DEATH_DAMAGE + 1);
//		assertTrue(playerBoard.isDead());
//
//		playerBoard.resetBoardAfterDeath();
//		playerBoard.addDamage(player1, GameConstants.OVERKILL_DAMAGE + 1);
//		assertTrue(playerBoard.isDead());
//	}
//
//	@Test
//	public void isDead_lessDamageThanDeathLimitAdded_shouldBeAlive() {
//		assertFalse(playerBoard.isDead());
//		playerBoard.addDamage(player1, GameConstants.DEATH_DAMAGE - 1);
//		assertFalse(playerBoard.isDead());
//	}
//
//	@Test
//	public void addPoints_correctInput_shouldBeAsSpecified() {
//		assertEquals(0, playerBoard.getPoints());
//		playerBoard.addPoints(27);
//		assertEquals(27, playerBoard.getPoints());
//		playerBoard.addPoints(2);
//		assertEquals(29, playerBoard.getPoints());
//		playerBoard.addPoints(970);
//		assertEquals(999, playerBoard.getPoints());
//	}
//
//	@Test (expected = IllegalArgumentException.class)
//	public void addPoints_negativeInput_throwsException() {
//		playerBoard.addPoints(-1);
//	}
//
//	@Test
//	public void getNumberOfDeaths_normalState_correctOutput() {
//		assertEquals(0, playerBoard.getNumberOfDeaths());
//		playerBoard.addDamage(player1, 11);
//		playerBoard.resetBoardAfterDeath();
//		assertEquals(1, playerBoard.getNumberOfDeaths());
//		playerBoard.addDamage(player2, 12);
//		playerBoard.resetBoardAfterDeath();
//		assertEquals(2, playerBoard.getNumberOfDeaths());
//	}
//
//	@Test
//	public void addWeapon_correctInput_correctOutput() {
//		WeaponCard cyberblade1 = new Cyberblade("Desc1");
//		WeaponCard cyberblade2 = new Cyberblade("Desc2");
//		WeaponCard cyberblade3 = new Cyberblade("Desc3");
//		assertEquals(0, playerBoard.getWeaponCards().size());
//		playerBoard.addWeapon(cyberblade1);
//		assertEquals(1, playerBoard.getWeaponCards().size());
//		assertEquals(cyberblade1, playerBoard.getWeaponCards().get(0));
//		playerBoard.addWeapon(cyberblade2);
//		assertEquals(2, playerBoard.getWeaponCards().size());
//		playerBoard.addWeapon(cyberblade3);
//		assertEquals(3, playerBoard.getWeaponCards().size());
//		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade1));
//		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade2));
//		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade3));
//	}
//
//	@Test (expected = InventoryFullException.class)
//	public void addWeapon_moreWeaponCardsThanLimit_shouldThrowException() {
//		WeaponCard cyberblade1 = new Cyberblade("Desc1");
//		WeaponCard cyberblade2 = new Cyberblade("Desc2");
//		WeaponCard cyberblade3 = new Cyberblade("Desc3");
//		WeaponCard cyberblade4 = new Cyberblade("Desc4");
//		playerBoard.addWeapon(cyberblade1);
//		playerBoard.addWeapon(cyberblade2);
//		playerBoard.addWeapon(cyberblade3);
//		playerBoard.addWeapon(cyberblade4);
//	}
//
//	@Test
//	public void swapWeapon_correctInput_correctOutput() {
//		WeaponCard cyberblade1 = new Cyberblade("Desc1");
//		WeaponCard cyberblade2 = new Cyberblade("Desc2");
//		WeaponCard cyberblade3 = new Cyberblade("Desc3");
//		WeaponCard cyberblade4 = new Cyberblade("Desc4");
//		playerBoard.addWeapon(cyberblade1);
//		playerBoard.addWeapon(cyberblade2);
//		playerBoard.addWeapon(cyberblade3);
//		playerBoard.swapWeapon(cyberblade4, 1);
//		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade1));
//		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade4));
//		assertNotEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade3));
//		assertEquals(-1, playerBoard.getWeaponCards().indexOf(cyberblade2));
//	}
//
//	@Test (expected = IllegalArgumentException.class)
//	public void swapWeapon_weaponToRemoveNotInInventory_shouldThrowException() {
//		WeaponCard cyberblade1 = new Cyberblade("Desc1");
//		WeaponCard cyberblade2 = new Cyberblade("Desc2");
//		WeaponCard cyberblade3 = new Cyberblade("Desc3");
//		WeaponCard cyberblade4 = new Cyberblade("Desc4");
//		playerBoard.addWeapon(cyberblade1);
//		playerBoard.addWeapon(cyberblade2);
//		playerBoard.addWeapon(cyberblade3);
//		playerBoard.swapWeapon(cyberblade4, -1);
//	}
//
//	@Test
//	public void addPowerup_correctInput_correctOutput() {
//		PowerupCard newton1 = new Newton(AmmoType.RED_AMMO);
//		PowerupCard newton2 = new Newton(AmmoType.BLUE_AMMO);
//		PowerupCard newton3 = new Newton(AmmoType.YELLOW_AMMO);
//		assertEquals(0, playerBoard.getPowerupCards().size());
//		playerBoard.addPowerup(newton1);
//		assertEquals(1, playerBoard.getPowerupCards().size());
//		assertEquals(newton1, playerBoard.getPowerupCards().get(0));
//		playerBoard.addPowerup(newton2);
//		assertEquals(2, playerBoard.getPowerupCards().size());
//		playerBoard.addPowerup(newton3);
//		assertEquals(3, playerBoard.getPowerupCards().size());
//		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton1));
//		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton2));
//		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton3));
//	}
//
//	@Test (expected = InventoryFullException.class)
//	public void addPowerup_morePowerupCardsThanLimit_shouldThrowException() {
//		PowerupCard newton1 = new Newton(AmmoType.RED_AMMO);
//		PowerupCard newton2 = new Newton(AmmoType.BLUE_AMMO);
//		PowerupCard newton3 = new Newton(AmmoType.YELLOW_AMMO);
//		PowerupCard newton4 = new Newton(AmmoType.RED_AMMO);
//		playerBoard.addPowerup(newton1);
//		playerBoard.addPowerup(newton2);
//		playerBoard.addPowerup(newton3);
//		playerBoard.addPowerup(newton4);
//	}
//
//	@Test
//	public void removePowerup_correctInput_correctOutput() {
//		PowerupCard newton1 = new Newton(AmmoType.RED_AMMO);
//		PowerupCard newton2 = new Newton(AmmoType.BLUE_AMMO);
//		PowerupCard newton3 = new Newton(AmmoType.YELLOW_AMMO);
//		PowerupCard newton4 = new Newton(AmmoType.RED_AMMO);
//		playerBoard.addPowerup(newton1);
//		playerBoard.addPowerup(newton2);
//		playerBoard.addPowerup(newton3);
//		playerBoard.removePowerup(1);
//		playerBoard.addPowerup(newton4);
//		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton1));
//		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton4));
//		assertNotEquals(-1, playerBoard.getPowerupCards().indexOf(newton3));
//		assertEquals(-1, playerBoard.getPowerupCards().indexOf(newton2));
//	}
//
//	@Test
//	public void getAmmoContainer_initialState_shouldGiveInitialContainer() {
//		for (AmmoType ammoType : AmmoType.values()) {
//			assertEquals(GameConstants.INITIAL_AMMO_PER_AMMO_TYPE, playerBoard.getAmmoContainer().getAmmo(ammoType));
//		}
//	}
//}