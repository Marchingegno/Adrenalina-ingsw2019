package it.polimi.se2019.model.cards.ammo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AmmoCardTest {

	private List<AmmoType> ammoInTheCard = new ArrayList<>();

	@Before
	public void setUp() {
		ammoInTheCard.add(AmmoType.RED_AMMO);
		ammoInTheCard.add(AmmoType.BLUE_AMMO);
	}

	@Test
	public void hasPowerup_standardInitialization_correctOutput() {
		AmmoCard ammoCardWithPowerup = new AmmoCard(ammoInTheCard, true, "r_b_p", null);
		AmmoCard ammoCardWithoutPowerup = new AmmoCard(ammoInTheCard, false, "r_b_p", null);
		assertTrue(ammoCardWithPowerup.hasPowerup());
		assertFalse(ammoCardWithoutPowerup.hasPowerup());
	}

	@Test
	public void getAmmo_standardInitialization_correctOutput() {
		AmmoCard ammoCard = new AmmoCard(ammoInTheCard, true, "r_b_p", null);
		assertEquals(ammoInTheCard, ammoCard.getAmmo());
	}

	@Test
	public void getAmmo_changeContent_shouldBeImmutable() {
		AmmoCard ammoCard = new AmmoCard(ammoInTheCard, true, "r_b_p", null);
		List<AmmoType> ammoTypeList = ammoCard.getAmmo();
		ammoTypeList.add(AmmoType.RED_AMMO);
		assertNotEquals(ammoInTheCard, ammoTypeList);
		ammoInTheCard.add(AmmoType.RED_AMMO);
		assertNotEquals(ammoCard.getAmmo(), ammoTypeList);
	}

	@Test
	public void getRep_standardInitialization_correctRepresentation() {
		AmmoCard ammoCard = new AmmoCard(ammoInTheCard, true, "r_b_p", null);
		AmmoCardRep ammoCardRep = (AmmoCardRep) ammoCard.getRep();

		assertEquals(ammoCard.hasPowerup(), ammoCardRep.hasPowerup());
		assertEquals(ammoCard.getAmmo(), ammoCardRep.getAmmo());
	}

	@Test
	public void getDescription_noInput_sameDescription() {
		AmmoCard ammoCard = new AmmoCard(ammoInTheCard, true, "r_b_p", null);
		AmmoCardRep ammoCardRep = (AmmoCardRep) ammoCard.getRep();
		assertEquals(ammoCard.getCardDescription(), ammoCardRep.getCardDescription());
	}
}