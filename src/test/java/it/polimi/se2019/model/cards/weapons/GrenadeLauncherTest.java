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

public class GrenadeLauncherTest {

	private static String name = "GrenadeLauncher";
	private WeaponCard grenadelauncher;
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
				grenadelauncher = new GrenadeLauncher(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		grenadelauncher.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		grenadelauncher.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(1, 0));
		gameMap.movePlayerTo(players.get(1), new Coordinates(0, 0));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = grenadelauncher.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select no optional..
		QuestionContainer nextQC;
		nextQC = grenadelauncher.doActivationStep(initialQuestion.getOptions().indexOf("No optional effects."));
		//Select first target.
		nextQC = grenadelauncher.doActivationStep(0);
		//Select move coordinate.
		nextQC = grenadelauncher.doActivationStep(nextQC.getCoordinates().indexOf(new Coordinates(0,1)));

		assertTrue(grenadelauncher.isActivationConcluded());
		assertEquals(1, grenadelauncher.getPlayersHit().size());
		assertEquals(gameMap.getPlayerCoordinates(players.get(1)), new Coordinates(0, 1));


		grenadelauncher.reset();
		assertFalse(grenadelauncher.isLoaded());
		assertTrue(grenadelauncher.currentTargets.isEmpty());

	}

	@Test
	public void primaryFireWithMoveShooting_correctBehaviour() {
		gameMap.movePlayerTo(players.get(2), new Coordinates(0, 1));

		QuestionContainer initialQuestion = grenadelauncher.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select no optional.
		QuestionContainer nextQC;
		nextQC = grenadelauncher.doActivationStep(initialQuestion.getOptions().indexOf("Optional effect 1."));
		//Select base
		nextQC = grenadelauncher.doActivationStep(0);
		//Select player1.
		nextQC = grenadelauncher.doActivationStep(nextQC.getOptions().indexOf(players.get(1).getPlayerName()));
		//Select move the target.
		nextQC = grenadelauncher.doActivationStep(0);
		//Select move coordinate.
		nextQC = grenadelauncher.doActivationStep(nextQC.getCoordinates().indexOf(new Coordinates(0,1)));
		//Select extra.
		nextQC = grenadelauncher.doActivationStep(0);
		//Select extra coordinate
		nextQC = grenadelauncher.doActivationStep(nextQC.getCoordinates().indexOf(new Coordinates(0,1)));

		assertTrue(grenadelauncher.isActivationConcluded());
		assertEquals(2, grenadelauncher.getPlayersHit().size());
		assertEquals(gameMap.getPlayerCoordinates(players.get(1)), new Coordinates(0, 1));


		grenadelauncher.reset();
		assertFalse(grenadelauncher.isLoaded());
		assertTrue(grenadelauncher.currentTargets.isEmpty());

	}

}