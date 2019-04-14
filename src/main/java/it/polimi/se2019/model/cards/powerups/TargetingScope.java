package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

/**
 * This class implements the Targeting scope powerup
 * @author MarcerAndrea
 */
public class TargetingScope extends PowerupCard {

	private static final String DESCRIPTION = "TargetingScope description";
	private static final int GIVEN_DAMAGE = 1;

	public TargetingScope(AmmoType associatedAmmo){	super(associatedAmmo, DESCRIPTION);	}

	@Override
	public void activatePowerup(Player activatingPlayer) {
		// TODO can be activated when the client is shooting someone (note: cannot use this to a target that is receiving only marks).
		// TODO ask client which type of ammo to use (must be in the client inventory).
		AmmoType ammoToUse = AmmoType.RED_AMMO; // TODO placeholder, must be choosen ammo type.
		activatingPlayer.getPlayerBoard().getAmmoContainer().removeAmmo(ammoToUse); // use ammo
		Player targetPlayer = activatingPlayer; // TODO placeholder, must be targetPlayer.
		targetPlayer.getPlayerBoard().addDamage(activatingPlayer, GIVEN_DAMAGE);
	}

}