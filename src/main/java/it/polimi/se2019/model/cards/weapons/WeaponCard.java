package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.ActivableCard;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class that defines the structure of a weapon card. Every subtype of weapon must extend this.
 * The weapons are handled with the help of the **Strategy Pattern*. Each weapon has a predetermined number of
 * "advancement steps", that represent the current state of advancement of the firing process of the weapon.
 * Each time the controller wishes to continue the process of firing of the weapon, it must call the method **handleFire**;
 * This will return a {@link QuestionContainer} that the controller must send to the Virtual View of the shooting player.
 * However, before doing that, the controller will need to check if the weapon has finished the firing process and has
 * already dealt damage; this can be done calling {@link #doneFiring()} that returns a boolean that is true when the
 * current step is the maximum step for the current firing mode. The controller will also need to {@link #reset()} the weapon
 * if it has finished firing. The method {@link #reset()} will deload and reset the weapon to its original state.
 * @author Marchingegno
 */
public abstract class WeaponCard extends ActivableCard {

	private boolean loaded = true;
	int maximumSteps; //Maximum advancement steps.
	boolean relocationDone; //If the player has already been relocated.
	boolean enemyRelocationDone; //If the enemies have already been relocated. Not sure if this is useful.
	List<DamageAndMarks> standardDamagesAndMarks;
	List<Player> currentTargets;
	Player target;
	private boolean doneFiring = false;

	private final ArrayList<AmmoType> reloadPrice;
	private final int moveDistance; //Standard move for relocation of the player.
	private final int primaryDamage;
	private final int primaryMarks;


	/**
	 * @deprecated
	 */
	public WeaponCard(String weaponName, String description, List<AmmoType> reloadPrice, final int primaryMarks, final int primaryDamage, final int moveDistance) {
		super(weaponName, description);
		this.reloadPrice = new ArrayList<>(reloadPrice);
		this.primaryDamage = primaryDamage;
		this.primaryMarks = primaryMarks;
		this.moveDistance = moveDistance;
	}

	/**
	 * @deprecated
	 */
	public WeaponCard(String weaponName, String description, List<AmmoType> reloadPrice, final int primaryDamage, final int primaryMarks) {
		super(weaponName, description);
		this.reloadPrice = new ArrayList<>(reloadPrice);
		this.primaryDamage = primaryDamage;
		this.primaryMarks = primaryMarks;
		this.moveDistance = 0;
	}

	/**
	 * @deprecated
	 */
	public WeaponCard(String weaponName, String description, List<AmmoType> reloadPrice, final int primaryDamage) {
		super(weaponName, description);
		this.reloadPrice = new ArrayList<>(reloadPrice);
		this.primaryDamage = primaryDamage;
		this.primaryMarks = 0;
		this.moveDistance = 0;
	}

	public WeaponCard(JsonObject parameters) {
		super(parameters.get("name").getAsString(), parameters.get("description").getAsString());

		this.reloadPrice = new ArrayList<>();

		for (JsonElement ammoPrice : parameters.getAsJsonArray("price")) {
			this.reloadPrice.add(AmmoType.valueOf(ammoPrice.getAsString()));
		}

		this.primaryDamage = parameters.get("primaryDamage").getAsInt();
		this.primaryMarks = parameters.get("primaryMarks").getAsInt();
		this.moveDistance = parameters.get("moveDistance").getAsInt();
	}

	public boolean canFire(){
		return true;
	}

	public boolean doneFiring(){
		return doneFiring;
	}

	GameMap getGameMap() {
		return getGameBoard().getGameMap();
	}

	List<Player> getAllPlayers(){
		return getGameBoard().getPlayers();
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
		return new ArrayList<>(reloadPrice.subList(1, reloadPrice.size()));
	}

	/**
	 * Returns whether or not the weapon is loaded.
	 * @return whether or not the weapon is loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Loads the weapon.
	 */
	public void load() {
		//TODO: Implement
		this.loaded = true;
	}

	/**
	 * Creates a {@link QuestionContainer} to be passed to the view.
	 * @return the {@link QuestionContainer}.
	 */
	public QuestionContainer initialQuestion(){
		return null;
	}


	/**
	 * Handles the firing process of the weapon based on the currentStep and the choice of the player.
	 * @return the {@link QuestionContainer} to be passed to the view, or **null** if the weapon has finished firing.
	 * @param choice the choice of the player.
	 */
	public abstract QuestionContainer handleFire(int choice);

	/**
	 * This method will be called by the damage-dealer methods of the weapons.
	 * The two arrays are ordered in such a way that the i-th playerToShoot will be dealt the i-th damageAndMark.
	 * This method does NOT deload the weapon.
	 * This method does check only the first array's size, so it can happen that playersToShoot is shorter than
	 * damagesAndMarks.
	 * @param damagesAndMarks the damage and marks for each player.
	 * @param playersToShoot the array of players that will receive damage and/or marks.
	 */
	protected void dealDamage(List<DamageAndMarks> damagesAndMarks, List<Player> playersToShoot){
		for (int i = 0; i < playersToShoot.size(); i++) {
			if(playersToShoot.get(i) != null){
				playersToShoot.get(i).getPlayerBoard().addDamage(getOwner(), damagesAndMarks.get(i).getDamage());
				playersToShoot.get(i).getPlayerBoard().addMarks(getOwner(), damagesAndMarks.get(i).getMarks());
			}
		}
		doneFiring = true;
	}

	protected void dealDamage (List<DamageAndMarks> damagesAndMarks, Player... playersToShoot) {
		for (int i = 0; i < playersToShoot.length; i++) {
			if (playersToShoot[i] != null) {
				playersToShoot[i].getPlayerBoard().addDamage(getOwner(), damagesAndMarks.get(i).getDamage());
				playersToShoot[i].getPlayerBoard().addMarks(getOwner(), damagesAndMarks.get(i).getMarks());
			}
		}
		doneFiring = true;
	}

//	protected void dealDamage(List<DamageAndMarks> damagesAndMarks, Player playerToShoot){
//		playerToShoot.getPlayerBoard().addDamage(owner, damagesAndMarks.get(0).getDamage());
//		playerToShoot.getPlayerBoard().addMarks(owner, damagesAndMarks.get(0).getMarks());
//	}


	/**
	 * Primary method of firing of the weapon.
	 */
	protected abstract void primaryFire();

	/**
	 * Handles the primary fire mode of the weapon.
	 * This will be called if currentStep is at least 2.
	 *
	 * @param choice the choice of the player.
	 * @return the {@link QuestionContainer}.
	 */
	abstract QuestionContainer handlePrimaryFire(int choice);

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
		resetCurrentStep();
		this.loaded = false;
		this.currentTargets = null;
		this.relocationDone = false;
		this.enemyRelocationDone = false;
		this.currentTargets = new ArrayList<>();
		this.target = null;
		this.doneFiring = false;
	}

	protected List<DamageAndMarks> getStandardDamagesAndMarks() {
		return standardDamagesAndMarks;
	}

	protected int getPrimaryDamage() {
		return primaryDamage;
	}

	protected int getPrimaryMarks() {
		return primaryMarks;
	}

	protected int getMoveDistance() {
		return moveDistance;
	}

	protected int getMaximumSteps() {
		return maximumSteps;
	}

	/**
	 * Builds a {@link QuestionContainer} that asks which player to target.
	 * @param targets the target players to choose from.
	 * @return the {@link QuestionContainer}.
	 */
	public static QuestionContainer getTargetPlayersQnO(List<Player> targets){
		String question = "Which of the following players do you want to target?";
		List<String> options = new ArrayList<>();
		targets.forEach(target-> options.add(target.getPlayerName()));
		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	/**
	 * Builds a {@link QuestionContainer} that ask in which coordinate to move the enemy player.
	 * @param targetPlayer the player that will be moved.
	 * @param coordinates the list of coordinates to choose from.
	 * @return the {@link QuestionContainer}.
	 */
	protected static QuestionContainer getMovingTargetEnemyCoordinatesQnO(Player targetPlayer, List<Coordinates> coordinates){
		String question = "Where do you want to move " + targetPlayer.getPlayerName() + "?";
		List<Coordinates> options = new ArrayList<>(coordinates);
		return QuestionContainer.createCoordinatesQuestionContainer(question, options);
	}

	/**
	 * Builds a {@link QuestionContainer} that asks in which coordinate to fire at.
	 * @param coordinates the list of coordinates to choose from.
	 * @return the {@link QuestionContainer}.
	 */
	protected static QuestionContainer getTargetCoordinatesQnO(List<Coordinates> coordinates){
		String question = "Where do you want to fire?";
		List<Coordinates> options = new ArrayList<>(coordinates);
		return QuestionContainer.createCoordinatesQuestionContainer(question, options);
	}

	protected static QuestionContainer getCardinalQnO(){
		String question = "In which direction do you wish to fire?";
		List<String> options = 	Arrays.stream(CardinalDirection.values()).map(Enum::toString).collect(Collectors.toList());
		return QuestionContainer.createStringQuestionContainer(question,options);
	}

	protected static QuestionContainer getTargetPlayersAndRefusalQnO(List<Player> targets){
		String question = "Which of the following players do you want to target?";
		List<String> options = new ArrayList<>();
		targets.forEach(target-> options.add(target.getPlayerName()));
		options.add("Nobody");
		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	protected static QuestionContainer getActionTypeQnO(){
		String question = "Do you want to move or shoot?";
		List<String> options = new ArrayList<>();
		Arrays.stream(ActionType.values()).forEach(item -> options.add(item.toString()));
		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	protected void relocateEnemy(Player enemy, Coordinates coordinates){
		getGameMap().movePlayerTo(enemy, coordinates);
	}

	protected void relocateOwner(Coordinates coordinates){
		getGameMap().movePlayerTo(getOwner(), coordinates);
	}


	// ####################################
	// OVERRIDDEN METHODS
	// ####################################

	@Override
	public Representation getRep() {
		return new WeaponRep(this);
	}


	// ####################################
	// ENUM
	// ####################################

	protected enum ActionType{
		MOVE,
		SHOOT
	}
}