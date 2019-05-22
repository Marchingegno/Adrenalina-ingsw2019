package it.polimi.se2019.model.cards.weapons;

import com.google.gson.*;
import it.polimi.se2019.model.cards.ActivableDeck;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gameboard.GameBoard;
import it.polimi.se2019.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class WeaponDeck extends ActivableDeck<WeaponCard> {

	public WeaponDeck(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void initializeDeck(){
		String placeHolder = "placeHolder";

		Reader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/decks/Weapon.json")));
		try {
			JsonParser parser = new JsonParser();
			JsonObject rootObject = parser.parse(reader).getAsJsonObject();
			JsonArray weapons = rootObject.getAsJsonArray("weapons");

			Class<?> weaponClassToIstantiate;
			Constructor<?> constructor;

			for (JsonElement entry : weapons) {
				JsonObject weaponToAdd = entry.getAsJsonObject();

				weaponClassToIstantiate = Class.forName("it.polimi.se2019.model.cards.weapons." + weaponToAdd.get("className").getAsString());
				constructor = weaponClassToIstantiate.getConstructor(JsonObject.class);
				addCard((WeaponCard) constructor.newInstance(weaponToAdd));
			}

		} catch (JsonParseException e) {
			Utils.logError("Cannot parse weapon cards", e);
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			Utils.logError("Cannot find create a class for a weapon", e);
			System.out.println("qui " + e);
		}

		for (int i = 0; i < 3; i++) {
			List<AmmoType> price = new ArrayList<>();
			price.add(AmmoType.BLUE_AMMO);
			addCard(new Elecroscythe(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.RED_AMMO);
			price.add(AmmoType.BLUE_AMMO);
			addCard(new Furnace(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.RED_AMMO);
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Hellion(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Shockwave(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.YELLOW_AMMO);
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Shotgun(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.BLUE_AMMO);
			addCard(new TractorBeam(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.BLUE_AMMO);
			price.add(AmmoType.BLUE_AMMO);
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Whisper(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.YELLOW_AMMO);
			price.add(AmmoType.RED_AMMO);
			addCard(new ZX_2(placeHolder, price));

			price = new ArrayList<>();
			price.add(AmmoType.YELLOW_AMMO);
			addCard(new Sledgehammer(placeHolder, price));
		}
	}

}