package it.polimi.se2019.model.cards.weapons;

/**
 * This is a simple utility class that collects damages and marks done by a single shot of the weapon.
 *
 * @author Marchingegno
 */
public class DamageAndMarks {
    private int damage;
    private int marks;

    /**
     * The constructor of the class.
     *
     * @param damage the damage that this class contains.
     * @param marks  the marks that this class contains.
     */
    public DamageAndMarks(int damage, int marks) {
        this.damage = damage;
        this.marks = marks;
    }

    /**
     * Add more damage and more marks to this class.
     *
     * @param damageToAdd the damage to add.
     * @param marksToAdd  the marks to add.
     */
    void enrich(int damageToAdd, int marksToAdd) {
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
