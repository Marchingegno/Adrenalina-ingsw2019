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

public class TractorBeamTest {
	private static String name = "TractorBeam";
	private WeaponCard tractorbeam;
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
				tractorbeam = new TractorBeam(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		tractorbeam.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		tractorbeam.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(0, 0));
		gameMap.movePlayerTo(players.get(1), new Coordinates(2, 3));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer initialQuestion = tractorbeam.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select standard fire
		QuestionContainer nextQC;
		nextQC = tractorbeam.doActivationStep(initialQuestion.getOptions().indexOf("Standard fire."));
		//Select a target
		nextQC = tractorbeam.doActivationStep(0);
		//Select move coordinates.
		assertTrue(nextQC.getCoordinates().contains(new Coordinates(1, 2)));
		nextQC = tractorbeam.doActivationStep(nextQC.getCoordinates().indexOf(new Coordinates(1, 2)));

		assertTrue(tractorbeam.isActivationConcluded());
		assertEquals(1, tractorbeam.getPlayersHit().size());
		assertEquals(gameMap.getPlayerCoordinates(players.get(1)), new Coordinates(1, 2));

		tractorbeam.reset();
		assertFalse(tractorbeam.isLoaded());
		assertTrue(tractorbeam.getCurrentTargets().isEmpty());

	}

	@Test
	public void secondaryFireShooting_correctBehaviour() {
		gameMap.movePlayerTo(players.get(1), new Coordinates(1, 1));
		QuestionContainer initialQuestion = tractorbeam.doActivationStep(0);
		initialQuestion.getOptions().forEach(Utils::logWeapon);
		//Select alternate fire
		QuestionContainer nextQC;
		nextQC = tractorbeam.doActivationStep(initialQuestion.getOptions().indexOf("Alternate fire."));
		//Select a target
		nextQC = tractorbeam.doActivationStep(0);
		//Select coordinate (1,1)

		assertTrue(tractorbeam.isActivationConcluded());
		assertEquals(1, tractorbeam.getPlayersHit().size());

		tractorbeam.reset();
		assertFalse(tractorbeam.isLoaded());
		assertTrue(tractorbeam.getCurrentTargets().isEmpty());
	}

}