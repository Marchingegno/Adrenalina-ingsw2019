package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.Deck;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

/**
 * This class implements the Powerup deck
 * @author MarcerAndrea
 */
public class PowerupDeck extends Deck<PowerupCard> {

	/**
	 * Initialize the Powerup deck according to the file "PowerupDeck.txt".
	 *
	 * FILE FORMAT:
	 * POWERUP_CARD_NAME, NUM_OF_YELLOW_AMMOS, NUM_OF_RED_AMMOS, NUM_OF_BLUE_AMMOS
	 */
	protected void initializeDeck(){

		String powerupDeckPath = System.getProperty("user.dir") + "/src/resources/decks/PowerupDeck.txt";
		String line;
		String separator = ",";
		int numOfLineRead = 0;

		try (BufferedReader bufReader = new BufferedReader(new FileReader(powerupDeckPath))) {

			while ((line = bufReader.readLine()) != null) {

				numOfLineRead++;
				String[] elements = line.split(separator);

				switch (elements[0]){
					case "TELEPORTER":
						for (int i = 0; i < Integer.parseInt(elements[1]); i ++) addCard(new Teleporter(AmmoType.YELLOW_AMMO));
						for (int i = 0; i < Integer.parseInt(elements[2]); i ++) addCard(new Teleporter(AmmoType.RED_AMMO));
						for (int i = 0; i < Integer.parseInt(elements[3]); i ++) addCard(new Teleporter(AmmoType.BLUE_AMMO));
						break;

					case "NEWTON":
						for (int i = 0; i < Integer.parseInt(elements[1]); i ++) addCard(new Newton(AmmoType.YELLOW_AMMO));
						for (int i = 0; i < Integer.parseInt(elements[2]); i ++) addCard(new Newton(AmmoType.RED_AMMO));
						for (int i = 0; i < Integer.parseInt(elements[3]); i ++) addCard(new Newton(AmmoType.BLUE_AMMO));
						break;

					case "TARGETING_SCOPE":
						for (int i = 0; i < Integer.parseInt(elements[1]); i ++) addCard(new TargetingScope(AmmoType.YELLOW_AMMO));
						for (int i = 0; i < Integer.parseInt(elements[2]); i ++) addCard(new TargetingScope(AmmoType.RED_AMMO));
						for (int i = 0; i < Integer.parseInt(elements[3]); i ++) addCard(new TargetingScope(AmmoType.BLUE_AMMO));
						break;

					case "TACKBACK_GRENADE":
						for (int i = 0; i < Integer.parseInt(elements[1]); i ++) addCard(new TagbackGrenade(AmmoType.YELLOW_AMMO));
						for (int i = 0; i < Integer.parseInt(elements[2]); i ++) addCard(new TagbackGrenade(AmmoType.RED_AMMO));
						for (int i = 0; i < Integer.parseInt(elements[3]); i ++) addCard(new TagbackGrenade(AmmoType.BLUE_AMMO));
						break;

					default: throw new ParseException("Error parsing powerups", numOfLineRead);
				}
			}
		} catch (IOException e) {
			Utils.logError("Error in initializeDeck()", e);
		}catch (ParseException e) {
			Utils.logError("Error while parsing PowerupDeck at line " + e.getErrorOffset(), e);
		}
	}

}