package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.utils.Pair;

/**
 * This is a simple utility class that collects damages and marks done by a single shot of the weapon.
 * @author Marchingegno
 */
public class DamageAndMarks extends Pair<Integer, Integer> {

	public DamageAndMarks(int damage, int marks) {
		super(damage,marks);
	}


	public int getDamage() {
		return getFirst();
	}

	public int getMarks() {
		return getSecond();
	}




}
