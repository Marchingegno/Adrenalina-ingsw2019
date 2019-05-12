package it.polimi.se2019.model.cards.powerups;

import com.google.gson.*;
import it.polimi.se2019.model.cards.Deck;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.utils.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * This class implements the powerup deck
 *
 * @author MarcerAndrea
 */
public class PowerupDeck extends Deck<PowerupCard> {

	/**
	 * Initialize the Powerup deck according to the file "PowerupDeck.json".
	 */
	protected void initializeDeck() {

		try (Reader reader = new FileReader(new File(Thread.currentThread().getContextClassLoader().getResource("decks/PowerupDeck.json").getFile()))) {
			JsonParser parser = new JsonParser();
			JsonObject rootObject = parser.parse(reader).getAsJsonObject();

			JsonArray powerupsToAdd = rootObject.getAsJsonArray("powerups");
			for (JsonElement entry : powerupsToAdd) {
				JsonObject powerupToAdd = entry.getAsJsonObject();

				switch (powerupToAdd.get("name").getAsString()) {
					case "Teleporter":
						addCard(new Teleporter(AmmoType.valueOf(powerupToAdd.get("ammoType").getAsString())));
						Utils.logInfo("PowerupDeck -> addTeleporter(): Added " + powerupToAdd.get("ammoType").getAsString() + " Teleporter");
						break;

					case "Newton":
						addCard(new Newton(AmmoType.valueOf(powerupToAdd.get("ammoType").getAsString())));
						Utils.logInfo("PowerupDeck -> addTeleporter(): Added " + powerupToAdd.get("ammoType").getAsString() + " Newton");
						break;

					case "Targetting scope":
						addCard(new TargetingScope(AmmoType.valueOf(powerupToAdd.get("ammoType").getAsString())));
						Utils.logInfo("PowerupDeck -> addTeleporter(): Added " + powerupToAdd.get("ammoType").getAsString() + " Targetting scope");
						break;

					case "Tagback grenade":
						addCard(new TagbackGrenade(AmmoType.valueOf(powerupToAdd.get("ammoType").getAsString())));
						Utils.logInfo("PowerupDeck -> addTeleporter(): Added " + powerupToAdd.get("ammoType").getAsString() + " Tagback grenade");
						break;

					default:
						throw new JsonParseException("No such powerup");
				}
			}
		} catch (IOException | JsonParseException e) {
			Utils.logError("Cannot parse powerup cards", e);
		}

	}
}