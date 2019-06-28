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


public class RocketLauncherTest {
	private static String name = "RocketLauncher";
	private WeaponCard rocketLauncher;
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
				rocketLauncher = new RocketLauncher(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		rocketLauncher.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		rocketLauncher.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(2, 3));
		gameMap.movePlayerTo(players.get(1), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(2), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(3), new Coordinates(0, 1));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = rocketLauncher.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select optional effect 1 + 2.
		QuestionContainer nextQC;
		rocketLauncher.doActivationStep(initialQuestion.getOptions().indexOf("Optional effect 1 + Optional effect 2."));
		//Select move two steps.
		nextQC = rocketLauncher.doActivationStep(0);
		//Select coordinates.
		nextQC = rocketLauncher.doActivationStep(nextQC.getCoordinates().indexOf(new Coordinates(1, 2)));
		//Select basic effect.
		nextQC = rocketLauncher.doActivationStep(0);
		//Select first player
		nextQC = rocketLauncher.doActivationStep(nextQC.getOptions().indexOf("Player 1"));
		//Select move target
		nextQC = rocketLauncher.doActivationStep(0);
		//Choose coordinates
		nextQC = rocketLauncher.doActivationStep(nextQC.getCoordinates().indexOf(new Coordinates(0, 0)));

		assertTrue(rocketLauncher.isActivationConcluded());
		assertEquals(gameMap.getPlayerCoordinates(players.get(1)), new Coordinates(0, 0));
		assertEquals(3, rocketLauncher.getPlayersHit().size());

		rocketLauncher.reset();
		assertTrue(rocketLauncher.currentTargets.isEmpty());
		assertFalse(rocketLauncher.isLoaded());
	}

}