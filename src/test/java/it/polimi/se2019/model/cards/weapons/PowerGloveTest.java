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

public class PowerGloveTest {
	private static String name = "PowerGlove";
	private WeaponCard powerglove;
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
				powerglove = new PowerGlove(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		powerglove.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		powerglove.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(0, 0));
		gameMap.movePlayerTo(players.get(1), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(2), new Coordinates(0, 2));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = powerglove.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select standard fire
		QuestionContainer nextQC;
		nextQC = powerglove.doActivationStep(initialQuestion.getOptions().indexOf("Standard fire."));
		//Select target
		nextQC = powerglove.doActivationStep(0);

		assertTrue(powerglove.isActivationConcluded());
		assertEquals(1, powerglove.getPlayersHit().size());

		powerglove.reset();
		assertFalse(powerglove.isLoaded());
		assertTrue(powerglove.currentTargets.isEmpty());

	}

	@Test
	public void secondaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = powerglove.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select alternate fire.
		QuestionContainer nextQC;
		nextQC = powerglove.doActivationStep(initialQuestion.getOptions().indexOf("Alternate fire."));
		//Select direction RIGHT
		nextQC = powerglove.doActivationStep(0);
		//Select distance of flight (2 squares).
		nextQC = powerglove.doActivationStep(1);
		//Select first target.
		nextQC = powerglove.doActivationStep(0);
		//Select second target
		nextQC = powerglove.doActivationStep(0);


		assertEquals(gameMap.getPlayerCoordinates(players.get(0)), new Coordinates(0,2));
		assertTrue(powerglove.isActivationConcluded());
		assertEquals(2, powerglove.getPlayersHit().size());

		powerglove.reset();
		assertFalse(powerglove.isLoaded());
		assertTrue(powerglove.currentTargets.isEmpty());
	}

}