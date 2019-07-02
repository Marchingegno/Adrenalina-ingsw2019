package it.polimi.se2019.model.cards.weapons;

import com.google.gson.*;
import it.polimi.se2019.model.cards.ActivableDeck;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Class that represents a deck of WeaponCards.
 */
public class WeaponDeck extends ActivableDeck<WeaponCard> {

	public WeaponDeck(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void initializeDeck() {
		Reader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/decks/Weapon.json")));
		try {
			JsonParser parser = new JsonParser();
			JsonObject rootObject = parser.parse(reader).getAsJsonObject();
			JsonArray weapons = rootObject.getAsJsonArray("weapons");

			Class<?> weaponClassToInstantiate;
			Constructor<?> constructor;

			for (JsonElement entry : weapons) {
				JsonObject weaponToAdd = entry.getAsJsonObject();
				weaponClassToInstantiate = Class.forName("it.polimi.se2019.model.cards.weapons." + weaponToAdd.get("className").getAsString());
				constructor = weaponClassToInstantiate.getConstructor(JsonObject.class);
				addCard((WeaponCard) constructor.newInstance(weaponToAdd));
				Utils.logInfo("WeaponDeck -> initializeDeck(): AddedToTheDeck " + weaponToAdd.get("className").getAsString());
			}

		} catch (JsonParseException e) {
			Utils.logError("Cannot parse weapon cards", e);
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			Utils.logError("Cannot find create a class for a weapon", e);
		}
	}

}