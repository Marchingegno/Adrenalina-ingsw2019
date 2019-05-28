package it.polimi.se2019.model.cards.weapons;

/**
 * This is a simple utility class that collects damages and marks done by a single shot of the weapon.
 * @author Marchingegno
 */
public class DamageAndMarks {
	private int damage;
	private int marks;

	public DamageAndMarks(int damage, int marks) {
		this.damage = damage;
		this.marks = marks;
	}

	public void enrich(DamageAndMarks damageAndMarksToAdd) {
		enrich(damageAndMarksToAdd.getDamage(), damageAndMarksToAdd.getMarks());
	}

	public void enrich(int damageToAdd, int marksToAdd) {
		this.damage += damageToAdd;
		this.marks += marksToAdd;
	}


	public int getDamage() {
		return damage;
	}

	public int getMarks() {
		return marks;
	}

}
