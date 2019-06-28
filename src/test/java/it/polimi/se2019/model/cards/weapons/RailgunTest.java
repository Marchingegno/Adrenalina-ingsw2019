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

public class RailgunTest {
	private static String name = "Railgun";
	private WeaponCard railgun;
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
				railgun = new Railgun(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		railgun.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		railgun.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(0, 1));
		gameMap.movePlayerTo(players.get(1), new Coordinates(1, 1));
		gameMap.movePlayerTo(players.get(2), new Coordinates(2, 1));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = railgun.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select standard fire
		QuestionContainer nextQC;
		nextQC = railgun.doActivationStep(initialQuestion.getOptions().indexOf("Standard fire."));
		//Select a direction
		assertEquals(1, nextQC.getOptions().size());
		nextQC = railgun.doActivationStep(0);

		//Select a target
		nextQC = railgun.doActivationStep(0);

		assertTrue(railgun.isActivationConcluded());
		assertEquals(1, railgun.getPlayersHit().size());

		railgun.reset();
		assertFalse(railgun.isLoaded());
		assertTrue(railgun.currentTargets.isEmpty());

	}

	@Test
	public void secondaryFireShooting_correctBehaviour() {
		gameMap.movePlayerTo(players.get(1), new Coordinates(0, 1));
		railgun.load();

		QuestionContainer initialQuestion = railgun.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select alternate fire
		QuestionContainer nextQC;
		nextQC = railgun.doActivationStep(initialQuestion.getOptions().indexOf("Alternate fire."));
		//Select a direction
		//There are 4 directions available since a player is in the same square of the owner.
		assertEquals(4, nextQC.getOptions().size());
		nextQC = railgun.doActivationStep(nextQC.getOptions().indexOf("DOWN"));

		//Select a target
		nextQC = railgun.doActivationStep(0);
		//Select a target
		nextQC = railgun.doActivationStep(0);

		assertTrue(railgun.isActivationConcluded());
		assertEquals(2, railgun.getPlayersHit().size());

		railgun.reset();
		assertFalse(railgun.isLoaded());
		assertTrue(railgun.currentTargets.isEmpty());
	}

}