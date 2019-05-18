package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.Representable;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class that defines the structure of a weapon card. Every subtype of weapon must extend this.
 * The weapons are handled with the help of the **Strategy Pattern*. Each weapon has a predetermined number of
 * "advancement steps", that represent the current state of advancement of the firing process of the weapon.
 * Each time the controller wishes to continue the process of firing of the weapon, it must call the method **handleFire**;
 * This will return a "Question" {@link Pair} that the controller must send to the Virtual View of the shooting player.
 * However, before doing that, the controller will need to check if the weapon has finished the firing process and has
 * already dealt damage; this can be done calling {@link #doneFiring()} that returns a boolean that is true when the
 * current step is the maximum step for the current firing mode. The controller will also need to {@link #reset()} the weapon
 * if it has finished firing. The method {@link #reset()} will deload and reset the weapon to its original state.
 * @author Marchingegno
 */
public abstract class WeaponCard extends Card implements Representable {

	private int currentStep; //Advancement step of the weapon.
	private Player owner;
	private boolean loaded;
	private GameBoard gameBoard;
	private final List<AmmoType> reloadPrice;
	int moveDistance; //Standard move for relocation of the player.
	int maximumSteps; //Maximum advancement steps.
	boolean relocationDone; //If the player has already been relocated.
	boolean enemyRelocationDone; //If the enemies have already been relocated. Not sure if this is useful.
	List<DamageAndMarks> standardDamagesAndMarks;
	List<Player> currentTargets;
	Player target;
	int PRIMARY_DAMAGE;
	int PRIMARY_MARKS;


	public WeaponCard(String weaponName, String description, List<AmmoType> reloadPrice) {
		super(weaponName, description);
		this.currentStep = 0;
		this.owner = null;
		this.loaded = true;
		this.reloadPrice = reloadPrice;
	}

	public boolean doneFiring(){
		return getCurrentStep() == getMaximumSteps();
	}

	GameMap getGameMap() {
		return gameBoard.getGameMap();
	}

	public void setGameBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	List<Player> getAllPlayers(){
		return gameBoard.getPlayers();
	}

	/**
	 *Returns the owner of the weapon.
	 * @return the owner of the weapon.
	 */
	public Player getOwner(){
		return owner;
	}

	/**
	 * Returns the reload price of this weapon.
	 * @return Reload price of this weapon.
	 */
	public List<AmmoType> getReloadPrice() {
		return new ArrayList<>(reloadPrice);
	}

	/**
	 * Returns the grab price for this weapon. It consists of the reload price minus the first occurrence.
	 * @return grab price for this weapon.
	 */
	public List<AmmoType> getGrabPrice(){
		return new ArrayList<>(reloadPrice.subList(1, reloadPrice.size() - 1));
	}

	/**
	 * Returns whether or not the weapon is loaded.
	 * @return whether or not the weapon is loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Called by the controller when this weapon is grabbed by a player.
	 * @param grabbingPlayer the grabbing player.
	 */
	public void setOwner(Player grabbingPlayer){
		this.owner = grabbingPlayer;
	}


	/**
	 * Loads the weapon.
	 */
	public void load() {
		//TODO: Implement
		this.loaded = true;
	}

	/**
	 * Creates a "Question" Pair to be passed to the view.
	 * @return the "Question" Pair.
	 */
	public Pair askingPair(){
		return null;
	}


	/**
	 * Handles the firing process of the weapon based on the currentStep and the choice of the player.
	 * @return the "Question" Pair to be passed to the view, or **null** if the weapon has finished firing.
	 * @param choice the choice of the player.
	 */
	public abstract Pair handleFire(int choice);

	/**
	 * This method will be called by the damage-dealer methods of the weapons.
	 * The two arrays are ordered in such a way that the i-th playerToShoot will be dealt the i-th damageAndMark.
	 * This method does NOT deload the weapon.
	 * This method does check only the first array's size, so it can happen that playersToShoot is shorter than
	 * damagesAndMarks.
	 * @param playersToShoot the array of players that will receive damage and/or marks.
	 * @param damagesAndMarks the damage and marks for each player.
	 */
	protected void dealDamage(List<Player> playersToShoot, List<DamageAndMarks> damagesAndMarks){
		for (int i = 0; i < playersToShoot.size(); i++) {
			if(playersToShoot.get(i) != null){
				playersToShoot.get(i).getPlayerBoard().addDamage(owner, damagesAndMarks.get(i).getDamage());
				playersToShoot.get(i).getPlayerBoard().addMarks(owner, damagesAndMarks.get(i).getMarks());
			}
		}
	}

	protected void dealDamage(Player playerToShoot, List<DamageAndMarks> damagesAndMarks){
		playerToShoot.getPlayerBoard().addDamage(owner, damagesAndMarks.get(0).getDamage());
		playerToShoot.getPlayerBoard().addMarks(owner, damagesAndMarks.get(0).getMarks());
	}


	/**
	 * Primary method of firing of the weapon.
	 */
	protected abstract void primaryFire();

	/**
	 * Handles the primary fire mode of the weapon.
	 * This will be called if currentStep is at least 2.
	 *
	 * @param choice the choice of the player.
	 * @return the "Question" Pair.
	 */
	abstract Pair handlePrimaryFire(int choice);

//	public void getAvailableOptions(){
//		Utils.logInfo(getDescription());
//	}

	/**
	 * Get the targets of the primary mode of fire for this weapon.
	 * @return the targettable players of the primary mode of fire.
	 */
	public abstract List<Player> getPrimaryTargets();

	/**
	 * Deloads the weapon and reset eventually modified parameters.
	 */
	public void reset(){
		this.currentStep = 0;
		this.loaded = false;
		this.currentTargets = null;
		this.relocationDone = false;
		this.enemyRelocationDone = false;
		this.currentTargets = new ArrayList<>();
		this.target = null;
	}

	List<DamageAndMarks> getStandardDamagesAndMarks() {
		return standardDamagesAndMarks;
	}

	int getMoveDistance() {
		return moveDistance;
	}

	int getCurrentStep() {
		return currentStep;
	}

	int getMaximumSteps() {
		return maximumSteps;
	}

	/**
	 * Increments the advancement step of the weapon.
	 */
	void incrementStep(){
		if(currentStep == maximumSteps)
			throw new IllegalStateException("Trying to increment steps already at maximum.");
		currentStep++;
	}

	/**
	 * Builds a "Question" Pair that asks which player to target.
	 * @param targets the target players to choose from.
	 * @return the "Question" Pair.
	 */
	public static Pair getTargetPlayersQnO(List<Player> targets){
		String question = "Which of the following players do you want to target?";
		List<String> options = new ArrayList<>();
		targets.forEach(target-> options.add(target.getPlayerName()));
		return new Pair<>(question, options);
	}

	/**
	 * Builds a "Question" Pair that ask in which coordinate to move the enemy player.
	 * @param targetPlayer the player that will be moved.
	 * @param coordinates the list of coordinates to choose from.
	 * @return the "Question" Pair.
	 */
	public static Pair getMovingTargetEnemyCoordinatesQnO(Player targetPlayer, List<Coordinates> coordinates){
		String question = "Where do you want to move " + targetPlayer.getPlayerName() + "?";
		List<String> options = new ArrayList<>();
		coordinates.forEach(coordinate -> options.add(coordinate.toString()));
		return new Pair<>(question, options);
	}

	/**
	 * Builds a "Question" Pair that asks in which coordinate to fire at.
	 * @param coordinates the list of coordinates to choose from.
	 * @return the "Question" Pair.
	 */
	public static Pair getTargetCoordinatesQnO(List<Coordinates> coordinates){
		String question = "Where do you want to fire?";
		List<String> options = new ArrayList<>();
		coordinates.forEach(coordinate -> options.add(coordinate.toString()));
		return new Pair<>(question, options);
	}

	public static Pair getCardinalQnO(){
		String question = "In which direction do you wish to fire?";
		List<String> options = 	Arrays.stream(CardinalDirection.values()).map(Enum::toString).collect(Collectors.toList());
		return new Pair<>(question,options);
	}

	protected void relocateEnemy(Player enemy, Coordinates coordinates){
		getGameMap().movePlayerTo(enemy, coordinates);
	}

	protected void relocateOwner(Coordinates coordinates){
		getGameMap().movePlayerTo(getOwner(), coordinates);
	}

	@Override
	public Representation getRep() {
		return new WeaponRep(this);
	}
}