package it.polimi.se2019.model;

/**
 * Contains all the constants of the game.
 * @author Desno365
 */
public class GameConstants {

	public static final int MAX_PLAYERS = 5;
	public static final int DEATH_DAMAGE = 11;
	public static final int OVERKILL_DAMAGE = 12;
	public static final int MAX_MARKS_PER_PLAYER = 3;
	public static final int MAX_WEAPON_CARDS_PER_PLAYER = 3;
	public static final int MAX_POWERUP_CARDS_PER_PLAYER = 3;
	public static final int MAX_AMMO_PER_AMMO_TYPE = 3;
	public static final int INITIAL_AMMO_PER_AMMO_TYPE = 1;


	/**
	 * Since it's an utility class it can't be instantiated.
	 */
	private GameConstants() {
		throw new IllegalStateException("Cannot create an instance of this utility class.");
	}
}
