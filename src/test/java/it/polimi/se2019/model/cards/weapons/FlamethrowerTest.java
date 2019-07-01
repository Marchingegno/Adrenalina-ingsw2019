package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.se2019.model.ModelDriver;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FlamethrowerTest {

	private static String name = "Flamethrower";
	private WeaponCard flamethrower;
	private GameMap gameMap;
	private ModelDriver model;
	private List<Player> players;

	@Before
	public void setUp() {
		//The first player is the shooter.
		Reader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/decks/WeaponDeckWhole.json")));
		JsonArray weapons = new JsonParser().parse(reader).getAsJsonObject().getAsJsonArray("weapons");
		for (JsonElement weapon : weapons) {
			if (weapon.getAsJsonObject().get("className").getAsString().equals(name)) {
				flamethrower = new Flamethrower(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		flamethrower.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		flamethrower.setOwner(players.get(0));
		players.get(0).getPlayerBoard().getAmmoContainer().addAmmo(AmmoType.YELLOW_AMMO);


		gameMap.movePlayerTo(players.get(0), new Coordinates(0, 0));
		gameMap.movePlayerTo(players.get(1), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(2), new Coordinates(0, 2));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = flamethrower.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select standard fire
		QuestionContainer nextQC;
		nextQC = flamethrower.doActivationStep(initialQuestion.getOptions().indexOf("Standard fire."));
		//Select right
		nextQC = flamethrower.doActivationStep(0);
		//Select first target
		nextQC = flamethrower.doActivationStep(0);
		//Select second target
		nextQC = flamethrower.doActivationStep(0);

		assertTrue(flamethrower.isActivationConcluded());
		assertEquals(2, flamethrower.getPlayersHit().size());

		flamethrower.reset();
		assertFalse(flamethrower.isLoaded());
		assertTrue(flamethrower.getCurrentTargets().isEmpty());

	}

	@Test
	public void secondaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = flamethrower.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select alternate fire.
		QuestionContainer nextQC;
		nextQC = flamethrower.doActivationStep(initialQuestion.getOptions().indexOf("Alternate fire."));
		//Select right
		nextQC = flamethrower.doActivationStep(0);

		assertTrue(flamethrower.isActivationConcluded());
		assertEquals(2, flamethrower.getPlayersHit().size());

		flamethrower.reset();
		assertFalse(flamethrower.isLoaded());
		assertTrue(flamethrower.getCurrentTargets().isEmpty());
	}
}