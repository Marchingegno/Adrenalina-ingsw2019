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

public class SledgehammerTest {
	private static String name = "Sledgehammer";
	private WeaponCard sledgehammer;
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
				sledgehammer = new Sledgehammer(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		sledgehammer.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		sledgehammer.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(1, 3));
		gameMap.movePlayerTo(players.get(1), new Coordinates(1, 3));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = sledgehammer.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select standard fire
		QuestionContainer nextQC;
		nextQC = sledgehammer.doActivationStep(initialQuestion.getOptions().indexOf("Standard fire."));
		//Select a target
		nextQC = sledgehammer.doActivationStep(0);

		assertTrue(sledgehammer.isActivationConcluded());
		assertEquals(1, sledgehammer.getPlayersHit().size());

		sledgehammer.reset();
		assertFalse(sledgehammer.isLoaded());
		assertTrue(sledgehammer.currentTargets.isEmpty());

	}

	@Test
	public void secondaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = sledgehammer.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select alternate fire
		QuestionContainer nextQC;
		nextQC = sledgehammer.doActivationStep(initialQuestion.getOptions().indexOf("Alternate fire."));
		//Select a target
		nextQC = sledgehammer.doActivationStep(0);
		//Select coordinate (1,1)
		assertTrue(nextQC.getCoordinates().contains(new Coordinates(2, 3)));
		assertTrue(nextQC.getCoordinates().contains(new Coordinates(1, 2)));
		assertTrue(nextQC.getCoordinates().contains(new Coordinates(1, 1)));
		nextQC = sledgehammer.doActivationStep(nextQC.getCoordinates().indexOf(new Coordinates(1, 1)));

		assertTrue(sledgehammer.isActivationConcluded());
		assertEquals(1, sledgehammer.getPlayersHit().size());

		sledgehammer.reset();
		assertFalse(sledgehammer.isLoaded());
		assertTrue(sledgehammer.currentTargets.isEmpty());
	}

}