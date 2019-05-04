package it.polimi.se2019.model.cards.ammo;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.Deck;
import it.polimi.se2019.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class implements the ammo deck
 *
 * @author MarcerAndrea
 */
public class AmmoDeck extends Deck<Card> {

	/**
	 * Initialize the ammo deck according to the file "AmmoDeck.txt"
	 * <p>
	 * FILE FORMAT:
	 * HAS_POWERUP, NUM_OF_YELLOW_AMMO, NUM_OF_RED_AMMO, NUM_OF_BLUE_AMMO
	 */
	protected void initializeDeck() {

		String ammoDeckPath = System.getProperty("user.dir") + "/src/resources/decks/AmmoDeck.txt";
		String line;
		String separator = ",";
		ArrayList<AmmoType> ammoToAdd = new ArrayList<>();

		try (BufferedReader bufReader = new BufferedReader(new FileReader(ammoDeckPath))) {

			while ((line = bufReader.readLine()) != null) {

				String[] elements = line.split(separator);
				ammoToAdd.clear();
				for (int i = 0; i < Integer.parseInt(elements[1]); i++) {
					ammoToAdd.add(AmmoType.YELLOW_AMMO);
				}
				for (int i = 0; i < Integer.parseInt(elements[2]); i++) {
					ammoToAdd.add(AmmoType.RED_AMMO);
				}
				for (int i = 0; i < Integer.parseInt(elements[3]); i++) {
					ammoToAdd.add(AmmoType.BLUE_AMMO);
				}
				Utils.logInfo("AmmoDeck -> initializeDeck(): Adding ammo card: " + ammoToAdd + (Boolean.parseBoolean(elements[0]) ? " Powerup" : ""));
				addCard(new AmmoCard(ammoToAdd, Boolean.parseBoolean(elements[0])));
			}
		} catch (IOException e) {
			Utils.logError("Error in initializeDeck()", e);
		}
	}

}