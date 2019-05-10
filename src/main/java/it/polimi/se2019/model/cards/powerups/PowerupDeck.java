package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.Deck;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

/**
 * This class implements the powerup deck
 *
 * @author MarcerAndrea
 */
public class PowerupDeck extends Deck<PowerupCard> {

	/**
	 * Initialize the Powerup deck according to the file "PowerupDeck.txt".
	 * <p>
	 * FILE FORMAT:
	 * POWERUP_CARD_NAME, NUM_OF_YELLOW_AMMOS, NUM_OF_RED_AMMOS, NUM_OF_BLUE_AMMOS
	 */
	protected void initializeDeck() {

		String powerupDeckPath = System.getProperty("user.dir") + "/src/main/resources/decks/PowerupDeck.txt";
		String line;
		String separator = ",";
		int numOfLineRead = 0;

		try (BufferedReader bufReader = new BufferedReader(new FileReader(powerupDeckPath))) {

			while ((line = bufReader.readLine()) != null) {

				numOfLineRead++;
				String[] elements = line.split(separator);

				switch (elements[0]) {
					case "TELEPORTER":
						addTeleporter(elements);
						break;

					case "NEWTON":
						addNewton(elements);
						break;

					case "TARGETING_SCOPE":
						addTargetingScope(elements);
						break;

					case "TACKBACK_GRENADE":
						addTagbackGrenade(elements);
						break;

					default:
						throw new ParseException("Error parsing powerups", numOfLineRead);
				}
			}
		} catch (IOException e) {
			Utils.logError("Error in initializeDeck()", e);
		} catch (ParseException e) {
			Utils.logError("Error while parsing PowerupDeck at line " + e.getErrorOffset(), e);
		}
	}

	private void addTeleporter(String[] numOfPowerupsForEachColor) {

		//Iterates om every ammo type
		for (int i = 1; i < numOfPowerupsForEachColor.length; i++) {

			//add the correct number of powerups with the i-th color in the enum
			for (int j = 0; j < Integer.parseInt(numOfPowerupsForEachColor[i]); j++) {
				addCard(new Teleporter(AmmoType.values()[i - 1]));
				Utils.logInfo("PowerupDeck -> addTeleporter(): Added " + j + "of" + numOfPowerupsForEachColor[i] + " " + AmmoType.values()[i - 1] + " Teleporter");
			}
		}

	}

	private void addNewton(String[] numOfPowerupsForEachColor) {

		//Iterates om every ammo type
		for (int i = 1; i < numOfPowerupsForEachColor.length; i++) {

			//add the correct number of powerups with the i-th color in the enum
			for (int j = 0; j < Integer.parseInt(numOfPowerupsForEachColor[i]); j++) {
				addCard(new Newton(AmmoType.values()[i - 1]));
				Utils.logInfo("PowerupDeck -> addNewton(): Added " + numOfPowerupsForEachColor[i] + " " + AmmoType.values()[i - 1] + " Newton");
			}
		}

	}

	private void addTargetingScope(String[] numOfPowerupsForEachColor) {

		//Iterates om every ammo type
		for (int i = 1; i < numOfPowerupsForEachColor.length; i++) {

			//add the correct number of powerups with the i-th color in the enum
			for (int j = 0; j < Integer.parseInt(numOfPowerupsForEachColor[i]); j++) {
				addCard(new TargetingScope(AmmoType.values()[i - 1]));
				Utils.logInfo("PowerupDeck -> addTargetingScope(): Added " + numOfPowerupsForEachColor[i] + " " + AmmoType.values()[i - 1] + " TargetingScope");
			}
		}

	}

	private void addTagbackGrenade(String[] numOfPowerupsForEachColor) {

		//Iterates om every ammo type
		for (int i = 1; i < numOfPowerupsForEachColor.length; i++) {

			//add the correct number of powerups with the i-th color in the enum
			for (int j = 0; j < Integer.parseInt(numOfPowerupsForEachColor[i]); j++) {
				addCard(new TagbackGrenade(AmmoType.values()[i - 1]));
				Utils.logInfo("PowerupDeck -> addTagbackGrenade(): Added " + numOfPowerupsForEachColor[i] + " " + AmmoType.values()[i - 1] + " TagbackGrenade");
			}
		}

	}

}