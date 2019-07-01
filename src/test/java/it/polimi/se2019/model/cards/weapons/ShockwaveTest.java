package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.polimi.se2019.model.ModelDriver;
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

public class ShockwaveTest {
	private static String name = "Shockwave";
	private WeaponCard shockwave;
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
				shockwave = new Shockwave(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		shockwave.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		shockwave.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(1, 2));
		gameMap.movePlayerTo(players.get(1), new Coordinates(1, 1));
		gameMap.movePlayerTo(players.get(2), new Coordinates(1, 3));
		gameMap.movePlayerTo(players.get(3), new Coordinates(1, 3));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = shockwave.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select standard fire
		QuestionContainer nextQC;
		nextQC = shockwave.doActivationStep(initialQuestion.getOptions().indexOf("Standard fire."));
		//Select a target
		assertEquals(3, nextQC.getOptions().size());
		nextQC = shockwave.doActivationStep(0);

		//Select a target
		nextQC = shockwave.doActivationStep(0);

		assertTrue(shockwave.isActivationConcluded());
		assertEquals(2, shockwave.getPlayersHit().size());

		shockwave.reset();
		assertFalse(shockwave.isLoaded());
		assertTrue(shockwave.getCurrentTargets().isEmpty());

	}

	@Test
	public void secondaryFireShooting_correctBehaviour() {
		gameMap.movePlayerTo(players.get(3), new Coordinates(0, 2));
		gameMap.movePlayerTo(players.get(4), new Coordinates(0, 2));
		shockwave.load();

		QuestionContainer initialQuestion = shockwave.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select alternate fire
		QuestionContainer nextQC;
		nextQC = shockwave.doActivationStep(initialQuestion.getOptions().indexOf("Alternate fire."));

		assertTrue(shockwave.isActivationConcluded());
		assertEquals(4, shockwave.getPlayersHit().size());

		shockwave.reset();
		assertFalse(shockwave.isLoaded());
		assertTrue(shockwave.getCurrentTargets().isEmpty());
	}

}