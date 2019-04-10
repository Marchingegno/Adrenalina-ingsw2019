package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.model.GameConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AmmoContainerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getAmmo_noInput_shouldGiveInitialAmmo() {
		AmmoContainer ammoContainer = new AmmoContainer();
		for (AmmoType ammoType : AmmoType.values()) {
			assertEquals(GameConstants.INITIAL_AMMO_PER_AMMO_TYPE, ammoContainer.getAmmo(ammoType));
		}
	}

	@Test
	public void addAmmo_lessThanMaxAmmoAdded_shouldGiveModifiedValue() {
		AmmoContainer ammoContainer = new AmmoContainer();
		final int ammoToAddForTest = 2; // must be less than MAX_AMMO_PER_AMMO_TYPE - INITIAL_AMMO_PER_AMMO_TYPE!
		for (AmmoType ammoType : AmmoType.values()) {
			ammoContainer.addAmmo(ammoType, ammoToAddForTest);
			assertEquals(GameConstants.INITIAL_AMMO_PER_AMMO_TYPE + ammoToAddForTest, ammoContainer.getAmmo(ammoType));
		}
	}

	@Test
	public void addAmmo_singleAmmoAdded_shouldGiveModifiedValue() {
		AmmoContainer ammoContainer = new AmmoContainer();
		ammoContainer.addAmmo(AmmoType.values()[0]);
		ammoContainer.addAmmo(AmmoType.values()[0]);
		assertEquals(GameConstants.INITIAL_AMMO_PER_AMMO_TYPE + 2, ammoContainer.getAmmo(AmmoType.values()[0]));
	}

	@Test
	public void addAmmo_moreThanMaxAmmoAdded_shouldGiveMaxAmmo() {
		AmmoContainer ammoContainer = new AmmoContainer();
		final int ammoToAddForTest = GameConstants.MAX_AMMO_PER_AMMO_TYPE + 1;
		for (AmmoType ammoType : AmmoType.values()) {
			ammoContainer.addAmmo(ammoType, ammoToAddForTest);
			assertEquals(GameConstants.MAX_AMMO_PER_AMMO_TYPE, ammoContainer.getAmmo(ammoType));
		}
	}

	@Test (expected = IllegalArgumentException.class)
	public void addAmmo_negativeNumberOfAmmoGiven_shouldThrowException() {
		AmmoContainer ammoContainer = new AmmoContainer();
		ammoContainer.addAmmo(AmmoType.values()[0], -1);
	}

	@Test
	public void removeAmmo_removedLessThanCurrent_shouldGiveModifiedValue() {
		AmmoContainer ammoContainer = new AmmoContainer();
		final int ammoToRemoveForTest = GameConstants.INITIAL_AMMO_PER_AMMO_TYPE; // must be less or equal to INITIAL_AMMO_PER_AMMO_TYPE!
		for (AmmoType ammoType : AmmoType.values()) {
			ammoContainer.removeAmmo(ammoType, ammoToRemoveForTest);
			assertEquals(GameConstants.INITIAL_AMMO_PER_AMMO_TYPE - ammoToRemoveForTest, ammoContainer.getAmmo(ammoType));
		}
	}

	@Test
	public void removeAmmo_removedMoreThanCurrent_shouldGiveZero() {
		AmmoContainer ammoContainer = new AmmoContainer();
		final int ammoToRemoveForTest = GameConstants.MAX_AMMO_PER_AMMO_TYPE + 1;
		for (AmmoType ammoType : AmmoType.values()) {
			ammoContainer.removeAmmo(ammoType, ammoToRemoveForTest);
			assertEquals(0, ammoContainer.getAmmo(ammoType));
		}
	}

	@Test (expected = IllegalArgumentException.class)
	public void removeAmmo_negativeNumberOfAmmoGiven_shouldThrowException() {
		AmmoContainer ammoContainer = new AmmoContainer();
		ammoContainer.removeAmmo(AmmoType.values()[0], -1);
	}
}