package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.network.message.Message;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

public final class Cyberblade extends OptionalEffect{
	private final int PRIMARY_DAMAGE = 2;
	private final int PRIMARY_MARKS = 0;
	private final int OPTIONAL2_DAMAGE = 2;
	private final int OPTIONAL2_MARKS = 0;
	private final int MOVE_DISTANCE = 1;


	public Cyberblade(String description, List<AmmoType> reloadPrice) {
		super(description, reloadPrice);
		this.standardDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.standardDamagesAndMarks.add(new DamageAndMarks(OPTIONAL2_DAMAGE, OPTIONAL2_MARKS));
		this.moveDistance = MOVE_DISTANCE;
	}


	@Override
	protected void handleFire(Message message) {
	}

	@Override
	public void getAvailableOptions() {
		super.getAvailableOptions();

	}

	@Override
	public void optionalEffect1(List<Player> targetPlayers, List<DamageAndMarks> damageAndMarksList) {
		moveActive = TRUE;
	}

	@Override
	public void optionalEffect2(List<Player> targetPlayers, List<DamageAndMarks> damageAndMarksList) {
		//Asks the player which target to fire at.
		//targetPlayers.add(a Player)
	}


	@Override
	public List<Player> primaryFire () {
		List<Player> targetPlayers = new ArrayList<>();
		List<Player> targettablePlayers = getPrimaryTargets();
		//Asks the player which target to fire at
		//targetPlayers.add(this player)
		return targetPlayers;
	}

	@Override
	public List<Player> getPrimaryTargets() {
		//gameMap.getPlayersFromDistance(distance)
		return null;
	}
}