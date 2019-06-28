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

public class ZX_2Test {
	private static String name = "ZX_2";
	private WeaponCard zx2;
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
				zx2 = new ZX_2(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		zx2.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		zx2.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(1), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(2), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(3), new Coordinates(0, 2));
	}

	@Test
	public void secondaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = zx2.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select standard fire
		QuestionContainer nextQC;
		nextQC = zx2.doActivationStep(initialQuestion.getOptions().indexOf("Alternate fire."));
		//Select a target.
		assertEquals(3, nextQC.getOptions().size());
		nextQC = zx2.doActivationStep(0);

		//Select a target.
		nextQC = zx2.doActivationStep(0);

		//Select a target
		nextQC = zx2.doActivationStep(0);

		assertTrue(zx2.isActivationConcluded());
		//This mode deals only marks
		assertEquals(0, zx2.getPlayersHit().size());

		zx2.reset();
		assertFalse(zx2.isLoaded());
		assertTrue(zx2.currentTargets.isEmpty());

	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = zx2.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select standard fire
		QuestionContainer nextQC;
		nextQC = zx2.doActivationStep(initialQuestion.getOptions().indexOf("Standard fire."));
		//Select a target.
		assertEquals(3, nextQC.getOptions().size());
		nextQC = zx2.doActivationStep(0);

		assertTrue(zx2.isActivationConcluded());
		assertEquals(1, zx2.getPlayersHit().size());

		zx2.reset();
		assertFalse(zx2.isLoaded());
		assertTrue(zx2.currentTargets.isEmpty());

	}

}