package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.ActivableCard;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class that defines the structure of a weapon card. Every subtype of weapon must extend this.
 * The weapons are handled with the help of the Strategy Pattern. Each weapon has a predetermined number of
 * "advancement steps", that represent the current state of advancement of the firing process of the weapon.
 * Each time the controller wishes to continue the process of firing of the weapon, it must call the method **doActivationStep**;
 * This will return a {@link QuestionContainer} that the controller must send to the Virtual View of the shooting player.
 * The controller will need to {@link #reset()} the weapon if it has finished firing.
 * The method {@link #reset()} will unload and reset the weapon to its original state.
 *
 * @author Marchingegno
 */
public abstract class WeaponCard extends ActivableCard {

	private final ArrayList<AmmoType> reloadPrice;
	private final int moveDistance; //Standard move for relocation of the player.
	private final int primaryDamage;
	private final int primaryMarks;
	List<DamageAndMarks> standardDamagesAndMarks;
	List<Player> currentTargets;
	Player target;
	private boolean loaded;
	private List<Player> playersHit;


	/**
	 * Constructor of the class.
	 *
	 * @param parameters the JsonObject with the parameters needed for the weapon.
	 */
	public WeaponCard(JsonObject parameters) {
		super(parameters.get("name").getAsString(), parameters.get("description").getAsString(), parameters.get("imagePath").getAsString());
		this.standardDamagesAndMarks = new ArrayList<>();
		this.reloadPrice = new ArrayList<>();

		for (JsonElement ammoPrice : parameters.getAsJsonArray("price")) {
			this.reloadPrice.add(AmmoType.valueOf(ammoPrice.getAsString()));
		}

		this.primaryDamage = parameters.get("primaryDamage").getAsInt();
		this.primaryMarks = parameters.get("primaryMarks").getAsInt();
		this.moveDistance = parameters.get("moveDistance").getAsInt();
		this.currentTargets = new ArrayList<>();
		this.playersHit = new ArrayList<>();
		resetCurrentStep();
		loaded = true;
	}

	/**
	 * Builds a {@link QuestionContainer} that asks in which coordinate to move at.
	 *
	 * @param coordinates the list of coordinates to choose from.
	 * @return the {@link QuestionContainer}.
	 */
	static QuestionContainer getMoveCoordinatesQnO(List<Coordinates> coordinates) {
		return getCoordinatesQno(coordinates, "Where do you want to move?");
	}

	/**
	 * Builds a {@link QuestionContainer} that asks in which coordinate to fire at.
	 *
	 * @param coordinates the list of coordinates to choose from.
	 * @param question the question of the QuestionContainer.
	 * @return the {@link QuestionContainer}.
	 */
	private static QuestionContainer getCoordinatesQno(List<Coordinates> coordinates, String question) {
		return QuestionContainer.createCoordinatesQuestionContainer(question, new ArrayList<>(coordinates));
	}

	/**
	 * Builds a {@link QuestionContainer} that asks in which cardinal direction to fire at.
	 * @param availableDirections the available directions that the player is allowed to fire.
	 * @return the {@link QuestionContainer}.
	 */
	static QuestionContainer getCardinalQnO(List<String> availableDirections) {
		String question = "In which direction do you wish to fire?";
		List<String> options = new ArrayList<>(availableDirections);
		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	/**
	 * Builds a {@link QuestionContainer} that asks which player to target.
	 * The player can refuse.
	 *
	 * @param targets the target players to choose from.
	 * @return the {@link QuestionContainer}.
	 */
	static QuestionContainer getTargetPlayersAndRefusalQnO(List<Player> targets) {
		String question = "Which of the following players do you want to target?";
		List<String> options = new ArrayList<>();
		targets.forEach(target -> options.add(target.getPlayerName()));
		options.add("Nobody");
		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	/**
	 * Given a list and a choice, check whether or not the choice is a refusal.
	 * The list must contain a refusal option as its last option.
	 * @param listToCheck the list to check.
	 * @param choice the choice of the player.
	 * @return whether or not the choice is a refusal.
	 */
	static boolean isThisChoiceRefusal(List listToCheck, int choice) {
		if (choice == listToCheck.size()) {
			Utils.logWeapon("The player refused.");
		}
		return choice == listToCheck.size();
	}

	/**
	 * Gets the firing cost after choosing eventual effects/mode of firing.
	 *
	 * @return the price.
	 */
	public List<AmmoType> getFiringCost() {
		return new ArrayList<>();
	}

	/**
	 * Builds a {@link QuestionContainer} that asks which player to target.
	 *
	 * @param targets the target players to choose from.
	 * @return the {@link QuestionContainer}.
	 */
	public static QuestionContainer getTargetPlayersQnO(List<Player> targets) {
		return getTargetPlayersQnO(targets, "Which of the following players do you want to target?");
	}

	public static QuestionContainer getTargetPlayersQnO(List<Player> targets, String question) {
		List<String> options = new ArrayList<>();
		targets.forEach(target -> options.add(target.getPlayerName()));
		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	/**
	 * Builds a {@link QuestionContainer} that ask in which coordinate to move the enemy player.
	 *
	 * @param targetPlayer the player that will be moved.
	 * @param coordinates  the list of coordinates to choose from.
	 * @return the {@link QuestionContainer}.
	 */
	static QuestionContainer getMovingTargetEnemyCoordinatesQnO(Player targetPlayer, List<Coordinates> coordinates) {
		return getCoordinatesQno(coordinates, "Where do you want to move " + targetPlayer.getPlayerName() + "?");
	}

	/**
	 * Builds a {@link QuestionContainer} that asks in which coordinate to fire at.
	 *
	 * @param coordinates the list of coordinates to choose from.
	 * @return the {@link QuestionContainer}.
	 */
	static QuestionContainer getTargetCoordinatesQnO(List<Coordinates> coordinates) {
		return getCoordinatesQno(coordinates, "Where do you want to fire?");
	}

	GameMap getGameMap() {
		return getGameBoard().getGameMap();
	}

	List<Player> getAllPlayers() {
		return getGameBoard().getPlayers();
	}

	/**
	 * Returns the reload price of this weapon.
	 *
	 * @return Reload price of this weapon.
	 */
	public List<AmmoType> getReloadPrice() {
		return new ArrayList<>(reloadPrice);
	}

	/**
	 * Returns the grab price for this weapon. It consists of the reload price minus the first occurrence.
	 *
	 * @return grab price for this weapon.
	 */
	public List<AmmoType> getGrabPrice() {
		return new ArrayList<>(reloadPrice.subList(1, reloadPrice.size()));
	}

	/**
	 * Returns whether or not the weapon is loaded.
	 *
	 * @return whether or not the weapon is loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * After getting the primary targets,
	 * builds a {@link QuestionContainer} that asks in which coordinate to fire at.
	 *
	 * @return the {@link QuestionContainer}.
	 */
	QuestionContainer setPrimaryCurrentTargetsAndReturnTargetQnO() {
		currentTargets = getPrimaryTargets();
		return getTargetPlayersQnO(currentTargets);
	}

	/**
	 * Check if the weapon can be activated and loaded.
	 * @return whether or not the weapon can be chosen to fire.
	 */
	@Override
	public boolean canBeActivated() {
		return canPrimaryBeActivated() && isLoaded();
	}

	/**
	 * Check if the weapon can be activated.
	 * @return whether or not the weapon can fire.
	 */
	protected boolean canPrimaryBeActivated() {
		return !getPrimaryTargets().isEmpty();
	}

	/**
	 * Loads the weapon.
	 */
	public void load() {
		this.loaded = true;
	}

	/**
	 * Deals damage to players and conclude the activation.
	 * @param damagesAndMarks the list of damages and marks ordered.
	 * @param playersToShoot the players to be shot.
	 */
	protected void dealDamageAndConclude(List<DamageAndMarks> damagesAndMarks, List<Player> playersToShoot) {
		dealDamage(damagesAndMarks, playersToShoot);
		concludeActivation();
	}

	/**
	 * Deals damage to players and conclude the activation.
	 * @param damagesAndMarks the list of damages and marks ordered.
	 * @param playersToShoot the players to be shot.
	 */
	protected void dealDamageAndConclude(List<DamageAndMarks> damagesAndMarks, Player... playersToShoot) {
		List<Player> list = Arrays.asList(playersToShoot);
		dealDamageAndConclude(damagesAndMarks, list);
	}

	/**
	 * This method will be called by the damage-dealer methods of the weapons.
	 * The two arrays are ordered in such a way that the i-th playerToShoot will be dealt the i-th damageAndMark.
	 * This method does NOT deload the weapon.
	 * This method does check only the first array's size, so it can happen that playersToShoot is shorter than
	 * damagesAndMarks.
	 *
	 * @param damagesAndMarks the damage and marks for each player.
	 * @param playersToShoot  the array of players that will receive damage and/or marks.
	 */
	void dealDamage(List<DamageAndMarks> damagesAndMarks, List<Player> playersToShoot) {
		for (int i = 0; i < playersToShoot.size(); i++) {
			if (playersToShoot.get(i) != null) {

				playersToShoot.get(i).addDamage(getOwner(), damagesAndMarks.get(i).getDamage() + GameConstants
						.DAMAGE_OVERLOAD);
				if (damagesAndMarks.get(i).getDamage() >= 1 && !playersHit.contains(playersToShoot.get(i))) {
					playersHit.add(playersToShoot.get(i));
				}
				playersToShoot.get(i).addMarks(getOwner(), damagesAndMarks.get(i).getMarks());
			}
		}
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
	 * @return the {@link QuestionContainer}.
	 */
	abstract QuestionContainer handlePrimaryFire(int choice);

	/**
	 * Get the targets of the primary mode of fire for this weapon.
	 *
	 * @return the targettable players of the primary mode of fire.
	 */
	public abstract List<Player> getPrimaryTargets();

	/**
	 * Deloads the weapon and reset eventually modified parameters.
	 */
	@Override
	public void reset() {
		super.reset();
		this.loaded = false;
		this.currentTargets = new ArrayList<>();
		this.target = null;
		this.playersHit = new ArrayList<>();
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

	/**
	 * Relocate an enemy.
	 * @param enemy the enemy to relocate.
	 * @param coordinates the coordinate in which the enemy will be moved.
	 */
	void relocateEnemy(Player enemy, Coordinates coordinates) {
		getGameMap().movePlayerTo(enemy, coordinates);
	}

	/**
	 * Relocate the owner.
	 * @param coordinates the coordinates in which the owner will be moved.
	 */
	void relocateOwner(Coordinates coordinates) {
		getGameMap().movePlayerTo(getOwner(), coordinates);
	}


	/**
	 * Get all players hit with at least 1 damage.
	 * @return the list of players.
	 */
	public List<Player> getPlayersHit() {
		return new ArrayList<>(playersHit);
	}

	// ####################################
	// OVERRIDDEN METHODS
	// ####################################

	@Override
	public String toString() {
		return getCardName();
	}

	@Override
	public Representation getRep() {
		return new WeaponRep(this);
	}
}