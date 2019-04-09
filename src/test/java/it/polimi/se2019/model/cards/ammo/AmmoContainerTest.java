package it.polimi.se2019.model.cards.ammo;

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
	public void getAmmo_initialState_shouldGiveInitialAmmo() {
		AmmoContainer ammoContainer = new AmmoContainer();
		for (AmmoType ammoType : AmmoType.values()) {
			assertEquals(AmmoContainer.INITIAL_AMMO, ammoContainer.getAmmo(ammoType));
		}
	}

	@Test
	public void addAmmo_lessThanMaxAmmoAdded_shouldGiveModifiedValue() {
		AmmoContainer ammoContainer = new AmmoContainer();
		final int ammoToAddForTest = 2; // must be less than MAX_AMMO - INITIAL_AMMO!
		for (AmmoType ammoType : AmmoType.values()) {
			ammoContainer.addAmmo(ammoType, ammoToAddForTest);
			assertEquals(AmmoContainer.INITIAL_AMMO + ammoToAddForTest, ammoContainer.getAmmo(ammoType));
		}
	}

	@Test
	public void addAmmo_moreThanMaxAmmoAdded_shouldGiveMaxAmmo() {
		AmmoContainer ammoContainer = new AmmoContainer();
		final int ammoToAddForTest = AmmoContainer.MAX_AMMO + 1;
		for (AmmoType ammoType : AmmoType.values()) {
			ammoContainer.addAmmo(ammoType, ammoToAddForTest);
			assertEquals(AmmoContainer.MAX_AMMO, ammoContainer.getAmmo(ammoType));
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
		final int ammoToRemoveForTest = AmmoContainer.INITIAL_AMMO; // must be less or equal to INITIAL_AMMO!
		for (AmmoType ammoType : AmmoType.values()) {
			ammoContainer.removeAmmo(ammoType, ammoToRemoveForTest);
			assertEquals(AmmoContainer.INITIAL_AMMO - ammoToRemoveForTest, ammoContainer.getAmmo(ammoType));
		}
	}

	@Test
	public void removeAmmo_removedMoreThanCurrent_shouldGiveZero() {
		AmmoContainer ammoContainer = new AmmoContainer();
		final int ammoToRemoveForTest = AmmoContainer.MAX_AMMO + 1;
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