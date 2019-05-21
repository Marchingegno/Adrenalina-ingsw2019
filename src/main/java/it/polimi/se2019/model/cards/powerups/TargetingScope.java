package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.QuestionContainer;

/**
 * This class implements the Targeting scope powerup
 *
 * @author MarcerAndrea
 * @author Desno365
 */
public class TargetingScope extends PowerupCard {

	private static final String DESCRIPTION =
			"You may play this card when you are dealing\n" +
					"damage to one or more targets. Pay 1 ammo\n" +
					"cube of any color. Choose 1 of those targets\n" +
					"and give it an extra point of damage. Note: You\n" +
					"cannot use this to do 1 damage to a target that\n" +
					"is receiving only marks.";
	private static final int GIVEN_DAMAGE = 1;

	public TargetingScope(AmmoType associatedAmmo) {
		super("Targeting scope", associatedAmmo, DESCRIPTION, PowerupUseCaseType.ON_SHOOT);
	}


	@Override
	public QuestionContainer doPowerupStep(Message answer) {
		// TODO ask client which type of ammo to use (must be in the client inventory).
		AmmoType ammoToUse = AmmoType.RED_AMMO; // TODO placeholder, must be choosen ammo type.
		getOwnerPlayer().getPlayerBoard().getAmmoContainer().removeAmmo(ammoToUse); // use ammo
		Player targetPlayer = getOwnerPlayer(); // TODO placeholder, must be targetPlayer.
		targetPlayer.getPlayerBoard().addDamage(getOwnerPlayer(), GIVEN_DAMAGE);
		return null;
	}

	@Override
	public boolean canBeActivated() {
		boolean hasOneAmmo = false;
		for(AmmoType ammoType : AmmoType.values()) {
			if(getOwnerPlayer().getPlayerBoard().getAmmoContainer().getAmmo(ammoType) > 0) {
				hasOneAmmo = true;
				break;
			}
		}

		boolean weaponHasDamage = true; // TODO

		return hasOneAmmo && weaponHasDamage;
	}

	@Override
	public String toString() {
		return "Targeting Scope";
	}

}