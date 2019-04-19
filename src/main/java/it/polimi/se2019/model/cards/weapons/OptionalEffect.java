package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;


/**
 * Weapons with one or more optional effect. An optional effect is an enhancement the player can choose to give at the
 * primary fire mode. Some optional effect have an additional ammo cost.
 * @author  Marchingegno
 */
public abstract class OptionalEffect extends WeaponCard {

	public OptionalEffect(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}

	/**
	 * Standard way of handling weapon with optional effects. The weapon may Override this method if it behaves
	 * differently than the standard.
	 * @param flags which options the player has chosen.
	 */
	@Override
	protected void handleFire(Boolean[] flags) {
		List<Player> targetPlayers;
		List<DamageAndMarks> damageAndMarks = new ArrayList<>(standardDamagesAndMarks);

		targetPlayers = primaryFire();

		if (flags[0] == TRUE) {
			optionalEffect1(targetPlayers, damageAndMarks);
		}

		if (flags[1] == TRUE) {
			optionalEffect2(targetPlayers, damageAndMarks);
		}

		dealDamage(targetPlayers, damageAndMarks);
		reset();
	}

	@Override
	public List<Player> primaryFire() {
		return null;
	}

	/**
	 * It contributes to build the list of target players and the list of damages and marks
	 * @param targetPlayers the players targeted.
	 * @param damageAndMarksList the damages and marks dealt to the players.
	 */
	public abstract void optionalEffect1(List<Player> targetPlayers, List<DamageAndMarks> damageAndMarksList);

	/**
	 * It contributes to build the list of target players and the list of damages and marks
	 * @param targetPlayers the players targeted.
	 * @param damageAndMarksList the damages and marks dealt to the players.
	 */
	public abstract void optionalEffect2(List<Player> targetPlayers, List<DamageAndMarks> damageAndMarksList);


}