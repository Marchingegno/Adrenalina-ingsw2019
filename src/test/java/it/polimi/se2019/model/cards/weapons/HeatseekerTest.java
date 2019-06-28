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
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HeatseekerTest {
	private static String name = "Heatseeker";
	private WeaponCard heatseeker;
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
				heatseeker = new Heatseeker(weapon.getAsJsonObject());
				break;
			}
		}

		List<String> playerNicknames = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			playerNicknames.add("Player " + i);
		}

		model = new ModelDriver(GameConstants.MapType.SMALL_MAP.getMapName(), playerNicknames, 8);
		heatseeker.setGameBoard(model.getGameBoard());


		gameMap = model.getGameBoard().getGameMap();
		players = model.getGameBoard().getPlayers();
		heatseeker.setOwner(players.get(0));


		gameMap.movePlayerTo(players.get(0), new Coordinates(0, 0));
		gameMap.movePlayerTo(players.get(1), new Coordinates(2, 3));
		gameMap.movePlayerTo(players.get(2), new Coordinates(1, 1));
	}

	@Test
	public void primaryFireShooting_correctBehaviour() {
		QuestionContainer targets = heatseeker.doActivationStep(0);

		assertTrue(targets.getOptions().contains(players.get(1).getPlayerName()));
		assertFalse(targets.getOptions().contains(players.get(2).getPlayerName()));

		//Select target
		heatseeker.doActivationStep(0);

		assertTrue(heatseeker.isActivationConcluded());
		assertEquals(1, heatseeker.getPlayersHit().size());

		heatseeker.reset();
		assertFalse(heatseeker.isLoaded());
		assertTrue(heatseeker.currentTargets.isEmpty());

	}

}