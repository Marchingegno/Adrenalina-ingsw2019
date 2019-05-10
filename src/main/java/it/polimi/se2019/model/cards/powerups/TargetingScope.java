package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

/**
 * This class implements the Targeting scope powerup
 *
 * @author MarcerAndrea
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
		super(associatedAmmo, DESCRIPTION);
	}

	/**
	 * Activates the powerup.
	 *
	 * @param activatingPlayer player who as activated the powerup.
	 */
	@Override
	public void activatePowerup(Player activatingPlayer) {
		// TODO can be activated when the client is shooting someone (note: cannot use this to a target that is receiving only marks).
		// TODO ask client which type of ammo to use (must be in the client inventory).
		AmmoType ammoToUse = AmmoType.RED_AMMO; // TODO placeholder, must be choosen ammo type.
		activatingPlayer.getPlayerBoard().getAmmoContainer().removeAmmo(ammoToUse); // use ammo
		Player targetPlayer = activatingPlayer; // TODO placeholder, must be targetPlayer.
		targetPlayer.getPlayerBoard().addDamage(activatingPlayer, GIVEN_DAMAGE);
	}

	@Override
	public String toString() {
		return "Targeting Scope";
	}

}