package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;

import java.util.List;

import static java.lang.Boolean.FALSE;


/**
 * Weapons with one or more optional effect. An optional effect is an enhancement the player can choose to give at the
 * primary fire mode. Some optional effect have an additional ammo cost.
 * @author  Marchingegno
 */
public abstract class OptionalEffect extends WeaponCard {

	private Boolean optional1Active;
	private Boolean optional2Active;

	public OptionalEffect(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		reset();
	}

	/**
	 * Standard way of handling weapon with optional effects. The weapon may Override this method if it behaves
	 * differently than the standard.
	 * @return
	 */
	@Override
	public Pair handleFire() {

	}

	@Override
	public void registerChoice(int choice) {
		switch (choice){
			case 1:
				optional1Active = true;
				break;
			case 2:
				optional2Active = true;
				break;
			case 3:
				optional1Active = true;
				optional2Active = true;
				break;
			default:
		}
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

	void optionalReset(){
		this.optional2Active = FALSE;
		this.optional1Active = FALSE;
	}

	@Override
	void reset() {
		super.reset();
		optionalReset();

	}
}