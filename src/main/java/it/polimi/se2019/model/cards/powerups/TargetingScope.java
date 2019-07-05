package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the Targeting scope powerup.
 *
 * @author MarcerAndrea
 * @author Desno365
 */
public class TargetingScope extends PowerupCard {

	private static final String DESCRIPTION =
			"You may play this card when you are dealing\n" +
					"damage to one or more targets. Pay 1 ammo\n" +
					"cube of any color. Choose 1 of those targets\n" +
					"and give it an extra point of damage. Note: You\n" +
					"cannot use this to do 1 damage to a target that\n" +
					"is receiving only marks.";
	private static final int GIVEN_DAMAGE = 1;
	private ArrayList<AmmoType> ownedTypes;
	private Player targetPlayer;


	public TargetingScope(AmmoType associatedAmmo) {
		super("Targeting scope", associatedAmmo, DESCRIPTION, PowerupUseCaseType.ON_SHOOT, "targeting_scope_" + associatedAmmo.getCharacterColorType().toString().toLowerCase());
	}


	// ####################################
	// OVERRIDDEN METHODS
	// ####################################

	@Override
	public boolean canBeActivated() {
		boolean hasOneAmmo = false;
		for (AmmoType ammoType : AmmoType.values()) {
			if (getOwner().getPlayerBoard().getAmmoContainer().getAmmo(ammoType) > 0) {
				hasOneAmmo = true;
				break;
			}
		}
		return hasOneAmmo && !getShotPlayers().isEmpty();
	}

	@Override
	protected QuestionContainer firstStep() {
		List<String> options = new ArrayList<>();
		getShotPlayers().forEach(target -> options.add(target.getPlayerName()));
		return QuestionContainer.createStringQuestionContainer("Which of the following players do you want to target?", options);
	}

	@Override
	protected QuestionContainer secondStep(int choice) {
		// Save target chosen.
		targetPlayer = getShotPlayers().get(choice);

		// Ask which ammo to use,
		ownedTypes = new ArrayList<>();
		for (AmmoType ammoType : AmmoType.values()) {
			if (getOwner().getPlayerBoard().getAmmoContainer().getAmmo(ammoType) > 0) {
				ownedTypes.add(ammoType);
			}
		}

		List<String> ownedNames = ownedTypes.stream()
				.map(Enum::toString)
				.collect(Collectors.toList());

		return QuestionContainer.createStringQuestionContainer("Choose the ammo to use.", ownedNames);
	}

	@Override
	protected QuestionContainer thirdStep(int choice) {
		if (choice >= 0 && choice < ownedTypes.size()) {
			AmmoType ammoToUse = ownedTypes.get(choice);
			getOwner().getPlayerBoard().getAmmoContainer().removeAmmo(ammoToUse); // Use ammo.
			targetPlayer.addDamage(getOwner(), GIVEN_DAMAGE);
		} else {
			Utils.logError(getCardName() + " has received an illegal choice: " + choice + " and the size of owned types is " + ownedTypes.size(), new IllegalArgumentException());
		}
		concludeActivation();
		return null;
	}

}