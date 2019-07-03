package it.polimi.se2019.model.cards.powerups;

import com.google.gson.*;
import it.polimi.se2019.model.cards.ActivableDeck;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * This class implements the powerup deck.
 *
 * @author MarcerAndrea
 * @author Desno365
 */
public class PowerupDeck extends ActivableDeck<PowerupCard> {

	public PowerupDeck(GameBoard gameBoard) {
		super(gameBoard);
	}

	/**
	 * Initialize the Powerup deck according to the file "PowerupDeck.json".
	 */
	@Override
	protected void initializeDeck() {
		Reader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/decks/PowerupDeck.json")));
		try {
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
		} catch (JsonParseException e) {
			Utils.logError("Cannot parse powerup cards", e);
		}
	}
}