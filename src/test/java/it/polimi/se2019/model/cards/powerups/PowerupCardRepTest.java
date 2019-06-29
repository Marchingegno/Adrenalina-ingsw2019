package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PowerupCardRepTest {

	private PowerupCardRep powerupCardRep;
	private PowerupCard powerupCard;

	@Before
	public void setUp() throws Exception {
		powerupCard = new Teleporter(AmmoType.RED_AMMO);
		powerupCardRep = new PowerupCardRep(powerupCard);
	}

	@Test
	public void getAssociatedAmmo_noInput_correctOutput() {
		Assert.assertEquals(powerupCard.getAssociatedAmmo(), powerupCardRep.getAssociatedAmmo());
	}

	@Test
	public void getUseCase_noInput_correctOutput() {
		Assert.assertEquals(powerupCard.getUseCase(), powerupCardRep.getUseCase());
	}

	@Test
	public void toString_noInput_correctOutput() {
		Assert.assertTrue(powerupCardRep.toString().length() >= 3);
	}
}